package by.zenkevich_churun.findcell.serial.prisoner.v1.pojo

import by.zenkevich_churun.findcell.entity.entity.Contact


/** Not a POJO, basically, because, as of protocol version 1, [Contact]s are represented
  * as a bunch of plain key-value pairs, not fully-qualified JSON objects.
  * But the name is picked to highlight the tiny relation of this class to [PrisonerPojo1]. **/
internal class ContactPojo1(
    override val type: Type,
    override val data: String
): Contact()