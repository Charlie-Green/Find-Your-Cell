package by.sviazen.result.db

import android.content.Context
import androidx.room.*
import by.sviazen.result.db.convert.*
import by.sviazen.result.db.dao.CoPrisonersDao
import by.sviazen.result.db.entity.CoPrisonerEntity
import by.sviazen.result.db.migr.*


@Database(
    entities = [
        CoPrisonerEntity::class
    ],
    version = CoPrisonerDatabaseVersions.APP_1_0,
    exportSchema = false
)
@TypeConverters(
    CoPrisonerRelationRoomConverter::class,
    ContactTypeRoomConverter::class
)
abstract class CoPrisonersDatabase: RoomDatabase() {

    abstract val dao: CoPrisonersDao


    companion object {
        private var instance: CoPrisonersDatabase? = null


        fun get(appContext: Context): CoPrisonersDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(appContext).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(appContext: Context): CoPrisonersDatabase {
            return Room
                .databaseBuilder(appContext, CoPrisonersDatabase::class.java, "coprisoner.db")
                .build()
        }
    }
}