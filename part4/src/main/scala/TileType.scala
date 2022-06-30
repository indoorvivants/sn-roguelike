package roguelike

import scalanative.unsafe.*

class TileType(using Zone)(using colors: Colors):
  val floor = Tile(
    Walkable.Yes,
    Transparent.Yes,
    Graphic(c" ", colors.WHITE, color(50, 50, 150))
  )
  val wall = Tile(
    Walkable.No,
    Transparent.No,
    Graphic(c" ", colors.WHITE, color(0, 0, 150))
  )

  val red = Tile(
    Walkable.Yes,
    Transparent.No,
    Graphic(c" ", colors.WHITE, color(150, 0, 0))
  )
end TileType
