package by.sviazen.server.internal.entity

import by.sviazen.server.internal.entity.key.CellKey
import by.sviazen.server.internal.entity.table.CellEntity
import by.sviazen.server.internal.entity.view.FullJailView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class FullJailViewTest {

    @Test
    fun empty() = test( listOf(
        // Empty list.
    ))

    @Test
    fun simple() = test( listOf(
        1, 3, 4, 2
    ))

    @Test
    fun withInternalGaps() = test( listOf(
        2, -1, -3, 1, -5, 3
    ))

    @Test
    fun withBoundingGaps() = test( listOf(
        -1, 5, 7, 3
    ))

    @Test
    fun withDuplications() = test( listOf(
        4, 1, 4, 2, 1
    ))

    @Test
    fun mixed() = test( listOf(
        -7, -3, 5, 7, -1, 1, 5, -2, 3, 7
    ))


    private fun test(seatCounts: List<Short>) {
        val view = FullJailView()

        val cellEntities = view
            .cellEntitiesSet
            as? MutableSet<CellEntity>
            ?: hashSetOf()
        view.cellEntitiesSet = cellEntities

        for(index in seatCounts.indices) {
            if(seatCounts[index] < 0) {
                continue
            }

            val cell = CellEntity()
            cell.key = CellKey()
            cell.key.cellNumber = (index + 1).toShort()
            cell.seatCount = seatCounts[index]

            cellEntities.add(cell)
        }

        assertCollectionsEqual(seatCounts, view.cells) { t1, t2 ->
            (t1 < 0 && t2 < 0) || (t1 == t2)
        }
    }


    private fun <T> assertCollectionsEqual(
        expected: Collection<T>,
        actual: Collection<T>,
        equal: (T, T) -> Boolean = { t1, t2 -> t1 == t2 } ) {

        Assertions.assertEquals(expected.size, actual.size)

        for(expectedItem in expected) {
            val expectedCount = expected.count { item ->
                equal(expectedItem, item)
            }

            val actualCount = actual.count { actualItem ->
                equal(expectedItem, actualItem)
            }

            Assertions.assertEquals(expectedCount, actualCount)
        }
    }
}