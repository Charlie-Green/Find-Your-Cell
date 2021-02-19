package by.zenkevich_churun.findcell.serial.sched.pojo


interface LightSchedulePojo {
    var arestId: Int?
    var passwordBase64: String?
    val periods: List<PeriodPojo>
}