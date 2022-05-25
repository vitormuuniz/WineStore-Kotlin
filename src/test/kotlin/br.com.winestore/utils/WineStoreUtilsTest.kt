package br.com.winestore.utils

import br.com.winestore.exception.BaseException
import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WineStoreUtilsTest {
    @Autowired
    private lateinit var repository: WineStoreRepository

    private lateinit var to: WineStore

    @BeforeEach
    fun init() {
        to = WineStore("12345", 13000L, 14000L)
    }

    @Test
    fun testValidateFields() {
        assertDoesNotThrow { WineStoreUtils.validateFields(to) }
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToCodigoLojaNull() {
        to.codigoLoja = null
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFields(to) },
            "It was expected that validateFields() thrown an exception, " +
                    "due to codigoLoja null"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("codigoLoja must be non null and not blank", ex.messageError)
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToCodigoLojaEmpty() {
        to.codigoLoja = ""
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFields(to) },
            "It was expected that validateFields() thrown an exception, " +
                    "due to codigoLoja empty"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("codigoLoja must be non null and not blank", ex.messageError)
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToFaixaInicioNull() {
        to.faixaInicio = null
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFields(to) },
            "It was expected that validateFields() thrown an exception, " +
                    "due to faixaInicio null"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.messageError)
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToFaixaFimNull() {
        to.faixaFim = null
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFields(to) },
            "It was expected that validateFields() thrown an exception, " +
                    "due to faixaFim null"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.messageError)
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToFaixaInicioEqualsZero() {
        to.faixaInicio = 0L
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFields(to) },
            "It was expected that validateFields() thrown an exception, " +
                    "due to faixaInicio equals zero"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.messageError)
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToFaixaFimEqualsZero() {
        to.faixaFim = 0L
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFields(to) },
            "It was expected that validateFields() thrown an exception, " +
                    "due to faixaFim equals zero"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("faixaInicio and faixaFim must be non null and greater than zero", ex.messageError)
    }

    @Test
    fun testValidateFieldsRange() {
        assertDoesNotThrow { WineStoreUtils.validateFieldsRange(to, repository) }
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToFaixaInicioGreaterThanFaixaFim() {
        to.faixaFim = to.faixaInicio!! - 1000L
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFieldsRange(to, repository) },
            "It was expected that validateFieldsRange() thrown an exception, " +
                    "due to faixaInicio greater than faixaFim"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("faixaFim must be greater than faixaInicio", ex.messageError)
    }

    @Test
    fun testValidateFieldsThrowExceptionDueToZipRangeConflitBetween() {
        repository.save(WineStore("9999", to.faixaInicio!! + 500L, to.faixaFim!! - 500L))

        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFieldsRange(to, repository) },
            "It was expected that validateFieldsRange() thrown an exception, " +
                    "due to zip range conflict"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("There is a zip range conflict, verify your data", ex.messageError)
    }

    @Test
    fun testValidateFieldsThrowExceptionDueToZipRangeConflitAround() {
        repository.save(WineStore("9999", to.faixaInicio!! - 500L, to.faixaFim!! + 500L))

        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.validateFieldsRange(to, repository) },
            "It was expected that validateFieldsRange() thrown an exception, " +
                    "due to zip range conflict"
        )
        assertEquals(HttpStatus.BAD_REQUEST, ex.httpStatus)
        assertEquals("There is a zip range conflict, verify your data", ex.messageError)
    }

    @Test
    fun testGetIfExists() {
        to = repository.save(to)
        assertDoesNotThrow { WineStoreUtils.getIfExists(to.id!!, repository) }
    }

    @Test
    fun testValidateFieldsShouldThrowBaseExceptionDueToNonexistentWineStore() {
        val id = Long.MAX_VALUE
        val ex = assertThrows(
            BaseException::class.java, { WineStoreUtils.getIfExists(id, repository) },
            "It was expected that getIfExists() thrown an exception, " +
                    "due to nonexistent wine store"
        )
        assertEquals(HttpStatus.NOT_FOUND, ex.httpStatus)
        assertEquals("There isn't a wine store with id = $id", ex.messageError)
    }
}