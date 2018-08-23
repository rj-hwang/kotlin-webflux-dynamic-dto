package com.rjhwang.kotlin.webflux.dto1

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.rjhwang.kotlin.webflux.DateTimeLocalDeserializer
import com.rjhwang.kotlin.webflux.DateTimeLocalSerializer
import com.rjhwang.kotlin.webflux.Utils.LOCAL_DATE_PATTERN
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

/**
 * Dynamic DTO example 1.
 *
 * Use Kotlin ['Delegated Properties'](https://kotlinlang.org/docs/reference/delegated-properties.html) and
 * ['Storing Properties in a Map'](https://kotlinlang.org/docs/reference/delegated-properties.html#storing-properties-in-a-map)
 * to store the changed properties in a [map].
 *
 * This idea comes from StackOverflow ['How to do PATCH properly in strongly typed languages based on Spring'](https://stackoverflow.com/questions/36907723#answer-37010895).
 *
 * > Notes: All properties need to be defined as `var` not `val`, and property is nullable and no default value setting.
 *
 * @author RJ
 */
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
class Dto1 {
  @JsonIgnore
  private val map: MutableMap<String, Any?> = mutableMapOf<String, Any?>().withDefault { null }

  var notSet: String? by map
  var name: String? by map
  var decimal: BigDecimal? by map
  @get:JsonFormat(pattern = LOCAL_DATE_PATTERN)
  @set:DateTimeFormat(pattern = LOCAL_DATE_PATTERN)
  var localDate: LocalDate?  by map
  //@get:JsonFormat(pattern = OFFSET_DATE_TIME_PATTERN)
  //@set:DateTimeFormat(pattern = OFFSET_DATE_TIME_PATTERN)
  @set:JsonSerialize(using = DateTimeLocalSerializer::class)
  @set:JsonDeserialize(using = DateTimeLocalDeserializer::class)
  var offsetDateTime: OffsetDateTime?  by map

  override fun toString(): String {
    return "Dto1=$map"
  }
}