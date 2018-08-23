package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.rjhwang.kotlin.webflux.DateTimeLocalFormatterUtils.LOCAL_OFFSET
import com.rjhwang.kotlin.webflux.DateTimeLocalFormatterUtils.getFormatter
import java.lang.UnsupportedOperationException
import java.time.*
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KClass

/**
 * @author RJ
 */
class DateTimeLocalDeserializer : ContextualDeserializer, JsonDeserializer<TemporalAccessor>() {
  private lateinit var pattern: String
  private lateinit var targetClass: KClass<TemporalAccessor>

  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
    pattern = property.getAnnotation(JsonFormat::class.java)?.pattern ?: ""
    targetClass = property.type.rawClass.kotlin as KClass<TemporalAccessor>
    return this
  }

  override fun deserialize(parser: JsonParser, context: DeserializationContext): TemporalAccessor {
    val value = parser.text
    return when (targetClass) {
      LocalDateTime::class -> LocalDateTime.parse(value, getFormatter(pattern, targetClass))
      LocalDate::class -> LocalDate.parse(value, getFormatter(pattern, targetClass))
      LocalTime::class -> LocalTime.parse(value, getFormatter(pattern, targetClass))
      OffsetDateTime::class -> OffsetDateTime.of(
        LocalDateTime.parse(value, getFormatter(pattern, targetClass)),
        LOCAL_OFFSET
      )
      OffsetTime::class -> OffsetTime.of(
        LocalTime.parse(value, getFormatter(pattern, targetClass)),
        LOCAL_OFFSET
      )
      ZonedDateTime::class -> ZonedDateTime.of(
        LocalDateTime.parse(value, getFormatter(pattern, targetClass)),
        ZoneId.systemDefault()
      )
      Month::class -> Month.of(value.toInt())
      Year::class -> Year.of(value.toInt())
      Instant::class -> Instant.ofEpochSecond(value.toLong())
      else -> throw UnsupportedOperationException("propertyType=$targetClass, value=$value, pattern=$pattern")
    }
  }
}
