package com.rjhwang.kotlin.webflux.javatime

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeUtils.LOCAL_OFFSET
import com.rjhwang.kotlin.webflux.javatime.JavaTimeUtils.getFormatter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.UnsupportedOperationException
import java.time.*
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KClass

/**
 * @author RJ
 */
class JavaTimeDeserializer private constructor() : ContextualDeserializer, JsonDeserializer<TemporalAccessor>() {
  constructor(handledType: KClass<TemporalAccessor>) : this() {
    this.handledType = handledType
  }

  private var handledType: KClass<TemporalAccessor> = TemporalAccessor::class

  override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
    @Suppress("UNCHECKED_CAST")
    val handledType = property.type.rawClass.kotlin as KClass<TemporalAccessor>
    return getDeserializer(handledType = handledType)
  }

  override fun handledType(): Class<*> {
    return handledType.java
  }

  override fun deserialize(parser: JsonParser, context: DeserializationContext): TemporalAccessor {
    val value = parser.text
    logger.debug("handledType={}, handledValue={}", handledType, value)
    return value2TemporalAccessor(value, handledType)
  }

  companion object {
    private val logger: Logger = LoggerFactory.getLogger(JavaTimeDeserializer::class.java)
    val INSTANCE = JavaTimeDeserializer()
    private val CACHE_DESERIALIZERS = mutableMapOf<KClass<TemporalAccessor>, JavaTimeDeserializer>()

    fun getDeserializer(handledType: KClass<TemporalAccessor>): JavaTimeDeserializer {
      if (!CACHE_DESERIALIZERS.containsKey(handledType))
        CACHE_DESERIALIZERS[handledType] = JavaTimeDeserializer(handledType)
      return CACHE_DESERIALIZERS[handledType]!!
    }

    fun value2TemporalAccessor(value: String, targetClass: KClass<TemporalAccessor>): TemporalAccessor {
      return when (targetClass) {
        LocalDateTime::class -> LocalDateTime.parse(value, getFormatter(targetClass))
        LocalDate::class -> LocalDate.parse(value, getFormatter(targetClass))
        LocalTime::class -> LocalTime.parse(value, getFormatter(targetClass))
        OffsetDateTime::class -> OffsetDateTime.of(
          LocalDateTime.parse(value, getFormatter(targetClass)),
          LOCAL_OFFSET
        )
        OffsetTime::class -> OffsetTime.of(
          LocalTime.parse(value, getFormatter(targetClass)),
          LOCAL_OFFSET
        )
        ZonedDateTime::class -> ZonedDateTime.of(
          LocalDateTime.parse(value, getFormatter(targetClass)),
          ZoneId.systemDefault()
        )
        Month::class -> Month.of(value.toInt())
        Year::class -> Year.of(value.toInt())
        Instant::class -> Instant.ofEpochSecond(value.toLong())
        else -> throw UnsupportedOperationException("handledType=$targetClass, handledValue=$value")
      }
    }

    fun instance(kClass: KClass<LocalDateTime>): JsonDeserializer<out LocalDateTime> {
      TODO()
    }
  }
}