package com.rjhwang.kotlin.webflux

import cn.gftaxi.webflux.dynamicdto.GetDto1Handler
import cn.gftaxi.webflux.dynamicdto.PatchDto1Handler
import com.rjhwang.kotlin.webflux.Utils.DEFAULT_DTO1
import com.rjhwang.kotlin.webflux.Utils.LOCAL_DATE_PATTERN
import com.rjhwang.kotlin.webflux.dto1.Dto1
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient.bindToRouterFunction
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.PATCH
import org.springframework.web.reactive.function.server.RouterFunctions.route
import java.time.format.DateTimeFormatter

/**
 * Test [Dto1].
 *
 * See [1.11.3. Conversion, formatting](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-config-conversion).
 *
 * @author RJ
 */
@SpringJUnitConfig(PatchDto1Handler::class, GetDto1Handler::class)
@EnableWebFlux
class Dto1Test @Autowired constructor(
  private val patchHandler: PatchDto1Handler,
  private val getHandler: GetDto1Handler
) {
  private val path = "/"
  private val localDate = DEFAULT_DTO1.localDate!!.format(DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN))

  @Test
  fun patch() {
    val client = bindToRouterFunction(route(PATCH(path), patchHandler)).build()
    val json = """{
        "name": "${DEFAULT_DTO1.name}",
        "decimal": ${DEFAULT_DTO1.decimal},
        "localDate": "$localDate"
      }""".trimIndent()

    client.patch().uri(path)
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(json)
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.notSet").doesNotExist()
      .jsonPath("$.name").isEqualTo(DEFAULT_DTO1.name!!)
      .jsonPath("$.decimal").isEqualTo(DEFAULT_DTO1.decimal!!)
      // By Dto/@set:DateTimeFormat
      .jsonPath("$.localDate").isEqualTo(localDate)
  }

  @Test
  fun get() {
    val client = bindToRouterFunction(route(GET(path), getHandler)).build()
    client.get().uri(path)
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.notSet").doesNotExist()
      .jsonPath("$.name").isEqualTo(DEFAULT_DTO1.name!!)
      .jsonPath("$.decimal").isEqualTo(DEFAULT_DTO1.decimal!!)
      // By Dto/@get:JsonFormat
      .jsonPath("$.localDate").isEqualTo(localDate)
  }
}