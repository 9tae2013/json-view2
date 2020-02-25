package com.example.demo

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.ZonedDateTime
import java.util.*


@WebMvcTest
class CustomerControllerTest {
    @Autowired
    lateinit var mvc: MockMvc
    @MockBean
    lateinit var mockService: CustomerService

    @Test
    fun `should use default view on web`() {
        val customer = Customer(
                "Emily",
                "642 Buckhannan Avenue Stratford",
                "111-111-111",
                "222-222-222",
                "emily@example.com",
                Date.from(ZonedDateTime.now().minusYears(8).toInstant())
        )
        doReturn(customer).`when`(mockService).create()

        mvc.perform(get("/customer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.phone").value(equalTo("111-111-111")))
                .andExpect(jsonPath("$.cellPhone").value(equalTo("222-222-222")))
    }
}