package site.stanzhai.minark.deploy.master

import akka.actor.{Actor, Props}
import akka.actor.Terminated

import site.stanzhai.minark.deploy.DeployMessages._
import site.stanzhai.minark.util.AkkaUtils
import site.stanzhai.minark.util.Logging

/**
  * Created by stan on 2017/3/26.
  */
class Master extends Actor with Logging {

  override def receive: Receive = {
    case RegisterWorker(id, host, port, cores, memory) =>
      val worker = sender()
      worker ! RegisteredWorker()
      context.watch(worker)
      logInfo(id)
    case Terminated(worker) =>
      logInfo(s"$worker terminated")
  }
}

object Master {
  private val actorSystemName = "minarkMaster"
  private val actorName = "master"
  private val masterUrlRegex = "minark://([^:]+):(\\d+)".r

  def props(): Props = {
    Props(classOf[Master])
  }

  def akkaUrl(masterUrl: String): String = {
    masterUrl match {
      case masterUrlRegex(host, port) =>
        s"akka.tcp://$actorSystemName@$host:$port/user/$actorName"
      case _ =>
        throw new Exception("Invalid master URL: " + masterUrl)
    }
  }

  def main(args: Array[String]): Unit = {
    val akkaSystem = AkkaUtils.createActorSystem(actorSystemName, "0.0.0.0", 3332)
    akkaSystem.actorOf(Master.props(), actorName)
  }
}