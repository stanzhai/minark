package site.stanzhai.minark

import scala.reflect.ClassTag

/**
 * Created by stan on 2017/3/28.
 */
abstract class RDD[T: ClassTag](private var mc: MinarkContext) extends Serializable {

  def compute(split: Partition): Iterator[T]

  protected def getPartitions: Array[Partition]

  protected def getPreferredLocations: Seq[String] = Nil

}
