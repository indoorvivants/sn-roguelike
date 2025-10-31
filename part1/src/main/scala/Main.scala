package roguelike

import raylib.functions.*
import raylib.types.*

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

@main def go_rogue =
  val screenWidth = 800
  val screenHeight = 450

  InitWindow(
    screenWidth,
    screenHeight,
    c"Scala Native Roguelike"
  )

  Zone { implicit z =>
    val WHITE = Color(255.toUByte, 255.toUByte, 255.toUByte, 255.toUByte)
    val GREEN = Color(0.toUByte, 255.toUByte, 0.toUByte, 255.toUByte)
    val BLUE = Color(0.toUByte, 0.toUByte, 255.toUByte, 255.toUByte)
    val BLACK = Color(0.toUByte, 0.toUByte, 0.toUByte, 255.toUByte)

    var player_x = screenWidth / 2
    var player_y = screenHeight / 2

    SetTargetFPS(200)

    var i = 0

    while !WindowShouldClose() do
      BeginDrawing()

      import Action.*

      EventHandler.dispatch.foreach {
        case Movement(dx, dy) =>
          player_x = player_x + dx.value
          player_y = player_y + dy.value
        case Escape => println("QUIT BITCH")
      }

      ClearBackground(!BLACK)

      DrawText(c"@", player_x, player_y, 50, !WHITE)

      EndDrawing()
    end while
    CloseWindow()
  }
end go_rogue
