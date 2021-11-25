package br.com.winestore.utils

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore

import br.com.winestore.repository.WineStoreRepository
import org.springframework.http.HttpStatus
import java.util.*

class WineStoreUtils {

    companion object {
        fun canCreateOrUpdateWineStore(wineStoreRequest: WineStore, wineStoreRepository: WineStoreRepository, isUpdate: Boolean): Boolean {
            if (wineStoreRequest.faixaInicio != null && wineStoreRequest.faixaFim != null) {
                if (wineStoreRequest.faixaFim <= wineStoreRequest.faixaInicio) throw BaseException(
                    "faixaFim must be greater than faixaInicio",
                    HttpStatus.BAD_REQUEST
                )
                val winStoreList =
                    wineStoreRepository.findWineStoresFiltered(wineStoreRequest.faixaInicio, wineStoreRequest.faixaFim)
                if (winStoreList.isEmpty())
                    throw BaseException("There is a zip range conflict, verify your data", HttpStatus.BAD_REQUEST)
            } else if (!isUpdate) throw BaseException(
                "faixaFim and faixaInicio must be greater than zero and must be not null",
                HttpStatus.BAD_REQUEST
            )
            return true
        }

        fun verifyAttributesNull(to: WineStore) {
            if(to.codigoLoja == "" || to.faixaInicio == 0L || to.faixaFim == 0L)
                throw BaseException(
                    "All of the fields must not be null, verify your data",
                    HttpStatus.BAD_REQUEST
                )
        }

        fun verifyIfExists(id: Long, wineStoreRepository: WineStoreRepository): Optional<WineStore>{
            val wineStoreOp: Optional<WineStore> = wineStoreRepository.findById(id)
            if (wineStoreOp.isEmpty) throw BaseException(
                "There isn't a wine store with id = $id",
                HttpStatus.NOT_FOUND
            )
            return wineStoreOp
        }
    }
}