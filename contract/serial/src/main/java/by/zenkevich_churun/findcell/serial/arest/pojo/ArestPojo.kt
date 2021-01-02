package by.zenkevich_churun.findcell.serial.arest.pojo

import by.zenkevich_churun.findcell.entity.entity.LightArest


/** Extended abstraction of [LightArest] for client-server interaction.
  * Includes credentials **/
abstract class ArestPojo: LightArest() {
    abstract var prisonerId: Int?
    abstract var passwordBase64: String?
}