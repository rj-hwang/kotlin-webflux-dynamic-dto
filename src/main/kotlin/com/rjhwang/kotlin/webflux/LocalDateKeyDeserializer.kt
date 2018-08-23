package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeUtils.getFormatter
import java.time.LocalDate

/**
 * @author RJ
 */
class LocalDateKeyDeserializer : KeyDeserializer() {
  companion object {
    val INSTANCE = LocalDateKeyDeserializer()
  }

  override fun deserializeKey(key: String, context: DeserializationContext): LocalDate {
    return LocalDate.parse(key, getFormatter(LocalDate::class))
  }
}