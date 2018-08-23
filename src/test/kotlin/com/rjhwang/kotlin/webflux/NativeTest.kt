package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.rjhwang.kotlin.webflux.javatime.JavaTimeDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeKeyDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeSerializer
import com.rjhwang.kotlin.webflux.javatime.LocalJavaTimeModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.*
import java.time.temporal.TemporalAccessor

/**
 * See [POJOs to JSON and back](https://github.com/FasterXML/jackson-databind/#1-minute-tutorial-pojos-to-json-and-back)
 *
 * @author RJ
 */
class NativeTest {
  @Test
  fun testJavaTimeSerializer() {
    val mapper = ObjectMapper()
    val testModule = SimpleModule("MyModule", Version(1, 0, 0, null, null, null))
    testModule.addSerializer(JavaTimeSerializer.INSTANCE)
    mapper.setSerializationInclusion(NON_EMPTY)
    mapper.registerModule(testModule)

    val dto = DtoA(
      name = "",
      localDate = LocalDate.now(),
      localDateTime = LocalDateTime.now(),
      offsetDateTime = OffsetDateTime.now()
    )
    println(mapper.writeValueAsString(dto))
  }

  @Test
  fun testJavaTimeDeserializer() {
    val mapper = ObjectMapper()
    val testModule = SimpleModule("MyModule", Version(1, 0, 0, null, null, null))
    testModule.addSerializer(JavaTimeSerializer.INSTANCE)

    testModule.addDeserializer(TemporalAccessor::class.java, JavaTimeDeserializer.INSTANCE)
    testModule.addKeyDeserializer(LocalDate::class.java, JavaTimeKeyDeserializer.INSTANCE)

    //testModule.addDeserializer(TemporalAccessor::class.java, JavaTimeDeserializer.INSTANCE)
    //testModule.addKeyDeserializer(TemporalAccessor::class.java, JavaTimeKeyDeserializer.INSTANCE)

    mapper.setSerializationInclusion(NON_EMPTY)
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.registerModule(testModule)

    val expected = DtoA(
      name = "",
      localDate = LocalDate.now(),
      localDateTime = LocalDateTime.now(),
      offsetDateTime = OffsetDateTime.now()
    )
    val json = mapper.writeValueAsString(expected)

    val actual = mapper.readValue(json, DtoA::class.java)
    assertEquals(expected, actual)
  }

  @Test
  fun testLocalJavaTimeModule() {
    val mapper = ObjectMapper()
    mapper.setSerializationInclusion(NON_EMPTY)
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.registerModule(LocalJavaTimeModule())

    val expected = DtoA(
      name = "",
      localDate = LocalDate.now(),
      localDateTime = LocalDateTime.now(),
      offsetDateTime = OffsetDateTime.now()
    )
    val json = mapper.writeValueAsString(expected)

    val actual = mapper.readValue(json, DtoA::class.java)
    assertEquals(expected, actual)
  }

  @Test
  fun testJavaTimeModule() {
    val mapper = ObjectMapper()
    mapper.setSerializationInclusion(NON_EMPTY)
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.registerModule(JavaTimeModule())

    val expected = DtoA(
      name = "",
      localDate = LocalDate.now(),
      localDateTime = LocalDateTime.now(),
      offsetDateTime = OffsetDateTime.now()
    )
    val json = mapper.writeValueAsString(expected)

    val actual = mapper.readValue(json, DtoA::class.java)
    assertEquals(expected, actual)
  }
}

data class DtoA(
  val id: Int? = null,
  val name: String? = null,
  val Instant: Instant? = null,
  val Year: Year? = null,
  val Month: Month? = null,
  val YearMonth: YearMonth? = null,
  val localDate: LocalDate? = null,
  val localDateTime: LocalDateTime? = null,
  val offsetDateTime: OffsetDateTime? = null
)