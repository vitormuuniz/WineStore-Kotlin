package br.com.winestore.repository

import br.com.winestore.model.WineStore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WineStoreRepository: JpaRepository<WineStore, Long> {

    fun findByFaixaInicioGreaterThan(faixaInicio: Long): List<WineStore>
    fun findByFaixaFimLessThan(faixaFim: Long): List<WineStore>
    fun findByCodigoLoja(codigoLoja: String): List<WineStore>
    @Query(value = "" +
            "SELECT * FROM WINE_STORE WHERE " +
            "(?1 NOT BETWEEN faixa_inicio AND faixa_fim) AND" +
            "(?2 NOT BETWEEN faixa_inicio AND faixa_fim) AND" +
            " NOT(?1 >= faixa_inicio AND ?2 <= faixa_fim) AND" +
            " NOT(?1 <= faixa_inicio AND ?2 >= faixa_fim)", nativeQuery = true)
    fun findWineStoresFiltered(faixaInicio: Long, faixaFim: Long): List<WineStore>
}