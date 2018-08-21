package site.stanzhai.minark.scheduler

/**
  * Created by stan on 2017/4/16.
  */
abstract class Task[T](
    val taskBinary: Array[Byte],
    val stageId: Int,
    val partitionId: Int,
    val jobId: Option[Int] = None,
    val appId: Option[String] = None) extends Serializable {

  def preferredLocations: Seq[TaskLocation] = Nil

  def run(): T = {
    runTask()
  }

  def runTask(): T

  def kill(): Unit = {

  }
}
