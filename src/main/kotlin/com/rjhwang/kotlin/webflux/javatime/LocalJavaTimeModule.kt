package com.rjhwang.kotlin.webflux.javatime

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.deser.ValueInstantiator
import com.fasterxml.jackson.databind.deser.ValueInstantiators
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.temporal.TemporalAccessor

class LocalJavaTimeModule : SimpleModule() {
  init {
    addDeserializer(TemporalAccessor::class.java, JavaTimeDeserializer.INSTANCE)

    addSerializer(TemporalAccessor::class.java, JavaTimeSerializer.INSTANCE)

    //addKeySerializer(TemporalAccessor::class.java, JavaTimeKeySerializer.INSTANCE)

    addKeyDeserializer(TemporalAccessor::class.java, JavaTimeKeyDeserializer.INSTANCE)
  }

  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addValueInstantiators(object : ValueInstantiators.Base() {
      override fun findValueInstantiator(config: DeserializationConfig?, beanDesc: BeanDescription?,
                                         defaultInstantiator: ValueInstantiator): ValueInstantiator {
        return defaultInstantiator
      }
    })
  }
}