package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * See [POJOs to JSON and back](https://github.com/FasterXML/jackson-databind/#1-minute-tutorial-pojos-to-json-and-back)
 *
 * @author RJ
 */
class LocalDateTimeDeserializerTest {
  @Test
  fun testDeserializer() {
    // config
    val mapper = ObjectMapper()
    val testModule = SimpleModule("MyModule", Version(1, 0, 0, null, null, null))
    testModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer.INSTANCE)
    mapper.registerModule(testModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)

    // init data
    val localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    val localDateTimeStr = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    val json = """{"localDateTime": "$localDateTimeStr"}"""

    // do deserialize
    val dto = mapper.readValue(json, Dto::class.java)

    // verify deserialize
    assertEquals(localDateTime, dto.localDateTime)
    assertNull(dto.name)
  }
}

data class Dto(
  val name: String? = null,
  val localDateTime: LocalDateTime? = null
)