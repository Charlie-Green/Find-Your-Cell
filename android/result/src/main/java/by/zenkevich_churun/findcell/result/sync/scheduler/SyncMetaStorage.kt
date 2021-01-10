package by.zenkevich_churun.findcell.result.sync.scheduler

import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile


internal class SyncMetaStorage(
    private val appContext: Context ) {


    var lastSyncTime: Long
        get() { return getLongAt(INDEX_ANY_SYNC, 0L) }
        set(value) { setLongAt(INDEX_ANY_SYNC, value) }

    var lastSuccessfulSyncTime: Long
        get() { return getLongAt(INDEX_SUCCESSFUL_SYNC, 0L) }
        set(value) { setLongAt(INDEX_SUCCESSFUL_SYNC, value) }


    private val file: File
        get() = File(appContext.filesDir, FILENAME)

    private fun getLongAt(index: Int, defaultValue: Long): Long {
        val byteOffset = index.toLong() * Long.SIZE_BYTES

        synchronized(this) {
            try {
                RandomAccessFile(file, "r").use { file ->
                    file.seek(byteOffset)
                    return file.readLong()
                }
            } catch(exc: FileNotFoundException) {
                return defaultValue
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
        private const val INDEX_ANY_SYNC = 0
        private const val INDEX_SUCCESSFUL_SYNC = 1
    }
}