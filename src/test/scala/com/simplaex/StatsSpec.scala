package com.simplaex

import java.util.UUID

import fs2._
import org.scalatest.{FreeSpec, Matchers}

import scala.collection.immutable.ListMap

class StatsSpec extends FreeSpec with Matchers {

  "Stats.toString" - {
    "should format per specs" in {
      val target = Stats(
        sumOfC = BigInt("30212888860247708058"),
        byUser = ListMap( // to guarantee order of entries
          UUID.fromString("0977dca4-9906-3171-bcec-87ec0df9d745") -> UserStats(
            avgOfA = 0.6794981485066369,
            latestB = BigInt(1851028776)
          ),
          UUID.fromString("5fac6dc8-ea26-3762-8575-f279fe5e5f51") -> UserStats(
            avgOfA = 0.7626710614484215,
            latestB = BigInt(1005421520)
          ),
          UUID.fromString("4d968baa-fe56-3ba0-b142-be9f457c9ff4") -> UserStats(
            avgOfA = 0.6532229483547558,
            latestB = BigInt(237475359)
          )
        )
      )

      val expected =
        """|30212888860247708058
           |3
           |0977dca4-9906-3171-bcec-87ec0df9d745,0.6794981485066369,1851028776
           |5fac6dc8-ea26-3762-8575-f279fe5e5f51,0.7626710614484215,1005421520
           |4d968baa-fe56-3ba0-b142-be9f457c9ff4,0.6532229483547558,237475359""".stripMargin

      target.toString shouldEqual expected
    }
  }

  "Stats.compute" - {
    "should correctly compute stats" in {
      val strings = Vector(
        "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817",
        "5fac6dc8-ea26-3762-8575-f279fe5e5f51,cBKFTwsXHjwypiPkaq3xTr8UoRE=,0.7626710614484215,1005421520,6642446482729493998",
        "0977dca4-9906-3171-bcec-87ec0df9d745,9ZWcYIblJ7ebN5gATdzzi4e8K7Q=,0.9655429720343038,237475359,3923415930816731861",
        "4d968baa-fe56-3ba0-b142-be9f457c9ff4,RnJNTKLYpcUqhjOey+wEIGHC7aw=,0.6532229483547558,1403876285,4756900066502959030",
        "0977dca4-9906-3171-bcec-87ec0df9d745,N0fiZEPBjr3bEHn+AHnpy7I1RWo=,0.8857966322563835,1851028776,6448117095479201352",
        "0977dca4-9906-3171-bcec-87ec0df9d745,P/wNtfFfa8jIn0OyeiS1tFvpORc=,0.8851653165728414,1163597258,8294506528003481004",
        "0977dca4-9906-3171-bcec-87ec0df9d745,Aizem/PgVMKsulLGquCAsLj674U=,0.5869654624020274,1012454779,2450005343631151248",
        "023316ec-c4a6-3e88-a2f3-1ad398172ada,TRQb8nSQEZOA5Ccx8NntYuqDPOw=,0.3790267017026414,652953292,4677453911100967584",
        "023316ec-c4a6-3e88-a2f3-1ad398172ada,UfL8VetarqYZparwV4AJtyXGgFM=,0.26029423666931595,1579431460,5620969177909661735",
        "0977dca4-9906-3171-bcec-87ec0df9d745,uZNIcWQtwst+9mjQgPkV2rvm7QY=,0.039107542861771316,280709214,4450245425875000740"
      )

      val rows = strings.map(Row.applyUnsafe)

      val target = Stats.compute(5)(Stream.apply(rows: _*)).toVector

      val expected = Vector(
        Stats(
          sumOfC = BigInt("30212888860247708058"),
          byUser = Map(
            UUID.fromString("0977dca4-9906-3171-bcec-87ec0df9d745") -> UserStats(
              avgOfA = 0.6794981485066369,
              latestB = BigInt(1851028776)
            ),
            UUID.fromString("5fac6dc8-ea26-3762-8575-f279fe5e5f51") -> UserStats(
              avgOfA = 0.7626710614484215,
              latestB = BigInt(1005421520)
            ),
            UUID.fromString("4d968baa-fe56-3ba0-b142-be9f457c9ff4") -> UserStats(
              avgOfA = 0.6532229483547558,
              latestB = BigInt(1403876285)
            )
          )
        ),
        Stats(
          sumOfC = BigInt("25493180386520262311"),
          byUser = Map(
            UUID.fromString("0977dca4-9906-3171-bcec-87ec0df9d745") -> UserStats(
              avgOfA = 0.50374610727888,
              latestB = BigInt(280709214)
            ),
            UUID.fromString("023316ec-c4a6-3e88-a2f3-1ad398172ada") -> UserStats(
              avgOfA = 0.3196604691859787,
              latestB = BigInt(1579431460)
            )
          )
        )
      )

      target shouldEqual expected
    }
  }

}
