package br.com.winestore.service

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import br.com.winestore.utils.WineStoreUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import java.util.Optional;

@Service
class WineStoreService(
    private val wineStoreRepository: WineStoreRepository
) {

    @Throws(BaseException::class)
    fun createWineStore(request: WineStore): WineStore {
        if (WineStoreUtils.atributtesAreNull(request)) throw BaseException(
            "All of the fields must not be null, verify your data",
            HttpStatus.BAD_REQUEST
        )
        if (request.faixaFim <= request.faixaInicio) throw BaseException(
            "FAIXA_FIM must be greater than FAIXA_INICIO",
            HttpStatus.BAD_REQUEST
        )
        if (!WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository)) {
            throw BaseException("There is a zip range conflit, verify your data", HttpStatus.BAD_REQUEST)
        }
        return wineStoreRepository.save(request)
    }

    fun listAllWineStores(faixaInicio: Long?, faixaFim: Long?, codigoLoja: String?): List<WineStore> {
        if (codigoLoja != null) return wineStoreRepository.findByCodigoLoja(codigoLoja)
        if (faixaInicio != null) return wineStoreRepository.findByFaixaInicioGreaterThan(faixaInicio)
        return if (faixaFim != null) wineStoreRepository.findByFaixaFimLessThan(faixaFim) else wineStoreRepository.findAll()
    }

    @Throws(BaseException::class)
    fun findWineStoreById(id: Long): WineStore {
        val wineStoreOp: Optional<WineStore> = wineStoreRepository.findById(id)
        if (!wineStoreOp.isPresent()) throw BaseException(
            "There isn't a wine store with id = $id",
            HttpStatus.NOT_FOUND
        )
        return wineStoreOp.get()
    }

    @Throws(BaseException::class)
    fun updateWineStore(request: WineStore, id: Long): WineStore? {
        if (request.faixaFim <= request.faixaInicio) throw BaseException(
            "FAIXA_FIM MUST BE GREATER THAN FAIXA_INICIO",
            HttpStatus.BAD_REQUEST
        )
        val wineStoreOp: Optional<WineStore> = wineStoreRepository.findById(id)
        if (!wineStoreOp.isPresent()) throw BaseException(
            "There isn't a wine store with id = $id",
            HttpStatus.NOT_FOUND
        )
        if (!WineStoreUtils.canCreateOrUpdateWineStore(request, wineStoreRepository)) {
            throw BaseException("There is a zip range conflit, verify your data", HttpStatus.BAD_REQUEST)
        }
        val wineStore: WineStore = wineStoreOp.get()
        if (request.codigoLoja != null) {
            wineStore.codigoLoja = request.codigoLoja
        }
        if (request.faixaInicio != null) {
            wineStore.faixaInicio = request.faixaInicio
        }
        if (request.faixaFim != null) {
            wineStore.faixaFim = request.faixaFim
        }
        wineStoreRepository.save(wineStore)
        return wineStore
    }

    @Throws(BaseException::class)
    fun deleteWineStore(id: Long) {
        val room: Optional<WineStore> = wineStoreRepository.findById(id)
        if (!room.isPresent) throw BaseException("Wine Store ID haven't found", HttpStatus.NOT_FOUND)
        wineStoreRepository.deleteById(id)
    }

}