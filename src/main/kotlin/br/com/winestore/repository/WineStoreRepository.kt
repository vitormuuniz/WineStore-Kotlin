package br.com.winestore.repository

import br.com.winestore.model.WineStore
import org.springframework.data.jpa.repository.JpaRepository

interface WineStoreRepository: JpaRepository<WineStore, Long> {

    fun findByFaixaInicioGreaterThan(faixaInicio: Long): List<WineStore>
    fun findByFaixaFimLessThan(faixaFim: Long): List<WineStore>
    fun findByCodigoLoja(codigoLoja: String): List<WineStore>
}