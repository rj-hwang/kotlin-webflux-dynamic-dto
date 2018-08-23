package cn.gftaxi.webflux.dynamicdto

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.rjhwang.kotlin.webflux.javatime.LocalJavaTimeModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


/**
 * @author RJ
 *
 * See spring-boot document [76.3 Customize the Jackson ObjectMapper]https://docs.spring.io/spring-boot/docs/2.0.4.RELEASE/reference/htmlsingle/#howto-customize-the-jackson-objectmapper).
 */
@Configuration
class LocalJavaTimeConfiguration {
  @Primary
  @Bean
  fun jacksonObjectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
    val objectMapper = builder.createXmlMapper(false).build<ObjectMapper>()
    objectMapper.registerModule(LocalJavaTimeModule())
    return objectMapper
  }

  @Bean
  fun customJackson(): Jackson2ObjectMapperBuilderCustomizer {
    return Jackson2ObjectMapperBuilderCustomizer {
      it.serializationInclusion(Include.NON_EMPTY)
      it.failOnUnknownProperties(true)
//      it.deserializerByType(LocalDate::class.java, JavaTimeDeserializer.INSTANCE)
//      it.deserializerByType(LocalDateTime::class.java, JavaTimeDeserializer.INSTANCE)
//      it.deserializerByType(OffsetDateTime::class.java, JavaTimeDeserializer.INSTANCE)
//      it.deserializerByType(TemporalAccessor::class.java, JavaTimeDeserializer.INSTANCE)
//      it.serializerByType(TemporalAccessor::class.java, DateTimeLocalSerializer())
      //it.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

      //it.deserializers(JavaTimeDeserializer.INSTANCE)
      //it.serializers(DateTimeLocalSerializer.INSTANCE)

      //it.serializerByType(TemporalAccessor::class.java, DateTimeLocalSerializer.INSTANCE)
      //it.serializerByType(LocalDate::class.java, DateTimeLocalSerializer.INSTANCE)
    }
  }
}