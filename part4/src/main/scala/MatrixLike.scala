package roguelike

import scalanative.unsafe.*
import raylib.types.{Color}
import raylib.functions.*
import roguelike.MatrixLike.Offset
import scala.collection.immutable.BitSet.BitSet1

abstract class MatrixLike[T](val width: X, val height: Y):
  import MatrixLike.*
  protected inline def atOffset(offset: Offset): T
  protected inline def updateOffset(offset: Offset, t: T): Unit

  protected inline def offset(pos: Position) = Offset(
    pos.y.value * width.value + pos.x.value
  )

  inline def at(pos: Position) =
    if inBounds(pos) then atOffset(offset(pos))
    else
      throw new Exception(
        s"Attempted to access position ${pos.x} ${pos.y} which is out of bounds"
      )

  inline def set(pos: Position, tile: T) =
    if inBounds(pos) then updateOffset(offset(pos), tile)
    else
      throw new Exception(
        s"Attempted to update position ${pos.x} ${pos.y} which is out of bounds"
      )

  inline def inBounds(pos: Position) =
    pos.x.value >= 0 && pos.x.value < width.value &&
      pos.y.value >= 0 && pos.y.value < height.value
end MatrixLike

object MatrixLike:
  opaque type Offset = Int
  object Offset extends OpaqueNum[Offset]
