package com.simplaex

import java.util.UUID

case class Row(userId: UUID, data: String, a: Double, b: BigInt, c: BigInt)

object Row {

  def applyUnsafe(s: String): Row = s.split(",") match {
    case Array(userId, data, a, b, c) =>
      Row(UUID.fromString(userId), data, a.toDouble, BigInt(b), BigInt(c))
    case _ =>
      throw new IllegalArgumentException(s"Unexpected row format: $s")
  }

}
