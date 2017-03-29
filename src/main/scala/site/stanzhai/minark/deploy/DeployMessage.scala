package site.stanzhai.minark.deploy

/**
  * Created by stan on 2017/3/26.
  */
sealed trait DeployMessage extends Serializable

object DeployMessages {

  case class RegisterWorker(
      id: String,
      host: String,
      port: Int,
      cores: Int,
      memory: Int)
    extends DeployMessage

  case class RegisteredWorker() extends DeployMessage

  case class HeartBeat(workerId: String) extends DeployMessage
}
