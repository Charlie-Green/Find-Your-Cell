package by.zenkevich_churun.findcell.server.protocol.controller.cellentry

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


    @PostMapping("cell/delete")
    fun delete(input: InputStream): String {

        val cell = CellEntriesDeserializer
            .forVersion(1)
            .deserialize(input)
        val passwordBase64 = cell.passwordBase64
            ?: throw IllegalServerParameterException("Missing password")

        repo.delete(
            Base64Util.decode(passwordBase64, "delete a cell entry"),
            cell.arestId,
            cell.jailId,
            cell.cellNumber
        )

        return ""
    }
}