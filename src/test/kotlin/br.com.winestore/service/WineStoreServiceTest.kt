package br.com.winestore.service

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.util.Collections
import java.util.Optional

@SpringBootTest
class WineStoreServiceTest {
    @Autowired
    private lateinit var service: WineStoreService

    @MockBean
    private lateinit var repository: WineStoreRepository

    private lateinit var wsDB: WineStore

    @BeforeEach
    fun init() {
        wsDB = WineStore("12345", 13000L, 14000L)
    }

    @Test
    fun testCreateWineStore() {
        `when`(repository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(Collections.emptyList())
        `when`(repository.save(wsDB)).thenReturn(wsDB)
        val response = service.createWineStore(wsDB)
        validateWineStore(response!!)
    }

    @Test
    fun testCreateWineStoreWithCodigoLojaNullShouldThrowBaseException() {
        wsDB.codigoLoja = null
        val ex = assertThrows(BaseException::class.java, { service.createWineStore(wsDB) },
            "It was expected that createWineStore() thrown an exception, " +
                    "due to codigoLoja null"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        verify(repository, times(0)).save(wsDB)
    }

    @Test
    fun testCreateWineStoreWithFaixaInicioNullShouldThrowBaseException() {
        wsDB.faixaInicio = null
        val ex = assertThrows(BaseException::class.java, { service.createWineStore(wsDB) },
            "It was expected that createWineStore() thrown an exception, " +
                    "due to faixaInicio null"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        verify(repository, times(0)).save(wsDB)
    }

    @Test
    fun testCreateWineStoreWithFaixaFimNullShouldThrowBaseException() {
        wsDB.faixaFim = null
        val ex = assertThrows(BaseException::class.java, { service.createWineStore(wsDB) },
            "It was expected that createWineStore() thrown an exception, " +
                    "due to faixaFim null"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        verify(repository, times(0)).save(wsDB)
    }

    @Test
    fun testCreateWineStoreWithFaixaInicioGreaterThanFaixaFimShouldThrowBaseException() {
        wsDB.faixaInicio = 15000L
        wsDB.faixaFim = 14000L
        val ex = assertThrows(BaseException::class.java, { service.createWineStore(wsDB) },
            "It was expected that createWineStore() thrown an exception, " +
                    "due to faixaInicio greater than faixaFim"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        verify(repository, times(0)).save(wsDB)
    }

    @Test
    fun testCreateWineStoreWithZipRangeConflictShouldThrowBaseException() {
        `when`(repository.findWineStoresFiltered(anyLong(), anyLong())).thenReturn(Collections.singletonList(WineStore()))
        `when`(repository.count()).thenReturn(1L)

        val ex = assertThrows(BaseException::class.java, { service.createWineStore(wsDB) },
            "It was expected that createWineStore() thrown an exception, " +
                    "due to zip range conflict"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        verify(repository, times(0)).save(wsDB)
    }

    @Test
    fun testListAllWineStoresByCodigoLoja() {
        `when`(repository.findByCodigoLoja(anyString())).thenReturn(Collections.singletonList(wsDB))
        service.listAllWineStores(null, null, wsDB.codigoLoja)
            .stream()
            .findFirst()
            .ifPresent(this::validateWineStore)
        verify(repository, times(1)).findByCodigoLoja(wsDB.codigoLoja!!)
    }

    @Test
    fun testListAllWineStores() {
        val ws = WineStore("12345", 10000L, 11000L)
        `when`(repository.findAll()).thenReturn(listOf(wsDB, ws))
        val response = service.listAllWineStores(null, null, null)
        assertEquals(2, response.size)
        verify(repository, times(1)).findAll()
    }

    @Test
    fun testListAllWineStoresBetweenFaixaInicioAndFaixaFim() {
        val ws = WineStore("12345", 1000L, 2000L)
        `when`(repository.findBetweenFaixaInicioAndFaixaFim(anyLong(), anyLong())).thenReturn(listOf(wsDB, ws))
        val response = service.listAllWineStores(1000L, 14000L, null)
        assertEquals(2, response.size)
        verify(repository, times(1)).findBetweenFaixaInicioAndFaixaFim(1000L, 14000L)
    }

    @Test
    fun testListAllWineStoresByFaixaInicio() {
        val ws1 = WineStore("12345", 1000L, 2000L)
        val ws2 = WineStore("12345", 20000L, 21000L)
        `when`(repository.findByFaixaInicioGreaterThan(anyLong())).thenReturn(listOf(wsDB, ws1, ws2))
        val response = service.listAllWineStores(1000L, null, null)
        assertEquals(3, response.size)
        verify(repository, times(1)).findByFaixaInicioGreaterThan(1000L)
    }

    @Test
    fun testListAllWineStoresByFaixaFim() {
        val ws1 = WineStore("12345", 1000L, 2000L)
        val ws2 = WineStore("12345", 20000L, 21000L)
        `when`(repository.findByFaixaFimLessThan(anyLong())).thenReturn(listOf(wsDB, ws1, ws2))
        val response = service.listAllWineStores(null, 20000L, null)
        assertEquals(3, response.size)
        verify(repository, times(1)).findByFaixaFimLessThan(20000L)
    }

    @Test
    fun testFindById() {
        `when`(repository.findById(anyLong())).thenReturn(Optional.of(wsDB))
        validateWineStore(service.findWineStoreById(123L))
    }

    @Test
    fun testFindByIdShouldThrowBaseException() {
        `when`(repository.findById(anyLong())).thenThrow(BaseException("any exception message", HttpStatus.NOT_FOUND))
        val ex = assertThrows(BaseException::class.java, { service.findWineStoreById(123L) },
            "It was expected that findWineStoreById() thrown an exception, " +
                    "due to wine store not found"
        )
        assertEquals(HttpStatus.NOT_FOUND, ex.httpStatus)
    }

    @Test
    fun testUpdateWineStoreShouldThrowBaseException() {
        `when`(repository.findById(anyLong())).thenThrow(BaseException("any exception message", HttpStatus.NOT_FOUND))
        val ws1 = WineStore(faixaInicio = 16000L)
        val ex = assertThrows(BaseException::class.java, { service.updateWineStore(ws1, 123L) },
            "It was expected that updateWineStore() thrown an exception, " +
                    "due to wine store not found"
        )
        assertEquals(HttpStatus.NOT_FOUND, ex.httpStatus)
        verify(repository, times(0)).save(wsDB)

    }

    @Test
    fun testDeleteWineStore() {
        wsDB.id = 123L
        `when`(repository.findById(anyLong())).thenReturn(Optional.of(wsDB))
        service.deleteWineStore(123L)
        verify(repository, times(1)).deleteById(123L)
    }

    @Test
    fun testDeleteWineStoreShouldThrowBaseException() {
        `when`(repository.findById(anyLong())).thenThrow(BaseException("any exception message", HttpStatus.NOT_FOUND))
        val ex = assertThrows(BaseException::class.java, { service.deleteWineStore(123L) },
            "It was expected that deleteWineStore() thrown an exception, " +
                    "due to wine store not found"
        )
        assertEquals(HttpStatus.NOT_FOUND, ex.httpStatus)
        verify(repository, times(0)).deleteById(123L)
    }

    private fun validateWineStore(response: WineStore) {
        assertEquals(wsDB.codigoLoja, response.codigoLoja)
        assertEquals(wsDB.faixaInicio, response.faixaInicio)
        assertEquals(wsDB.faixaFim, response.faixaFim)
    }
}