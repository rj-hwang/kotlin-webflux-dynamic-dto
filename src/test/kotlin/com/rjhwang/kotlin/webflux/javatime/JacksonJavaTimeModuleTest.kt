package com.rjhwang.kotlin.webflux.javatime

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jayway.jsonpath.matchers.JsonPathMatchers.*
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

/**
 * See [POJOs to JSON and back](https://github.com/FasterXML/jackson-databind/#1-minute-tutorial-pojos-to-json-and-back)
 *
 * @author RJ
 */
class JacksonJavaTimeModuleTest {
  private val logger: Logger = LoggerFactory.getLogger(JacksonJavaTimeModuleTest::class.java)
  private val now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)!!
  private val nowStr = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)!!

  @Test
  fun test() {
    // config
    val mapper = ObjectMapper()
    mapper.registerModule(JavaTimeModule())
    mapper.setSerializationInclusion(NON_EMPTY) // not serialize null and empty value
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)

    // init data
    val expected = Dto(
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
      withJsonPath("$.localDateTime", equalTo(nowStr.substring(0, 19))),
      withJsonPath("$.offsetDateTime", equalTo(nowStr))
    )))

    // do deserialize
    val actual = mapper.readValue(json, Dto::class.java)

    // verify deserialize
    assertEquals(Dto(
      name = null,
      localDate = now.toLocalDate(),
      localDateTime = now.toLocalDateTime(),
      offsetDateTime = now
    ), actual)
  }

  data class Dto(
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
}