package com.spacebitlabs.plantly.data

import android.content.Context
import android.os.Environment
import com.spacebitlabs.plantly.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlantsBackupManager(private val appContext: Context) {

    /**
     * https://github.com/googlesamples/android-architecture-components/issues/340#issuecomment-451084063
     * TODO backup photos
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
                val inStream = File(filePath).inputStream()
                val fileName = filePath.split(File.separator).last()
                val outStream = FileOutputStream(toPath.toString() + File.separator + fileName)

                inStream.use { input ->
                    outStream.use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    /**
     * TODO restore photos + workmanager reminders
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
                val inStream = File(filePath).inputStream()
                val fileName = filePath.split(File.separator).last()
                val outStream = FileOutputStream(dbDirectory.toString() + File.separator + fileName)

                inStream.use { input ->
                    outStream.use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}
