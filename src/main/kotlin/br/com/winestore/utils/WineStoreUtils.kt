package br.com.winestore.utils

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import org.springframework.http.HttpStatus
import org.springframework.util.StringUtils
import java.util.Objects

class WineStoreUtils {

    companion object {

        @Throws(BaseException::class)
        fun validateFields(to: WineStore) {
            if (isNullOrEqualsZero(to.faixaInicio) || isNullOrEqualsZero(to.faixaFim)) {
                throw BaseException(
                    "faixaInicio and faixaFim must be non null and greater than zero",
                    HttpStatus.BAD_REQUEST
                )
            }
            if (!StringUtils.hasText(to.codigoLoja)) {
                throw BaseException("codigoLoja must be non null and not blank", HttpStatus.BAD_REQUEST)
            }
        }

        private fun isNullOrEqualsZero(faixa: Long?): Boolean {
            return Objects.isNull(faixa) || faixa == 0L
        }

        @Throws(BaseException::class)
        fun validateFieldsRange(to: WineStore, wineStoreRepository: WineStoreRepository) {
            if (to.faixaFim!! <= to.faixaInicio!!) {
                throw BaseException("faixaFim must be greater than faixaInicio", HttpStatus.BAD_REQUEST)
            }
            val wineStores: List<WineStore> = wineStoreRepository.findWineStoresFiltered(to.faixaInicio, to.faixaFim)
            if (wineStoreRepository.count() > 0 && wineStores.isNotEmpty()) {
                throw BaseException("There is a zip range conflict, verify your data", HttpStatus.BAD_REQUEST)
            }
        }

        fun getIfExists(id: Long, wineStoreRepository: WineStoreRepository): WineStore {
            return wineStoreRepository.findById(id).orElseThrow {
                throw BaseException(
                    "There isn't a wine store with id = $id",
                    HttpStatus.NOT_FOUND
                )
            }
        }
    }
}