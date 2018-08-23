package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.temporal.TemporalAccessor

/**
 * @author RJ
 */
class DateTimeLocalKeySerializer : JsonSerializer<TemporalAccessor>() {
  companion object {
    val INSTANCE = DateTimeLocalKeySerializer()
  }

  override fun serialize(value: TemporalAccessor?, gen: JsonGenerator?, serializers: SerializerProvider?) {
    println("key=$value")
    TODO("not implemented")
  }
}
