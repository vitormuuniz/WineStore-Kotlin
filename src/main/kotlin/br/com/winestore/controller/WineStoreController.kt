package br.com.winestore.controller

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.service.WineStoreService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
class WineStoreController(
    val wineStoreService: WineStoreService
) {

    @PostMapping
    @Throws(BaseException::class)
    fun registerRoom(
        @RequestBody @Valid request: WineStore,
    ): ResponseEntity<WineStore> {
        return ResponseEntity.status(HttpStatus.CREATED).body(wineStoreService.createWineStore(request))
    }

    @GetMapping
    @Throws(BaseException::class)
    fun listWineRooms(
        @RequestParam(required = false) faixaInicio: Long?,
        @RequestParam(required = false) faixaFim: Long?,
        @RequestParam(required = false) codigoLoja: String?
    ): ResponseEntity<List<WineStore>> {
        return ResponseEntity.ok(wineStoreService.listAllWineStores(faixaInicio, faixaFim, codigoLoja))
    }

    @GetMapping("/{id}")
    @Throws(BaseException::class)
    fun listOneWineStoreById(@PathVariable id: Long): ResponseEntity<WineStore?>? {
        return ResponseEntity.ok(wineStoreService.findWineStoreById(id))
    }

    @PutMapping("/{id}")
    @Throws(BaseException::class)
    fun updateWineStore(
        @RequestBody form: @Valid WineStore,
        @PathVariable id: Long
    ): ResponseEntity<WineStore> {
        return ResponseEntity.ok(wineStoreService.updateWineStore(form, id))
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Throws(BaseException::class)
    fun deleteWineStore(@PathVariable id: Long): ResponseEntity<Any> {
        wineStoreService.deleteWineStore(id)
        return ResponseEntity.ok().build()
    }
}