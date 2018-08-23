package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import com.rjhwang.kotlin.webflux.DateTimeLocalFormatterUtils.getFormatter
import java.time.Instant
import java.time.Month
import java.time.Year
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KClass

/**
 * @author RJ
 */
class DateTimeLocalSerializer : ContextualSerializer, JsonSerializer<TemporalAccessor>() {
  companion object {
    val INSTANCE = DateTimeLocalSerializer()
  }

  private lateinit var pattern: String
  private lateinit var targetClass: KClass<TemporalAccessor>
  override fun createContextual(provider: SerializerProvider, property: BeanProperty): JsonSerializer<*> {
    pattern = property.getAnnotation(JsonFormat::class.java)?.pattern ?: ""
    targetClass = property.type.rawClass.kotlin as KClass<TemporalAccessor>
    return this
  }

  override fun handledType(): Class<TemporalAccessor> {
    return TemporalAccessor::class.java
  }

  override fun serialize(value: TemporalAccessor?, generator: JsonGenerator, provider: SerializerProvider) {
    if (value == null) generator.writeNull()
    when (targetClass) {
      Year::class -> generator.writeNumber((value as Year).value)
      Month::class -> generator.writeNumber((value as Month).value)
      Instant::class -> generator.writeNumber((value as Instant).epochSecond)
      else -> generator.writeString(getFormatter(pattern, targetClass).format(value))
    }
  }
}