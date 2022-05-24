package br.com.winestore.controller

import br.com.winestore.model.WineStore
import br.com.winestore.repository.WineStoreRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WineStoreControllerTest () {

    @Autowired
    private lateinit var repository: WineStoreRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val uri: URI = URI("/api/v1")

    private lateinit var wsDB: WineStore

    @BeforeEach
    fun init() {
        wsDB = repository.save(WineStore("12345", 13000L, 14000L))
    }

    @Test
    fun testCreateWineStore() {
        val wsToBeCreated = WineStore("123456", 15000L, 16000L)

        val response = mockMvc.perform(
            post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wsToBeCreated))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val contentAsString = response.response.contentAsString
        val wsObjectResponse = objectMapper.readValue<WineStore>(contentAsString)

        assertEquals(wsToBeCreated.codigoLoja, wsObjectResponse.codigoLoja)
        assertEquals(wsToBeCreated.faixaInicio, wsObjectResponse.faixaInicio)
        assertEquals(wsToBeCreated.faixaFim, wsObjectResponse.faixaFim)
        assertTrue(repository.findById(wsObjectResponse.id!!).isPresent)
    }

    @Test
    fun testListAllWineStores() {
        repository.save(WineStore("123456", 15000L, 16000L))

        val response = mockMvc.perform(get(uri))
            .andExpect(status().isOk)
            .andReturn()

        val contentAsString = response.response.contentAsString
        val wsObjectResponse = objectMapper.readValue<List<WineStore>>(contentAsString)

        assertEquals(2, wsObjectResponse.size)
    }

    @Test
    fun testListOneWineStoreById() {
        repository.save(WineStore("123456", 15000L, 16000L))

        val response = mockMvc.perform(get(uri.toString() + "/" + wsDB.id))
            .andExpect(status().isOk)
            .andReturn()

        val contentAsString = response.response.contentAsString
        val wsObjectResponse = objectMapper.readValue<WineStore>(contentAsString)

        assertEquals(wsDB.codigoLoja, wsObjectResponse.codigoLoja)
        assertEquals(wsDB.faixaInicio, wsObjectResponse.faixaInicio)
        assertEquals(wsDB.faixaFim, wsObjectResponse.faixaFim)    }

    @Test
    fun testUpdateWineStore() {
        val wsToBeUpdated = WineStore(codigoLoja = "99999")

        val response = mockMvc.perform(
            put(uri.toString() + "/" + wsDB.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wsToBeUpdated))
        )
            .andExpect(status().isOk)
            .andReturn()

        val contentAsString = response.response.contentAsString
        val wineStoreObjectResponse = objectMapper.readValue<WineStore>(contentAsString)

        assertEquals(wsToBeUpdated.codigoLoja, wineStoreObjectResponse.codigoLoja)
    }

    @Test
    fun testDeleteWineStore() {
        mockMvc.perform(delete(uri.toString()+ "/"+ wsDB.id))
            .andExpect(status().isOk)
            .andReturn()

        assertTrue(repository.findById(wsDB.id!!).isEmpty)
    }
}