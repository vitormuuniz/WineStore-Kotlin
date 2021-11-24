package br.com.winestore.utils

import br.com.winestore.model.WineStore

import br.com.winestore.repository.WineStoreRepository

class WineStoreUtils {

    companion object {
        fun canCreateOrUpdateWineStore(wineStoreRequest: WineStore, wineStoreRepository: WineStoreRepository): Boolean {
            val winStoreList = wineStoreRepository.findAll()
            if (winStoreList.size > 0) {
                for (wineStore in winStoreList) {
                    if (!zipRangeVerification(wineStoreRequest, wineStore)) return false
                }
            }
            return true
        }

        private fun zipRangeVerification(wsToBeCreate: WineStore, wsSaved: WineStore): Boolean {
            if (wsToBeCreate.faixaInicio >= wsSaved.faixaInicio
                && wsToBeCreate.faixaInicio <= wsSaved.faixaFim
            ) return false
            if (wsToBeCreate.faixaFim >= wsSaved.faixaInicio
                && wsToBeCreate.faixaFim <= wsSaved.faixaFim
            ) return false
            if (wsToBeCreate.faixaInicio >= wsSaved.faixaInicio
                && wsToBeCreate.faixaFim <= wsSaved.faixaFim
            ) return false
            return !(wsToBeCreate.faixaInicio <= wsSaved.faixaInicio
                    && wsToBeCreate.faixaFim >= wsSaved.faixaFim)
        }

        fun atributtesAreNull(to: WineStore): Boolean {
            return to.codigoLoja == null || to.faixaInicio == null || to.faixaFim == null
        }
    }
}