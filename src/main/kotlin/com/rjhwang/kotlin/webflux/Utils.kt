package com.rjhwang.kotlin.webflux

import com.rjhwang.kotlin.webflux.dto1.Dto1
import java.math.BigDecimal
import java.time.LocalDate

object Utils {
  const val LOCAL_DATE_PATTERN: String = "yyyy-MM-dd"
  val DEFAULT_DTO1: Dto1 = Dto1()

  init {
    DEFAULT_DTO1.name = "simter"
    DEFAULT_DTO1.decimal = BigDecimal("3.45")
    DEFAULT_DTO1.localDate = LocalDate.of(2018, 1, 10)
  }
}