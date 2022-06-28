package roguelike

import raylib.functions.*
import raylib.types.*

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

@main def go_rogue =
  given rc: RenderConfig = RenderConfig(tileSize = 25)

  val gameWidth = X(25)
  val gameHeight = Y(25)

  val screenWidth = gameWidth.map(_ * rc.tileSize)
  val screenHeight = gameHeight.map(_ * rc.tileSize)

  InitWindow(
    screenWidth.value,
    screenHeight.value,
    c"Scala Native Roguelike"
  )

  Zone { implicit z =>
    given colors: Colors = Colors()
    given tileTypes: TileType = TileType()

    import colors.*

    SetTargetFPS(30)

    val midpoint = Position.midpoint(gameWidth, gameHeight)

    val player = Entity(midpoint, c"@", WHITE)
    val npc = Entity(midpoint.change(dx = dX(-5)), c"@", YELLOW)

    val game = GameMap.create(gameWidth, gameHeight)

    for
      x <- 30 to 33
      y = 22
    do game.updateTile(X(x), Y(y), tileTypes.wall)

    Engine.start(
      Set(npc),
      player,
      game
    ) { engine =>

      while !WindowShouldClose() do
        BeginDrawing()
        ClearBackground(BLACK)

        EventHandler.dispatch.foreach(engine.react)
        engine.render

        EndDrawing()
      end while
    }
    CloseWindow()
  }
end go_rogue
