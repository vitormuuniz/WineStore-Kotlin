package br.com.winestore.controller

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.service.WineStoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
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
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<WineStore> {
        val wineStore: WineStore = wineStoreService.createWineStore(request)
        val uri: URI = uriBuilder.path("/wine-stores/{id}").buildAndExpand(wineStore.id).toUri()
        return ResponseEntity.created(uri).body(wineStore)
    }

    @GetMapping
    @Throws(BaseException::class)
    fun listWineRooms(
        @RequestParam(required = false) faixaInicio: Long?,
        @RequestParam(required = false) faixaFim: Long?,
        @RequestParam(required = false) codigoLoja: String?
    ): ResponseEntity<List<WineStore>> {
        val wineStoreList = wineStoreService.listAllWineStores(faixaInicio, faixaFim, codigoLoja)
        return ResponseEntity.ok(wineStoreList)
    }

    @GetMapping("/{id}")
    @Throws(BaseException::class)
    fun listOneWineStoreById(@PathVariable id: Long): ResponseEntity<WineStore?>? {
        val wineStore = wineStoreService.findWineStoreById(id)
        return ResponseEntity.ok(wineStore)
    }

    @PutMapping("/{id}")
    @Throws(BaseException::class)
    fun updateWineStore(
        @RequestBody form: @Valid WineStore,
        @PathVariable id: Long
    ): ResponseEntity<WineStore> {
        val wineStore = wineStoreService.updateWineStore(form, id)
        return ResponseEntity.ok(wineStore)
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Throws(BaseException::class)
    fun deleteWineStore(@PathVariable id: Long): ResponseEntity<Object> {
        wineStoreService.deleteWineStore(id)
        return ResponseEntity.ok().build()
    }
}