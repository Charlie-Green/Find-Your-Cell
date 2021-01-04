package by.zenkevich_churun.findcell.remote.retrofit.jail.pojo

import by.zenkevich_churun.findcell.entity.entity.Cell


internal class Cell1(
    override val jailId: Int,
    override val number: Short,
    override val jailName: String,
    override val seats: Short
): Cell()