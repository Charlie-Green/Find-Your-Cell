package by.sviazen.result.sync.scheduler

import android.content.Context
import by.sviazen.core.util.std.toBoolean
import by.sviazen.core.util.std.toLong
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile


internal class SyncMetaStorage(
    private val appContext: Context ) {


    var lastSyncTime: Long
        get() { return getLongAt(INDEX_ANY_SYNC, DEFAULT_TIME) }
        set(value) { setLongAt(INDEX_ANY_SYNC, value) }

    var lastSuccessfulSyncTime: Long
        get() { return getLongAt(INDEX_SUCCESSFUL_SYNC, DEFAULT_TIME) }
        set(value) { setLongAt(INDEX_SUCCESSFUL_SYNC, value) }


    private val file: File
        get() = File(appContext.filesDir, FILENAME)

    private fun getLongAt(index: Int, defaultValue: Long): Long {
        val byteOffset = index.toLong() * Long.SIZE_BYTES
        if(!file.exists() || file.length() < byteOffset + Long.SIZE_BYTES) {
            return defaultValue
        }

        synchronized(this) {
            RandomAccessFile(file, "r").use { file ->
                file.seek(byteOffset)
                return file.readLong()
            }
        }
    }

    private fun setLongAt(index: Int, value: Long) {
        val byteOffset = index.toLong() * Long.SIZE_BYTES

        synchronized(this) {
            RandomAccessFile(file, "rw").use { file ->
                file.seek(byteOffset)
                file.writeLong(value)
            }
        }
    }


    companion object {
        private const val FILENAME = "SyncMeta.bin"
        private const val INDEX_ANY_SYNC        = 0
        private const val INDEX_SUCCESSFUL_SYNC = 1

        /** Used as default value for some properties
          * if actual data is missing on storage.
          * [Long.MIN_VALUE] is not used to avoid arithmetic overflow. **/
        private const val DEFAULT_TIME = Int.MIN_VALUE.toLong()
    }
}