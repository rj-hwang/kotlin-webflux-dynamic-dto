package com.rjhwang.kotlin.webflux

import cn.gftaxi.webflux.dynamicdto.GetDto1Handler
import cn.gftaxi.webflux.dynamicdto.PatchDto1Handler
import com.rjhwang.kotlin.webflux.dto1.Dto1.Companion.DEFAULT
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.reactive.server.WebTestClient.bindToRouterFunction
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.PATCH
import org.springframework.web.reactive.function.server.RouterFunctions.route

/**
 * Test [org.springframework.format.annotation.DateTimeFormat]。
 *
 * See [1.11.3. Conversion, formatting](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-config-conversion)
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
  private val name = DEFAULT.name!!
  private val localDate = DEFAULT.localDate.toString()

  @Test
  fun patch() {
    val client = bindToRouterFunction(route(PATCH(path), patchHandler)).build()
    val json = """{"name": "$name", "localDate": "$localDate"}"""

    client.patch().uri(path)
      .contentType(APPLICATION_JSON_UTF8)
      .syncBody(json)
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.name").isEqualTo(name)
      .jsonPath("$.localDate").isEqualTo(localDate) // By Dto/@set:DateTimeFormat
      .jsonPath("$.code").doesNotExist()
  }

  @Test
  fun get() {
    val client = bindToRouterFunction(route(GET(path), getHandler)).build()
    client.get().uri(path)
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType(APPLICATION_JSON_UTF8)
      .expectBody()
      .jsonPath("$.name").isEqualTo(name)
      .jsonPath("$.localDate").isEqualTo(localDate) // By Dto/@get:JsonFormat
      .jsonPath("$.code").doesNotExist()
  }
}