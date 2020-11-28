package by.zenkevich_churun.findcell.prisoner.db

import android.content.Context
import androidx.room.*
import by.zenkevich_churun.findcell.prisoner.db.dao.*
import by.zenkevich_churun.findcell.prisoner.db.entity.*
import by.zenkevich_churun.findcell.prisoner.db.version.JailsDatabaseVersion


/** [Jail]s and [Cell]s are cached in this database. **/
@Database(
    entities = [
        JailEntity::class,
        CellEntity::class
    ],
    version = JailsDatabaseVersion.APP_1_0,
    exportSchema = false
)
abstract class JailsDatabase: RoomDatabase() {

    abstract val jailsDao: JailsDao
    abstract val cellsDao: CellsDao


    companion object {
        private var instance: JailsDatabase? = null

        fun get(appContext: Context): JailsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildInstance(appContext).also {
                    instance = it
                }
            }
        }

        private fun buildInstance(appContext: Context): JailsDatabase {
            return Room
                .databaseBuilder(appContext, JailsDatabase::class.java, "jails.db")
                .build()
        }
    }
}