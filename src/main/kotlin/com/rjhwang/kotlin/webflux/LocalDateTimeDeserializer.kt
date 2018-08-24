package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author RJ
 */
class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
  companion object {
    val INSTANCE = LocalDateTimeDeserializer()
  }

  override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime {
    return LocalDateTime.parse(parser.text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  }
}