package com.rjhwang.kotlin.webflux.javatime

import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * @author RJ
 */
class JavaTimeModule : SimpleModule() {
  init {
    JavaTimeSerializer.addAllSupportedSerializerToModule(this)
    JavaTimeDeserializer.addAllSupportedDeserializerToModule(this)
    JavaTimeKeyDeserializer.addAllSupportedKeyDeserializerToModule(this)
  }
}