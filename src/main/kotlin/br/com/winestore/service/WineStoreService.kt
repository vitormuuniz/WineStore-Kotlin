package br.com.winestore.service

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import br.com.winestore.utils.WineStoreUtils.Companion.validateFields
import br.com.winestore.utils.WineStoreUtils.Companion.validateFieldsRange
import br.com.winestore.utils.WineStoreUtils.Companion.getIfExists
import org.springframework.stereotype.Service

@Service
class WineStoreService(
    private val wineStoreRepository: WineStoreRepository
) {

    @Throws(BaseException::class)
    fun createWineStore(request: WineStore): WineStore? {
        validateFields(request)
        validateFieldsRange(request, wineStoreRepository)
        return wineStoreRepository.save(request)
    }

    fun listAllWineStores(faixaInicio: Long?, faixaFim: Long?, codigoLoja: String?): List<WineStore> {
        if (codigoLoja != null) {
            return wineStoreRepository.findByCodigoLoja(codigoLoja)
        }
        if (faixaInicio != null && faixaFim != null) {
            return wineStoreRepository.findBetweenFaixaInicioAndFaixaFim(faixaInicio, faixaFim)
        }
        if (faixaInicio != null) {
            return wineStoreRepository.findByFaixaInicioGreaterThan(faixaInicio)
        }
        if (faixaFim != null)  {
            return wineStoreRepository.findByFaixaFimLessThan(faixaFim)
        }
        return wineStoreRepository.findAll()
    }

    @Throws(BaseException::class)
    fun findWineStoreById(id: Long): WineStore {
        return getIfExists(id, wineStoreRepository)
    }

    @Throws(BaseException::class)
    fun updateWineStore(request: WineStore, id: Long): WineStore? {
        val wineStore = getIfExists(id, wineStoreRepository)
        return wineStoreRepository.save(
            wineStore.copy(
                codigoLoja = request.codigoLoja ?: wineStore.codigoLoja,
                faixaInicio = request.faixaInicio ?: wineStore.faixaInicio,
                faixaFim = request.faixaFim ?: wineStore.faixaFim
            )
        )
    }

    @Throws(BaseException::class)
    fun deleteWineStore(id: Long) {
        val wineStore = getIfExists(id, wineStoreRepository)
        wineStoreRepository.deleteById(wineStore.id)
    }
}
