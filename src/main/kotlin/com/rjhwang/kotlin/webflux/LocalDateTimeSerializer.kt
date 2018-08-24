package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author RJ
 */
class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
  companion object {
    val INSTANCE = LocalDateTimeSerializer()
  }

  override fun serialize(value: LocalDateTime?, generator: JsonGenerator, provider: SerializerProvider) {
    if (value == null) generator.writeNull()
    else generator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(value))
  }

  override fun handledType(): Class<LocalDateTime> {
    return LocalDateTime::class.java
  }
}