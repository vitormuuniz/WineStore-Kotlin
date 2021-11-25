package br.com.winestore.service

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import br.com.winestore.utils.WineStoreUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import java.util.Optional

@Service
class WineStoreService(
    private val wineStoreRepository: WineStoreRepository
) {

    @Throws(BaseException::class)
    fun createWineStore(request: WineStore): WineStore? {
        WineStoreUtils.verifyAttributesNull(request)
        var wineStore: WineStore? = null
        if (WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository, false))
            wineStore = wineStoreRepository.save(request)

        return wineStore
    }

    fun listAllWineStores(faixaInicio: Long?, faixaFim: Long?, codigoLoja: String?): List<WineStore> {
        if (codigoLoja != null) return wineStoreRepository.findByCodigoLoja(codigoLoja)
        if (faixaInicio != null) return wineStoreRepository.findByFaixaInicioGreaterThan(faixaInicio)
        return if (faixaFim != null) wineStoreRepository.findByFaixaFimLessThan(faixaFim) else wineStoreRepository.findAll()
    }

    @Throws(BaseException::class)
    fun findWineStoreById(id: Long): WineStore {
        val wineStoreOp = WineStoreUtils.verifyIfExists(id, wineStoreRepository)
        return wineStoreOp.get()
    }

    @Throws(BaseException::class)
    fun updateWineStore(request: WineStore, id: Long): WineStore? {
        val wineStoreOp = WineStoreUtils.verifyIfExists(id, wineStoreRepository)
        var wineStore: WineStore? = null
        if (WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository, true)) {
            wineStore = wineStoreOp.get()
            wineStoreRepository.save(
                wineStore.copy(
                    codigoLoja = request.codigoLoja ?: wineStore.codigoLoja,
                    faixaInicio = request.faixaInicio ?: wineStore.faixaInicio,
                    faixaFim = request.faixaFim ?: wineStore.faixaFim
                )
            )
        }
        return wineStore
    }

    @Throws(BaseException::class)
    fun deleteWineStore(id: Long) {
        val wineStore: Optional<WineStore> = wineStoreRepository.findById(id)
        if (wineStore.isEmpty) throw BaseException("Wine Store ID haven't found", HttpStatus.NOT_FOUND)
        wineStoreRepository.deleteById(id)
    }

}