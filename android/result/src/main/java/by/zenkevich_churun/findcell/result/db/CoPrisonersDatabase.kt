package by.zenkevich_churun.findcell.result.db

import android.content.Context
import androidx.room.*
import by.zenkevich_churun.findcell.result.db.convert.*
import by.zenkevich_churun.findcell.result.db.dao.CoPrisonersDao
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerContactEntity
import by.zenkevich_churun.findcell.result.db.entity.CoPrisonerEntity
import by.zenkevich_churun.findcell.result.db.migr.*


@Database(
    entities = [
        CoPrisonerEntity::class,
        CoPrisonerContactEntity::class
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