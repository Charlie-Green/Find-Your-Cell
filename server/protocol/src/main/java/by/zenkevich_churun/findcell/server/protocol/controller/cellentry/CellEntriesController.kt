package by.zenkevich_churun.findcell.server.protocol.controller.cellentry

import by.zenkevich_churun.findcell.domain.contract.cellentry.CellEntryPojo
import by.zenkevich_churun.findcell.domain.util.Base64Coder
import by.zenkevich_churun.findcell.domain.util.Deserializer
import by.zenkevich_churun.findcell.server.internal.repo.cellentry.CellEntriesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream


@RestController
class CellEntriesController {

    @Autowired
    private lateinit var repo: CellEntriesRepository

    @Autowired
    private lateinit var base64Coder: Base64Coder


    @PostMapping("cell/add")
    fun add(input: InputStream): String {

        val cell = deserializeCell(input)
        val passwordHash = base64Coder.decode(cell.passwordBase64)
        repo.add(cell, passwordHash)

        return ""
    }

    // TODO: in future versions...
//    @PostMapping("cell/update")
//    fun update(input: InputStream): String {
//
//    }

    @PostMapping("cell/delete")
    fun delete(input: InputStream): String {

        val cell = deserializeCell(input)
        val passwordHash = base64Coder.decode(cell.passwordBase64)
        repo.delete(cell, passwordHash)

        return ""
    }


    private fun deserializeCell(
        input: InputStream
    ) = Deserializer.fromJsonStream(input, CellEntryPojo::class.java)
}