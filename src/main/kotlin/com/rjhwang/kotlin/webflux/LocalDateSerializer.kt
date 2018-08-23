package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.rjhwang.kotlin.webflux.javatime.JavaTimeUtils.getFormatter
import java.time.LocalDate

/**
 * @author RJ
 */
class LocalDateSerializer : JsonSerializer<LocalDate>() {
  companion object {
    val INSTANCE = LocalDateSerializer()
  }

  override fun serialize(value: LocalDate?, generator: JsonGenerator, provider: SerializerProvider) {
    if (value == null) generator.writeNull()
    else generator.writeString(getFormatter(LocalDate::class).format(value))
  }

  override fun handledType(): Class<LocalDate> {
    return LocalDate::class.java
  }
}