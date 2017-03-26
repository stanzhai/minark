package site.stanzhai.minark.util

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
  * Created by stan on 2017/3/26.
  */
object AkkaUtils {

  def createActorSystem(name: String, host: String, port: Int): ActorSystem = {
    val akkaConfig = ConfigFactory.parseString(
      s"""
        |akka.remote.netty.tcp.hostname = "$host"
        |akka.remote.netty.tcp.port = "$port"
      """.stripMargin)
      .withFallback(ConfigFactory.load("akka.conf"))

    val actorSystem = ActorSystem(name, akkaConfig)
    actorSystem
  }

}
