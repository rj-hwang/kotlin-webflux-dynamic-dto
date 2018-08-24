package com.rjhwang.kotlin.webflux.javatime

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.UnsupportedOperationException
import java.time.*
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KClass

class JavaTimeGenericDeserializer<T : TemporalAccessor>(private val handledType: KClass<T>)
  : JsonDeserializer<T>() {
  override fun handledType(): Class<*> {
    return this.handledType.java
  }

  @Suppress("UNCHECKED_CAST")
  override fun deserialize(parser: JsonParser, context: DeserializationContext): T {
    val value = parser.text
    logger.debug("handledType={}, handledValue={}", handledType, value)
    return value2TemporalAccessor(value, handledType as KClass<TemporalAccessor>) as T
  }

  companion object {
    private val logger: Logger = LoggerFactory.getLogger(JavaTimeGenericDeserializer::class.java)

    fun value2TemporalAccessor(value: String, targetClass: KClass<TemporalAccessor>): TemporalAccessor {
      return when (targetClass) {
        LocalDateTime::class -> LocalDateTime.parse(value, JavaTimeUtils.getFormatter(targetClass))
        LocalDate::class -> LocalDate.parse(value, JavaTimeUtils.getFormatter(targetClass))
        LocalTime::class -> LocalTime.parse(value, JavaTimeUtils.getFormatter(targetClass))
        OffsetDateTime::class -> OffsetDateTime.of(
          LocalDateTime.parse(value, JavaTimeUtils.getFormatter(targetClass)),
          JavaTimeUtils.LOCAL_OFFSET
        )
        OffsetTime::class -> OffsetTime.of(
          LocalTime.parse(value, JavaTimeUtils.getFormatter(targetClass)),
          JavaTimeUtils.LOCAL_OFFSET
        )
        ZonedDateTime::class -> ZonedDateTime.of(
          LocalDateTime.parse(value, JavaTimeUtils.getFormatter(targetClass)),
          ZoneId.systemDefault()
        )
        Instant::class -> Instant.ofEpochSecond(value.toLong())
        YearMonth::class -> YearMonth.of(value.substring(0, 4).toInt(), value.substring(4).toInt())
        Year::class -> Year.of(value.toInt())
        Month::class -> Month.of(value.toInt())
        MonthDay::class -> MonthDay.parse(value, JavaTimeUtils.getFormatter(targetClass))
        else -> throw UnsupportedOperationException("handledType=$targetClass, handledValue=$value")
      }
    }

    fun addAllSupportedDeserializerToModule(module: SimpleModule) {
      module.addDeserializer(LocalDateTime::class.java, JavaTimeGenericDeserializer(LocalDateTime::class))
      module.addDeserializer(LocalDate::class.java, JavaTimeGenericDeserializer(LocalDate::class))
      module.addDeserializer(LocalTime::class.java, JavaTimeGenericDeserializer(LocalTime::class))
      module.addDeserializer(OffsetDateTime::class.java, JavaTimeGenericDeserializer(OffsetDateTime::class))
      module.addDeserializer(OffsetTime::class.java, JavaTimeGenericDeserializer(OffsetTime::class))
      module.addDeserializer(ZonedDateTime::class.java, JavaTimeGenericDeserializer(ZonedDateTime::class))

      module.addDeserializer(Instant::class.java, JavaTimeGenericDeserializer(Instant::class))
      module.addDeserializer(YearMonth::class.java, JavaTimeGenericDeserializer(YearMonth::class))
      module.addDeserializer(Year::class.java, JavaTimeGenericDeserializer(Year::class))
      module.addDeserializer(Month::class.java, JavaTimeGenericDeserializer(Month::class))
      module.addDeserializer(MonthDay::class.java, JavaTimeGenericDeserializer(MonthDay::class))
    }
  }
}