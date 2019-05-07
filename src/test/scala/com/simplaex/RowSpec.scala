package com.simplaex

import java.util.UUID

import org.scalatest.{FreeSpec, Matchers}

class RowSpec extends FreeSpec with Matchers {

  "Row.applyUnsafe" - {
    "should correctly parse valid string" in {
      val s =
        "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817"
      val target = Row.applyUnsafe(s)

      val expected = Row(
        userId = UUID.fromString("0977dca4-9906-3171-bcec-87ec0df9d745"),
        data = "kFFzW4O8gXURgP8ShsZ0gcnNT5E=",
        a = 0.18715484122922377,
        b = BigInt(982761284),
        c = BigInt(8442009284719321817L)
      )

      target shouldEqual expected
    }
  }

}
