package com.example.demo

import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class SecretJsonSerializerTest {
    @Autowired(required = false)
    lateinit var json: JacksonTester<TestData>

    @Test
    fun `test default view`() {
        val date = TestData("Emily", "111-111-111")
        assertThat(json.write(date))
                .extractingJsonPathStringValue("@.phone")
                .doesNotStartWith("secret")
    }

    @Test
    fun `test secret view`() {
        val date = TestData("Emily", "111-111-111")
        assertThat(json.forView(Views.Secret::class.java).write(date))
                .extractingJsonPathStringValue("@.phone")
                .startsWith("secret")
    }
}

class TestData(
        var name: String,
        @JsonView(Views.Secret::class)
        @JsonSerialize(using = SecretJsonSerializer::class)
        var phone: String
)