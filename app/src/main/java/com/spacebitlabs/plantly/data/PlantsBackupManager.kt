package com.spacebitlabs.plantly.data

import android.content.Context
import android.os.Environment
import com.spacebitlabs.plantly.Injection
import com.spacebitlabs.plantly.reminder.WorkReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

class PlantsBackupManager(
    private val appContext: Context,
    private val workReminder: WorkReminder
) {

    /**
     * https://github.com/googlesamples/android-architecture-components/issues/340#issuecomment-451084063
     */
    suspend fun backup() {
        withContext(Dispatchers.IO) {
            val dbFile = appContext.getDatabasePath(Injection.DATABASE_FILE_NAME).path
            val dbFilePathList = listOf(dbFile, "$dbFile-shm", "$dbFile-wal")

            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return@withContext
            }

            val toPath = File(Environment.getExternalStorageDirectory(), "plantly_backup")
            toPath.mkdir()

            for (filePath in dbFilePathList) {
                val inFile = File(filePath)
                val fileName = filePath.split(File.separator).last()
                val outFile = File(toPath, fileName)

                inFile.copyTo(outFile)
            }

            val pictures = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            pictures ?: return@withContext

            for (picture in pictures.listFiles()) {
                val picturesDir = File(toPath, "pictures")
                val destFile = File(picturesDir, picture.name)
                picture.copyTo(destFile, true)
            }
        }
    }

    /**
     *
     */
    suspend fun restore() {
        withContext(Dispatchers.IO) {
            val dbFileName = Injection.DATABASE_FILE_NAME
            val backupPath = File(Environment.getExternalStorageDirectory(), "plantly_backup")
            val backupFilePathList = listOf(
                "$backupPath${File.separator}$dbFileName",
                "$backupPath${File.separator}$dbFileName-shm",
                "$backupPath${File.separator}$dbFileName-wal"
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

            val picturesDir = File(backupPath, "pictures")
            val pictureList = picturesDir.listFiles { _, name -> name.contains(".jpg") }
            val toPath = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            for (picture in pictureList) {
                Timber.d(picture.name)
                val destFile = File(toPath, picture.name)
                picture.copyTo(destFile, true)
            }

            workReminder.scheduleDailyReminder()
        }
    }
}
