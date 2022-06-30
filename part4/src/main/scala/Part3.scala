package roguelike

import raylib.functions.*
import raylib.types.*

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

@main def go_rogue =
  given rc: RenderConfig = RenderConfig(tileSize = 20)

  val gameWidth = X(50)
  val gameHeight = Y(50)

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

    val npc = Entity(midpoint.change(dx = dX(-5)), c"@", YELLOW)

    val game = GameMap.create(gameWidth, gameHeight, tileTypes.wall)

    given Random with
      def probability = Probability(scala.util.Random.nextDouble())
      def int(from: Int, to: Int) =
        scala.util.Random.nextInt(to) + from

    val GeneratedRooms(rooms, playerLocation, tunnels) =
      RoomGenerator.generate(
        10,
        min_size = Cells(5),
        max_size = Cells(10),
        game = game
      )

    val player = Entity(playerLocation, c"@", WHITE)

    rooms.foreach(game.carveOut(_, tileTypes.floor))
    tunnels.foreach(game.carveOut(_, tileTypes.floor))

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
