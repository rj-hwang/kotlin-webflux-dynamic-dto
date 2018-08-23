package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.rjhwang.kotlin.webflux.DateTimeLocalFormatterUtils.getFormatter
import java.time.LocalDate

/**
 * @author RJ
 */
class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
  companion object {
    val INSTANCE = LocalDateDeserializer()
  }

  override fun handledType(): Class<*> {
    return LocalDate::class.java
  }

  override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDate {
    return LocalDate.parse(parser.text, getFormatter("", LocalDate::class))
  }
}