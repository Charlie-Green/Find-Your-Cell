package by.zenkevich_churun.findcell.serial.sched.pojo


interface SchedulePojo: LightSchedulePojo {
    var start: Long
    var end: Long
    val cells: List<CellPojo>
}