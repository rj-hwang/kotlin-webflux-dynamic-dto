package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jayway.jsonpath.matchers.JsonPathMatchers.*
import com.rjhwang.kotlin.webflux.javatime.JavaTimeDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeKeyDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeSerializer
import com.rjhwang.kotlin.webflux.javatime.LocalJavaTimeModule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor

/**
 * See [POJOs to JSON and back](https://github.com/FasterXML/jackson-databind/#1-minute-tutorial-pojos-to-json-and-back)
 *
 * @author RJ
 */
class NativeTest {
  private val logger: Logger = LoggerFactory.getLogger(NativeTest::class.java)
  val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
  val nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

  @Test
  fun testJavaTimeSerializer() {
    // config
    val mapper = ObjectMapper()
    val testModule = SimpleModule("MyModule", Version(1, 0, 0, null, null, null))
    testModule.addSerializer(JavaTimeSerializer.INSTANCE)
    mapper.registerModule(testModule)
    mapper.setSerializationInclusion(NON_EMPTY) // not serialize null and empty value

    // init data
    val dto = DtoA(
      name = "",
      localDate = now.toLocalDate(),
      localDateTime = now.toLocalDateTime(),
      offsetDateTime = now
    )

    // do serialize
    val json = mapper.writeValueAsString(dto)
    logger.debug("json={}", json)

    // verify serialize
    assertThat(json, isJson(allOf(
      withoutJsonPath("$.name"),
      withJsonPath("$.localDate", equalTo(nowStr.substring(0, 10))),
      withJsonPath("$.localDateTime", equalTo(nowStr.substring(0, 16))),
      withJsonPath("$.offsetDateTime", equalTo(nowStr.substring(0, 16)))
    )))
  }

  @Test
  fun testJacksonJavaTimeModule() {
    // config
    val mapper = ObjectMapper()
    mapper.registerModule(JavaTimeModule())
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.setSerializationInclusion(NON_EMPTY) // not serialize null and empty value

    // init data
    val expected = DtoA(
      name = "",
      localDate = now.toLocalDate(),
      localDateTime = now.toLocalDateTime(),
      offsetDateTime = now
    )

    // do serialize
    val json = mapper.writeValueAsString(expected)
    logger.debug("json={}", json)

    // verify serialize
    val nowIsoStr = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    assertThat(json, isJson(allOf(
      withoutJsonPath("$.name"),
      withJsonPath("$.localDate", equalTo(nowIsoStr.substring(0, 10))),
      withJsonPath("$.localDateTime", equalTo(nowIsoStr.substring(0, 19))),
      withJsonPath("$.offsetDateTime", equalTo(nowIsoStr))
    )))

    // do deserialize
    val actual = mapper.readValue(json, DtoA::class.java)

    // verify deserialize
    assertEquals(DtoA(
      name = null,
      localDate = now.toLocalDate(),
      localDateTime = now.toLocalDateTime(),
      offsetDateTime = now
    ), actual)
  }

  @Test
  fun testLocalJavaTimeModule() {
    // config
    val mapper = ObjectMapper()
    mapper.registerModule(LocalJavaTimeModule())
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.setSerializationInclusion(NON_EMPTY) // not serialize null and empty value

    // init data
    val expected = DtoA(
      name = "",
      localDate = now.toLocalDate(),
      localDateTime = now.toLocalDateTime(),
      offsetDateTime = now
    )

    // do serialize
    val json = mapper.writeValueAsString(expected)
    logger.debug("json={}", json)

    // verify serialize
    assertThat(json, isJson(allOf(
      withoutJsonPath("$.name"),
      withJsonPath("$.localDate", equalTo(nowStr.substring(0, 10))),
      withJsonPath("$.localDateTime", equalTo(nowStr.substring(0, 16))),
      withJsonPath("$.offsetDateTime", equalTo(nowStr.substring(0, 16)))
    )))

    // do deserialize
    val actual = mapper.readValue(json, DtoA::class.java)

    // verify deserialize
    assertEquals(DtoA(
      name = null,
      localDate = now.toLocalDate(),
      localDateTime = now.toLocalDateTime(),
      offsetDateTime = now
    ), actual)
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