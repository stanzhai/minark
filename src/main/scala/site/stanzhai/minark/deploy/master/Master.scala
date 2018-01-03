package site.stanzhai.minark.deploy.master

import scala.collection.mutable
import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Terminated
import site.stanzhai.minark.MinarkConf
import site.stanzhai.minark.deploy.DeployMessages._
import site.stanzhai.minark.util.AkkaUtils
import site.stanzhai.minark.util.Logging


/**
  * Created by stan on 2017/3/26.
  */
class Master extends Actor with Logging {

  val workers = new mutable.HashSet[WorkerInfo]()
  val idToWorker = new mutable.HashMap[String, WorkerInfo]()
  val actorToWorker = new mutable.HashMap[ActorRef, WorkerInfo]()

  override def receive: Receive = {
    case RegisterWorker(id, host, port, cores, memory) =>
      val worker = sender()
      val workerInfo = new WorkerInfo(id, host, port, cores, memory, worker)
      if (registerWorker(workerInfo)) {
        worker ! RegisteredWorker()
        context.watch(worker)
      }
    case Terminated(worker) =>
      removeWorker(worker)
      logInfo(s"$worker terminated")
  }

  private def registerWorker(workerInfo: WorkerInfo): Boolean = {
    workers += workerInfo
    idToWorker(workerInfo.id) = workerInfo
    actorToWorker(workerInfo.actorRef) = workerInfo
    logInfo(s"${workerInfo.actorRef} registered to master")
    true
  }

  private def removeWorker(actor: ActorRef) = {
    if (actorToWorker.contains(actor)) {
      val workerInfo = actorToWorker(actor)
      workers -= workerInfo
      idToWorker.remove(workerInfo.id)
      actorToWorker.remove(actor)
      logInfo(s"${workerInfo.actorRef} removed")
    }
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
    val conf = new MinarkConf()
    val akkaSystem = AkkaUtils.createActorSystem(actorSystemName, "0.0.0.0", conf.MasterPort)
    akkaSystem.actorOf(Master.props(), actorName)
  }
}