package com.spacebitlabs.plantly.data

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.reminder.WorkReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class PlantsBackupManager(
    private val appContext: Context,
    private val workReminder: WorkReminder
) {

    /**
     * https://github.com/googlesamples/android-architecture-components/issues/340#issuecomment-451084063
     */
    suspend fun backup() {
        withContext(Dispatchers.IO) {
            // get database files
            val dbFile = appContext.getDatabasePath(Injection.DATABASE_FILE_NAME).path
            val dbFilePaths = listOf(dbFile, "$dbFile-shm", "$dbFile-wal")

            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return@withContext
            }

            // get pictures files
            val picturesDir = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val srcPicPaths = if (picturesDir == null) emptyList() else picturesDir.listFiles().map { it.path }
            val srcFiles = dbFilePaths + srcPicPaths

            val destPath = File(Environment.getExternalStorageDirectory(), "plantly_backup")
            destPath.mkdir()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            val zipFile = File(destPath, "plantly_backup_${LocalDateTime.now().format(formatter)}.zip")

            addFilesToZip(zipFile, srcFiles)
        }
    }

    /**
     *
     */
    suspend fun restore(fileUri: Uri) {
        withContext(Dispatchers.IO) {
            val tempDestDir = File(Environment.getExternalStorageDirectory(), "plantly_backup_extracted")

            extractFromZip(fileUri, tempDestDir)

            val dbFileName = Injection.DATABASE_FILE_NAME
            val backupFilePathList = listOf(
                "$tempDestDir${File.separator}$dbFileName",
                "$tempDestDir${File.separator}$dbFileName-shm",
                "$tempDestDir${File.separator}$dbFileName-wal"
            )

            val dbDirectory = appContext.getDatabasePath(dbFileName).parent

            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return@withContext
            }

            for (filePath in backupFilePathList) {
                val inFile = File(filePath)
                val fileName = filePath.split(File.separator).last()
                val outFile = File(dbDirectory, fileName)

                inFile.copyTo(outFile, true)
            }

            val picturesDir = File(tempDestDir, "pictures")
            val pictureList = picturesDir.listFiles { _, name -> name.contains(".jpg") }
            val toPath = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            for (picture in pictureList) {
                Timber.d(picture.name)
                val destFile = File(toPath, picture.name)
                picture.copyTo(destFile, true)
            }

            tempDestDir.deleteRecursively()

            workReminder.scheduleDailyReminder()
        }
    }

    private fun extractFromZip(fileUri: Uri, destDir: File) {
        val zipFileStream = appContext.contentResolver.openInputStream(fileUri)
        ZipInputStream(zipFileStream).use { zipInputStream ->
            var zipEntry = zipInputStream.nextEntry
            while (zipEntry != null) {
                val fileNameParts = zipEntry.name.split(File.separator)
                val fileName = fileNameParts.last()
                val folderPath = fileNameParts.take(fileNameParts.size - 1).joinToString(File.separator)
                val folderPathInDestDir = arrayOf(destDir.path, folderPath).joinToString(File.separator)

                val newFolder = File(folderPathInDestDir)
                newFolder.mkdirs()

                val newFile = File(folderPathInDestDir, fileName)

                FileOutputStream(newFile).use { fileOutputStream ->
                    zipInputStream.copyTo(fileOutputStream, 1024)
                }
                zipEntry = zipInputStream.nextEntry
            }
        }
    }

    /**
     * Add files to zip
     *
     * If their extension is jpg, save them
     * in the pictures folder inside the zip
     */
    private fun addFilesToZip(zipFile: File, fileList: List<String>) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile.path))).use { out ->
            for (filePath in fileList) {
                FileInputStream(filePath).use { inFile ->
                    BufferedInputStream(inFile).use { inFileStream ->
                        val destFileName = filePath.split(File.separator).last()
                        val destFilePath = if (!destFileName.endsWith("jpg")) destFileName else arrayOf("pictures", destFileName).joinToString(File.separator)
                        val entry = ZipEntry(destFilePath)
                        out.putNextEntry(entry)
                        inFileStream.copyTo(out, 1024)
                    }
                }
            }
        }
    }

    private fun addFolderToZip(srcFolder: File, folderInZip: String = "", zipOutputStream: ZipOutputStream) {
        for (file in srcFolder.listFiles()) {
            if (file.isDirectory) {
                addFolderToZip(file, arrayOf(folderInZip, file, file.name).joinToString(File.separator), zipOutputStream)
            } else {
                FileInputStream(file.path).use { inFile ->
                    BufferedInputStream(inFile).use { inFileStream ->
                        val entry = ZipEntry(file.path)
                        zipOutputStream.putNextEntry(entry)
                        inFileStream.copyTo(zipOutputStream, 1024)
                    }
                }
            }
        }
    }
}
