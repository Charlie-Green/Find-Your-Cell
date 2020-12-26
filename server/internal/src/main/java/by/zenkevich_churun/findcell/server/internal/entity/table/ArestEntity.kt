package by.zenkevich_churun.findcell.server.internal.entity.table

import jdk.nashorn.internal.ir.annotations.Ignore
import java.util.Calendar
import javax.persistence.*


@Entity
@Table(name = "Arests")
class ArestEntity {

    @Ignore
    val start = Calendar.getInstance()

    @Ignore
    val end = Calendar.getInstance()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    // TODO: This is only for me to know how to implement such case.
    // Then I'm gonna remove all the Calendars!
    @get:Column(name = "start")
    var startMillis: Long
        get() { return start.timeInMillis }
        set(value) { start.timeInMillis = value }

    @get:Column(name = "end")
    var endMillis: Long
        get() { return end.timeInMillis }
        set(value) { end.timeInMillis = value }
}