package cn.gftaxi.webflux.dynamicdto

import com.rjhwang.kotlin.webflux.Utils.DEFAULT_DTO1
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

/**
 * @author RJ
 */
@Component
class GetDto1Handler @Autowired constructor() : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    return ok().contentType(APPLICATION_JSON_UTF8).syncBody(DEFAULT_DTO1)
  }
}