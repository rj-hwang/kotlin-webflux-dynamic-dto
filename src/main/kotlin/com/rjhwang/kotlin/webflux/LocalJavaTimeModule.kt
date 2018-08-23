package com.rjhwang.kotlin.webflux

import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.LocalDate

class LocalJavaTimeModule : SimpleModule() {
  init {
    addDeserializer(LocalDate::class.java, LocalDateDeserializer.INSTANCE)
    addKeyDeserializer(LocalDate::class.java, LocalDateKeyDeserializer.INSTANCE)
    //addKeySerializer(LocalDate::class.java, LocalDateKeySerializer.INSTANCE)

//    addDeserializer(TemporalAccessor::class.java, DateTimeLocalDeserializer.INSTANCE)
//    addSerializer(TemporalAccessor::class.java, DateTimeLocalSerializer.INSTANCE)
//
//    addKeySerializer(TemporalAccessor::class.java, DateTimeLocalKeySerializer.INSTANCE)
//    addKeyDeserializer(TemporalAccessor::class.java, DateTimeLocalKeyDeserializer.INSTANCE)
  }
}