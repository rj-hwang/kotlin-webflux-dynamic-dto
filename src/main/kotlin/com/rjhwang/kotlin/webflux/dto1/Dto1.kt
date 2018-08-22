package com.rjhwang.kotlin.webflux.dto1

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

/**
 * 动态 DTO 例子。
 *
 * 这个类使用了 Kotlin 的 [Delegated Properties](https://kotlinlang.org/docs/reference/delegated-properties.html) 中的
 * [Storing Properties in a Map](https://kotlinlang.org/docs/reference/delegated-properties.html#storing-properties-in-a-map) 技术
 * 将有设置过值的属性以键值对的形式保存在 [map] 属性中。
 * 这个主意来源于 StackOverflow [How to do PATCH properly in strongly typed languages based on Spring](https://stackoverflow.com/questions/36907723#answer-37010895)。
 *
 * 特别注意类中的属性需要定义为非只读属性 `var` 而不是 `val`，并且全部属性需要定义为可空，且不要设置任何默认值。
 *
 * @author RJ
 */
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
class Dto1 constructor() {
  @JsonIgnore
  private val map: MutableMap<String, Any?> = mutableMapOf<String, Any?>().withDefault { null }

  constructor(name: String?, localDate: LocalDate?) : this() {
    this.name = name
    this.localDate = localDate
  }

  var name: String? by map
  var code: String? by map
  @get:JsonFormat(pattern = "yyyy-MM-dd")
  @set:DateTimeFormat(pattern = "yyyy-MM-dd")
  var localDate: LocalDate?  by map

  override fun toString(): String {
    return "Dto1=$map"
  }

  companion object {
    val DEFAULT = Dto1(
      name = "simter",
      localDate = LocalDate.of(2018, 1, 10)
    )
  }
}