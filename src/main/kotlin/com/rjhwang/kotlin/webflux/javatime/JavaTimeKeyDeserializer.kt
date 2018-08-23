package com.rjhwang.kotlin.webflux.javatime

import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer
import com.rjhwang.kotlin.webflux.javatime.JavaTimeDeserializer.Companion.value2TemporalAccessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.temporal.TemporalAccessor
import kotlin.reflect.KClass

/**
 * @author RJ
 */
class JavaTimeKeyDeserializer private constructor() : ContextualKeyDeserializer, KeyDeserializer() {
  constructor(handledType: KClass<TemporalAccessor>) : this() {
    this.handledType = handledType
  }

  private var handledType: KClass<TemporalAccessor> = TemporalAccessor::class

  override fun createContextual(context: DeserializationContext, property: BeanProperty): KeyDeserializer {
    @Suppress("UNCHECKED_CAST")
    val handledType = property.type.rawClass.kotlin as KClass<TemporalAccessor>
    return getKeyDeserializer(handledType = handledType)
  }

  override fun deserializeKey(key: String, context: DeserializationContext): TemporalAccessor {
    logger.debug("handledType={}, handledValue={}", handledType, key)
    return value2TemporalAccessor(key, handledType)
  }

  companion object {
    private val logger: Logger = LoggerFactory.getLogger(JavaTimeKeyDeserializer::class.java)
    val INSTANCE = JavaTimeKeyDeserializer()
    private val CACHE_DESERIALIZERS = mutableMapOf<KClass<TemporalAccessor>, JavaTimeKeyDeserializer>()

    fun getKeyDeserializer(handledType: KClass<TemporalAccessor>): JavaTimeKeyDeserializer {
      if (!CACHE_DESERIALIZERS.containsKey(handledType))
        CACHE_DESERIALIZERS[handledType] = JavaTimeKeyDeserializer(handledType)
      return CACHE_DESERIALIZERS[handledType]!!
    }
  }
}