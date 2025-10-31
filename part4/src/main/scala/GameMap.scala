package roguelike

import scalanative.unsafe.*
import raylib.types.Color
import raylib.functions.*
import roguelike.MatrixLike.Offset
import scala.collection.immutable.BitSet.BitSet1

case class Graphic(ch: CString, fg: Color, bg: Color)
case class Tile(
    walkable: Walkable,
    transparent: Transparent,
    dark: Graphic,
    light: Graphic
)

object Tile:
  def draw(tile: Tile, pos: Position, graphic: Graphic)(using
      rc: RenderConfig
  ) =
    DrawRectangle(
      pos.pixelX.value,
      pos.pixelY.value,
      rc.tileSize,
      rc.tileSize,
      stackPtr(graphic.bg)
    )
    DrawText(
      tile.dark.ch,
      pos.pixelX.value,
      pos.pixelY.value,
      rc.tileSize,
      stackPtr(graphic.fg)
    )
  end draw
end Tile

case class GameMap private (
    tiles: TileMatrix,
    visible: Mask[Visible],
    explored: Mask[Explored]
)(using tileTypes: TileType):
  val width = tiles.width
  val height = tiles.height

  inline def foreachPosition(inline f: Position => Unit) =
    var x = 0
    var y = 0
    while x < width.value do
      y = 0
      while y < height.value do
        f(Position(X(x), Y(y)))
        y += 1
      x += 1
  end foreachPosition

  inline def draw(using rc: RenderConfig) =
    foreachPosition { pos =>
      val tile = tiles.at(pos)
      if isVisible(pos) then Tile.draw(tile, pos, tile.light)
      else if isExplored(pos) then Tile.draw(tile, pos, tile.dark)
      else Tile.draw(tileTypes.shroud, pos, tileTypes.shroud.dark)
    }

  inline def updateTile(pos: Position, tile: Tile): GameMap =
    tiles.set(pos, tile)
    this

  inline def isVisible(pos: Position) = visible.at(pos) == Visible.Yes
  inline def isExplored(pos: Position) = explored.at(pos) == Explored.Yes

  inline def isTransparent(pos: Position) =
    tiles.at(pos).transparent == Transparent.Yes

  inline def setVisible(pos: Position): Unit =
    visible.set(pos, Visible.Yes)

  inline def setHidden(pos: Position): Unit =
    visible.set(pos, Visible.No)

  inline def setExplored(pos: Position) =
    explored.set(pos, Explored.Yes)

  inline def setVisiblePoints(pi: PointIterator) =
    foreachPosition(setHidden(_))
    pi.forEachPoint(setVisible(_))

  inline def isWalkable(pos: Position) =
    tiles.inBounds(pos)
      && tiles.at(pos).walkable == Walkable.Yes

  inline def carveOut(room: Rectangle, tile: Tile): GameMap =
    for
      offsetX <- 0 until room.width.value
      offsetY <- 0 until room.height.value
    do
      tiles.set(
        Position(room.x.map(_ + offsetX), room.y.map(_ + offsetY)),
        tile
      )
    this
  end carveOut

  inline def carveOut(tunnel: Tunnel, tile: Tile) =
    tunnel.points.forEachPoint { pos =>
      tiles.set(pos, tile)
    }

  inline def inBounds(pos: Position) = tiles.inBounds(pos)

end GameMap

object GameMap:
  def create(width: X, height: Y, tile: Tile)(using Zone, TileType) =
    val visible = Mask.create(width, height, Visible)
    val explored = Mask.create(width, height, Explored)
    new GameMap(TileMatrix.create(width, height, tile), visible, explored)
