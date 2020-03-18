package com.example.demo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
    @MockkBean
    lateinit var mockService: CustomerService

    @Test
    fun `should use default view on web`() {
        every { mockService.create() } returns Customer(
                "Emily",
                "642 Buckhannan Avenue Stratford",
                "111-111-111",
                "222-222-222",
                "emily@example.com",
                Date.from(ZonedDateTime.now().minusYears(8).toInstant())
        )

        mvc.perform(get("/customer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.phone").value(equalTo("111-111-111")))
                .andExpect(jsonPath("$.cellPhone").value(equalTo("222-222-222")))
    }
}