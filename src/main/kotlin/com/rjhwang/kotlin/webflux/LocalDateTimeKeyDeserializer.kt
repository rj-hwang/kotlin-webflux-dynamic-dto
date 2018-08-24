package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author RJ
 */
class LocalDateTimeKeyDeserializer : KeyDeserializer() {
  companion object {
    val INSTANCE = LocalDateTimeKeyDeserializer()
  }

  override fun deserializeKey(key: String, context: DeserializationContext): LocalDateTime {
    return LocalDateTime.parse(key, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  }
}