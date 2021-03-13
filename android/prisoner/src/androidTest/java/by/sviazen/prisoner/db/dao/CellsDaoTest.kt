package by.sviazen.prisoner.db.dao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import by.sviazen.prisoner.db.JailsDatabase
import by.sviazen.prisoner.db.entity.CellEntity
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class CellsDaoTest {

    private lateinit var db: JailsDatabase


    @Before
    fun setup() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(appContext, JailsDatabase::class.java)
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    prepopulate(db)
                }
            })
            .allowMainThreadQueries()
            .build()
    }


    @Test
    fun jailNotMatch() = testCellFound(
        1, 1,
        2, 1,
        false
    )

    @Test
    fun cellNotMatch() = testCellFound(
        1, 1,
        1, 2,
        false
    )

    @Test
    fun everythingMatches() = testCellFound(
        1, 1,
        1, 1,
        true
    )

    @Test
    fun content1() = testCellContent(1, JAIL_1_NAME)

    @Test
    fun content2() = testCellContent(2, JAIL_2_NAME)


    private fun prepopulate(db: SupportSQLiteDatabase) {
        val insertJailStat = db.compileStatement(
            "insert into Jails(id, name, cells) " +
            "values(?, ?, 1)"
        )
        insertJailStat.apply {
            bindLong(1, 1L)
            bindString(2, JAIL_1_NAME)
            executeInsert()
        }
        insertJailStat.apply {
            bindLong(1, 2L)
            bindString(2, JAIL_2_NAME)
            executeInsert()
        }
    }

    private fun testCellFound(
        insertedJailId: Int, insertedCellNumber: Short,
        queriedJailId: Int, queriedCellNumber: Short,
        mustCellBeFound: Boolean ) {

        val insertedCell = CellEntity(insertedJailId, insertedCellNumber, 1)

        val dao = db.cellsDao
        dao.addOrUpdate( listOf(insertedCell) )
        val actualCell = dao.get(queriedJailId, queriedCellNumber)

        if(mustCellBeFound) {
            assertThat(actualCell).isNotNull()
        } else {
            assertThat(actualCell).isNull()
        }
    }

    private fun testCellContent(jailId: Int, jailName: String) {
        val expectedCell = CellEntity(jailId, 3, 12)

        val dao = db.cellsDao
        dao.addOrUpdate( listOf(expectedCell) )
        val actualCell = dao.get(jailId, 3)

        assertThat(actualCell).isNotNull()
        assertThat(actualCell!!.jailId  ).isEqualTo(expectedCell.jailId)
        assertThat(actualCell!!.jailName).isEqualTo(jailName)
        assertThat(actualCell!!.number  ).isEqualTo(expectedCell.number)
        assertThat(actualCell!!.seats   ).isEqualTo(expectedCell.seats)
    }


    companion object {
        private const val JAIL_1_NAME = "Uno"
        private const val JAIL_2_NAME = "Dos"
    }
}