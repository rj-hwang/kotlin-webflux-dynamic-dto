package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer

/**
 * @author RJ
 */
class DateTimeLocalKeyDeserializer : KeyDeserializer() {
  companion object {
    val INSTANCE = DateTimeLocalKeyDeserializer()
  }

  override fun deserializeKey(key: String, context: DeserializationContext): Any {
    println("key=$key,c=${context::class}")
    TODO("not implemented")
  }
}
