package com.simplaex

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import cats.effect.Sync

object IOUtils {

  def writeToFile[F[_]: Sync](s: String): F[Unit] = Sync[F].delay {
    Files.write(
      Paths.get(s"${System.currentTimeMillis()}.txt"),
      s.getBytes(StandardCharsets.UTF_8)
    )
  }

}
