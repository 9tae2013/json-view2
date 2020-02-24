package com.example.demo

import com.example.demo.Views.Secret
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.*


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Configuration
class SecretWriterConfig {
    @Bean
    fun secretWriter(mapper: ObjectMapper): ObjectWriter {
        return mapper.writerWithView(Views.Secret::class.java)
    }
}

@RestController
class CustomerController(val writer: ObjectWriter) {

    @GetMapping("/customer")
    fun customer(): Customer {
        val customer = Customer(
                "Emily",
                "642 Buckhannan Avenue Stratford",
                "111-111-111",
                "222-222-222",
                "emily@example.com",
                Date.from(ZonedDateTime.now().minusYears(8).toInstant())
        )
        log.info("Get customer {}", writer.writeValueAsString(customer))
        return customer;
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CustomerController::class.java)
    }
}

sealed class Views {
    sealed class Secret
}

class SecretJsonSerializer : JsonSerializer<String>() {
    override fun serialize(value: String, gen: JsonGenerator, provider: SerializerProvider) {
        if (provider.activeView == Views.Secret::class.java) {
            val encryptedValue = "secret{${value}}"
            gen.writeString(encryptedValue)
        } else {
            gen.writeString(value)
        }
    }
}

class Customer(
        var name: String,
        var address: String,
        @JsonView(Secret::class)
        @JsonSerialize(using = SecretJsonSerializer::class)
        var phone: String,
        @JsonView(Secret::class)
        @JsonSerialize(using = SecretJsonSerializer::class)
        var cellPhone: String,
        var emailAddress: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "America/Chicago")
        var customerSince: Date
)

