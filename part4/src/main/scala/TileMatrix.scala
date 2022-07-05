package roguelike

import scalanative.unsafe.*
import raylib.types.{Color}
import raylib.functions.*
import roguelike.MatrixLike.Offset
import scala.collection.immutable.BitSet.BitSet1

class TileMatrix private (
    private val tiles: Array[Tile],
    private val positions: Array[Position],
    width: X,
    height: Y
) extends MatrixLike[Tile](width, height):
  protected inline def atOffset(offset: Offset): Tile =
    tiles(offset.value)

  protected inline def updateOffset(offset: Offset, t: Tile): Unit =
    tiles.update(offset.value, t)
end TileMatrix

object TileMatrix:
  private[TileMatrix] opaque type Offset = Int
  private[TileMatrix] object Offset extends OpaqueNum[Offset]

  def create(width: X, height: Y, tile: Tile) =
    val size = width.value * height.value
    val arr = Array.fill(size)(tile)
    // pre-calculate positions
    val positions = Array.fill(size)(Position(X(0), Y(0)))
    for
      x <- 0 until width.value
      y <- 0 until height.value
    do positions.update(y * width.value + x, Position(X(x), Y(y)))

    new TileMatrix(arr, positions, width, height)
  end create
end TileMatrix
