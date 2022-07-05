package roguelike

import scalanative.unsafe.*

opaque type Walkable = Boolean
object Walkable extends YesNo[Walkable]

opaque type Transparent = Boolean
object Transparent extends YesNo[Transparent]

opaque type Visible = Boolean
object Visible extends YesNo[Visible]

opaque type Explored = Boolean
object Explored extends YesNo[Explored]

class TileType(using Zone)(using colors: Colors):
  val floor = Tile(
    Walkable.Yes,
    Transparent.Yes,
    Graphic(c" ", colors.WHITE, color(50, 50, 150)),
    Graphic(c" ", colors.WHITE, color(200, 180, 50))
  )
  val wall = Tile(
    Walkable.No,
    Transparent.No,
    Graphic(c" ", colors.WHITE, color(0, 0, 100)),
    Graphic(c" ", colors.WHITE, color(130, 110, 50))
  )

  val red = Tile(
    Walkable.Yes,
    Transparent.Yes,
    Graphic(c" ", colors.WHITE, color(150, 0, 0)),
    Graphic(c" ", colors.WHITE, color(220, 0, 0))
  )

  val shroud =
    Tile(
      Walkable.No,
      Transparent.No,
      Graphic(c" ", colors.WHITE, color(0, 0, 0)),
      Graphic(c" ", colors.WHITE, color(0, 0, 0))
    )
end TileType
