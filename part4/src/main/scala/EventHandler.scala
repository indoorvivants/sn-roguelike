package roguelike

import raylib.functions.*
import raylib.types.*
import scala.concurrent.duration.*

object EventHandler:
  private var lastDetection = -1L
  private val dampen = 40.millis

  inline def dampen(inline action: Action): Option[Action] =
    if lastDetection == -1L then
      lastDetection = System.nanoTime()
      Some(action)
    else if (System.nanoTime - lastDetection) > dampen.toNanos then
      lastDetection = System.nanoTime()
      Some(action)
    else None
  end dampen

  def keyDown: Option[Action] =
    import KeyboardKey.*
    import Action.*
    if IsKeyDown(KEY_RIGHT.int) then dampen(Movement(dX(1), dY(0)))
    else if IsKeyDown(KEY_LEFT.int) then dampen(Movement(dX(-1), dY(0)))
    else if IsKeyDown(KEY_UP.int) then dampen(Movement(dX(0), dY(-1)))
    else if IsKeyDown(KEY_DOWN.int) then dampen(Movement(dX(0), dY(1)))
    else if IsKeyDown(KEY_ESCAPE.int) then Some(Escape)
    else None
  end keyDown

  def dispatch: Option[Action] = keyDown
end EventHandler
