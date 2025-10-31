package roguelike

import scalanative.unsafe.*
import raylib.types.Color
import raylib.functions.*
import roguelike.MatrixLike.Offset
import scala.collection.immutable.BitSet.BitSet1

class Mask[T] private (bs: Ptr[Byte], width: X, height: Y, nt: YesNo[T])
    extends MatrixLike[T](width, height):

  private val SET = 1.toByte
  private val UNSET = 0.toByte
  protected inline def atOffset(offset: Offset): T =
    nt.apply(bs(offset.value) == SET)

  protected inline def updateOffset(offset: Offset, t: T): Unit =
    bs.update(offset.value.toLong, if t == nt.Yes then SET else UNSET)
end Mask

object Mask:
  def create[T](width: X, height: Y, nt: YesNo[T])(using Zone) =
    val bytes = alloc[Byte](width.value * height.value)
    new Mask[T](bytes, width, height, nt)
