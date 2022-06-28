package roguelike

import scalanative.unsafe.*
import raylib.types.*
import raylib.functions.*

case class Graphic(ch: CString, fg: Color, bg: Color)
case class Tile(walkable: Walkable, transparent: Transparent, dark: Graphic)

object Tile:
  given drawTile(using rc: RenderConfig): Drawable[Tile] with
    def draw(tile: Tile, pos: Position) =
      DrawRectangle(
        pos.pixelX.value,
        pos.pixelY.value,
        rc.tileSize,
        rc.tileSize,
        stackPtr(tile.dark.bg)
      )
      DrawText(
        tile.dark.ch,
        pos.pixelX.value,
        pos.pixelY.value,
        rc.tileSize,
        stackPtr(tile.dark.fg)
      )
    end draw
  end drawTile
end Tile

opaque type Walkable = Boolean
object Walkable extends YesNo[Walkable]

opaque type Transparent = Boolean
object Transparent extends YesNo[Transparent]

class TileMatrix private (
    private val tiles: Array[Tile],
    private val positions: Array[Position],
    width: X,
    height: Y
):

  import TileMatrix.*
  private inline def offset(x: X, y: Y) = Offset(
    y.value * width.value + x.value
  )

  inline def at(x: X, y: Y) =
    tiles(offset(x, y).value)

  inline def positionAt(x: X, y: Y) =
    positions(offset(x, y).value)

  inline def set(x: X, y: Y, tile: Tile) =
    tiles.update(offset(x, y).value, tile)

  inline def inBounds(x: X, y: Y) =
    x.value >= 0 && x.value < width.value &&
      y.value >= 0 && y.value < height.value

end TileMatrix

object TileMatrix:
  private[TileMatrix] opaque type Offset = Int
  private[TileMatrix] object Offset extends OpaqueNum[Offset]

  def create(width: X, height: Y)(using tt: TileType) =
    val size = width.value * height.value
    val arr = Array.fill(size)(tt.floor)
    // pre-calculate positions
    val positions = Array.fill(size)(Position(X(0), Y(0)))
    for
      x <- 0 until width.value
      y <- 0 until height.value
    do positions.update(y * width.value + x, Position(X(x), Y(y)))

    new TileMatrix(arr, positions, width, height)
  end create
end TileMatrix

case class GameMap private (tiles: TileMatrix, width: X, height: Y):
  inline def draw(using rc: RenderConfig) =
    for
      x <- 0 until width.value
      y <- 0 until height.value
    do
      val tile = tiles.at(X(x), Y(y))
      val pos = tiles.positionAt(X(x), Y(y))
      summon[Drawable[Tile]].draw(tile, pos)

  inline def updateTile(x: X, y: Y, tile: Tile) =
    tiles.set(x, y, tile)

  inline def isWalkable(x: X, y: Y) =
    tiles.inBounds(x, y) && tiles.at(x, y).walkable == Walkable.Yes

end GameMap

object GameMap:
  def create(width: X, height: Y)(using tt: TileType) =
    new GameMap(TileMatrix.create(width, height), width, height)
