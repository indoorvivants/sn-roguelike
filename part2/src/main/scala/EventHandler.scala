package roguelike

import raylib.functions.*
import raylib.types.*

object EventHandler:
  def keyDown =
    import KeyboardKey.*
    import Action.*
    if IsKeyDown(KEY_RIGHT.int) then Some(Movement(dX(1), dY(0)))
    else if IsKeyDown(KEY_LEFT.int) then Some(Movement(dX(-1), dY(0)))
    else if IsKeyDown(KEY_UP.int) then Some(Movement(dX(0), dY(-1)))
    else if IsKeyDown(KEY_DOWN.int) then Some(Movement(dX(0), dY(1)))
    else if IsKeyDown(KEY_ESCAPE.int) then Some(Escape)
    else None

  def dispatch = keyDown
end EventHandler
