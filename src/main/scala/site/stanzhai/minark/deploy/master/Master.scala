package site.stanzhai.minark.deploy.master

import akka.actor.{Actor, Props}
import akka.actor.Actor.Receive

import site.stanzhai.minark.util.AkkaUtils

/**
  * Created by stan on 2017/3/26.
  */
class Master extends Actor {

  override def receive: Receive = {
    case _ =>
  }
}

object Master {
  private val akkaSystemName = "minarkMaster"
  private val actorName = "master"

  def props(): Props = {
    Props(classOf[Master])
  }

  def main(args: Array[String]): Unit = {
    val akkaSystem = AkkaUtils.createActorSystem(akkaSystemName, "0.0.0.0", 3332)
    akkaSystem.actorOf(Master.props(), actorName)
  }
}