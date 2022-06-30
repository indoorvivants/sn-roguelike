package roguelike

import raylib.types.Color
import raylib.functions.DrawText

import scalanative.unsafe.*

case class Line(from: Position, to: Position):
  inline def midpoint =
    Position(
      x = from.x.map(num => (num + to.x.value) / 2),
      y = from.y.map(num => (num + to.y.value) / 2)
    )

  inline def forEachPoint(inline f: Position => Unit) =
    val signX =
      if to.x.value > from.x.value then 1
      else if to.x.value < from.x.value then -1
      else 0
    val signY =
      if to.y.value > from.y.value then 1
      else if to.y.value < from.y.value then -1
      else 0

    var curPos = from

    f(curPos)

    while curPos != to do

      curPos =
        curPos.copy(x = curPos.x.map(_ + signX), y = curPos.y.map(_ + signY))

      f(curPos)

  end forEachPoint
end Line

case class Position(x: X, y: Y):
  def change(dx: dX = dX(0), dy: dY = dY(0)) =
    copy(x = x.map(_ + dx.value), y = y.map(_ + dy.value))

  inline def pixelX(using rc: RenderConfig) =
    Position.pixelX(x)

  inline def pixelY(using rc: RenderConfig) =
    Position.pixelY(y)
end Position

object Position:
  def midpoint(x: X, y: Y) = Position(x.map(_ / 2), y.map(_ / 2))

  inline def pixelX(x: X)(using rc: RenderConfig) =
    PixelX(x.value * rc.tileSize)

  inline def pixelY(y: Y)(using rc: RenderConfig) =
    PixelY(y.value * rc.tileSize)

case class Entity(pos: Position, char: CString, color: Color):
  self =>
  def move(ment: Action.Movement) =
    copy(pos = pos.change(ment.dx, ment.dy))
end Entity

inline def stackPtr[T: Tag](inline v: T) =
  val ptr = stackalloc[T](1)
  !ptr = v

  ptr

object Entity:
  given drawEntity(using rc: RenderConfig): Drawable[Entity] with
    def draw(e: Entity, pos: Position) =
      val pixelX = pos.x.value * rc.tileSize
      val pixelY = pos.y.value * rc.tileSize
      DrawText(e.char, pixelX, pixelY, rc.tileSize, stackPtr(e.color))
