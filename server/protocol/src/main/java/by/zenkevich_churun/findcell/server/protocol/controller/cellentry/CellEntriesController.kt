package by.zenkevich_churun.findcell.server.protocol.controller.cellentry

import by.zenkevich_churun.findcell.serial.sched.pojo.CellEntryPojo
import by.zenkevich_churun.findcell.serial.util.protocol.Base64Util
import by.zenkevich_churun.findcell.server.internal.repo.cellentry.CellEntriesRepository
import by.zenkevich_churun.findcell.server.protocol.exc.IllegalServerParameterException
import by.zenkevich_churun.findcell.server.protocol.serial.cellentry.abstr.CellEntriesDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream


@RestController
class CellEntriesController {

    @Autowired
    private lateinit var repo: CellEntriesRepository


    @PostMapping("cell/add")
    fun add(input: InputStream): String {

        val cell = deserializeCell(input)
        val passwordHash = requirePassword(cell.passwordBase64)

        repo.add(
            passwordHash,
            cell.arestId,
            cell.jailId,
            cell.cellNumber
        )

        return ""
    }


    @PostMapping("cell/update")
    fun update(input: InputStream): String {

        val cell = CellEntriesDeserializer
            .forVersion(1)
            .deserializeTwo(input)

        repo.update(
            requirePassword(cell.passwordBase64),
            cell.arestId,
            cell.oldJailId, cell.oldCellNumber,
            cell.newJailId, cell.newCellNumber
        )

        return ""
    }


    @PostMapping("cell/delete")
    fun delete(input: InputStream): String {

        val cell = deserializeCell(input)
        val passwordHash = requirePassword(cell.passwordBase64)

        repo.delete(
            passwordHash,
            cell.arestId,
            cell.jailId,
            cell.cellNumber
        )

        return ""
    }


    private fun deserializeCell(input: InputStream): CellEntryPojo {
        return CellEntriesDeserializer
            .forVersion(1)
            .deserialize(input)
    }

    private fun requirePassword(base64: String?): ByteArray {
        base64 ?: throw IllegalServerParameterException("Missing password")
        return Base64Util.decode(base64)
    }
}