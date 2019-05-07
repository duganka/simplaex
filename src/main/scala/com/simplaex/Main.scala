package com.simplaex

import java.net.InetSocketAddress
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.spi.AsynchronousChannelProvider
import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, IOApp}
import fs2.io.tcp
import fs2.{text, Stream}
import org.rogach.scallop.ScallopConf

object Main extends IOApp {

  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
    val port = opt[Int](required = false, default = Some(9000))
    val groupSize = opt[Int](required = false, default = Some(1000))
    verify()
  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      conf <- IO(new Conf(args))
      result <- runServer(conf).attempt
    } yield {
      result match {
        case Left(e) =>
          // log error
          conf.printHelp()
          ExitCode.Error
        case Right(_) =>
          ExitCode.Success
      }
    }
  }

  private def runServer(conf: Conf): IO[Unit] = {
    Stream
      .bracket(
        IO(AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(Executors.newCachedThreadPool(), 1))
      )(acg => IO(acg.shutdown()))
      .flatMap { asynchronousChannelGroup =>
        println(s"Starting server on port ${conf.port()} ...")
        implicit val asg: AsynchronousChannelGroup = asynchronousChannelGroup
        tcp.Socket.server[IO](new InetSocketAddress("0.0.0.0", conf.port()), maxQueued = 1)
      }
      .flatMap(Stream.resource)
      .map { socket =>
        println(s"Connection on $socket")
        socket
          .reads(32768)
          .through(text.utf8Decode)
          .through(text.lines)
          .evalMap(s => IO(Row.applyUnsafe(s))) // no instructions on how to deal with invalid input, so failing
          .through(Stats.compute(conf.groupSize()))
          .map(_.toString)
          .evalMap(IOUtils.writeToFile[IO])
          .drain
          .attempt
          .map {
            case Left(e) =>
              println("Connection closed")
            case Right(_) =>
              println("Probably will never be here")
          }
      }
      .parJoin(1)
      .compile
      .drain
  }

}
