package com.simplaex

import java.util.UUID

import fs2._

import scala.collection.mutable

case class UserStats(avgOfA: Double, latestB: BigInt) {

  def toString(userId: UUID): String = s"$userId,$avgOfA,$latestB"

}

case class Stats(sumOfC: BigInt, byUser: Map[UUID, UserStats]) {

  override def toString: String = {
    val userRows = byUser.map { case (userId, stats) => stats.toString(userId) }.mkString("\n")
    s"$sumOfC\n${byUser.size}\n$userRows"
  }

}

object Stats {

  private class MutableUserStats(
    private var count: Int = 0,
    private var sumOfA: Double = 0d,
    private var latestB: BigInt = 0
  ) {

    def update(row: Row): Unit = {
      count += 1
      sumOfA += row.a
      latestB = row.b
    }

    def toUserStats: UserStats = UserStats(sumOfA / count, latestB)

  }

  def compute[F[_]](groupSize: Int)(rows: Stream[F, Row]): Stream[F, Stats] = {
    rows.chunkN(groupSize).map { chunk =>
      var sumOfC = BigInt(0)
      val byUser = mutable.Map.empty[UUID, MutableUserStats]

      for (row <- chunk) {
        sumOfC += row.c
        val userStats = byUser.getOrElseUpdate(row.userId, new MutableUserStats())
        userStats.update(row)
      }

      Stats(sumOfC, byUser.mapValues(_.toUserStats).toMap)
    }
  }

}
