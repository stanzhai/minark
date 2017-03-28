package site.stanzhai.minark

/**
 * Created by stan on 2017/3/28.
 */
trait Partition extends Serializable {

  def index: Int

  override def hashCode(): Int = index
}
