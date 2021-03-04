package by.sviazen.server.protocol.controller.cellentry

import by.sviazen.domain.contract.cellentry.CellEntryPojo
import by.sviazen.domain.contract.cellentry.UpdatedCellEntryPojo
import by.sviazen.domain.util.Base64Coder
import by.sviazen.domain.util.Deserializer
import by.sviazen.server.internal.repo.cellentry.CellEntriesRepository
import by.sviazen.server.protocol.controller.shared.ControllerUtil
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

    @PostMapping("cell/update")
    fun update(input: InputStream): String {

        val pojo = Deserializer
            .fromJsonStream(input, UpdatedCellEntryPojo::class.java)
        ControllerUtil.catchingIllegalArgument {
            val passwordHash = base64Coder.decode(pojo.passwordBase64)
            repo.update(pojo, passwordHash)
        }

        return ""
    }

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