package roguelike

import raylib.types.Color
import raylib.functions.DrawText

import scalanative.unsafe.*

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
