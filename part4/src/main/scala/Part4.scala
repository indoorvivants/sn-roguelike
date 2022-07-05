package roguelike

import raylib.functions.*

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

@main def go_rogue =
  given rc: RenderConfig = RenderConfig(tileSize = 15)

  scribe.Logger.root
    .clearHandlers()
    .clearModifiers()
    .withHandler(minimumLevel = Some(scribe.Level.Trace))
    .replace()

  val gameWidth = X(60)
  val gameHeight = Y(40)

  Zone { implicit z =>
    given colors: Colors = Colors()
    given tileTypes: TileType = TileType()

    import colors.*

    val game = GameMap.create(gameWidth, gameHeight, tileTypes.wall)

    given Random with
      def probability = Probability(scala.util.Random.nextDouble())
      def int(from: Int, to: Int) =
        scala.util.Random.nextInt(to) + from

    val GeneratedRooms(rooms, playerLocation, tunnels) =
      RoomGenerator.generate(
        15,
        min_size = Cells(3),
        max_size = Cells(5),
        game = game
      )

    val player = Entity(playerLocation, c"@", WHITE)

    val npcs = rooms.map { room =>
      val midpoint = room.center
      Entity(midpoint, c"@", RED)
    }.toSet

    rooms.foreach(game.carveOut(_, tileTypes.floor))
    tunnels.foreach(game.carveOut(_, tileTypes.floor))

    Engine.start(
      npcs,
      player,
      game
    ) { engine =>
      SetTargetFPS(60)

      val screenWidth = gameWidth.map(_ * rc.tileSize)
      val screenHeight = gameHeight.map(_ * rc.tileSize)

      InitWindow(
        screenWidth.value,
        screenHeight.value,
        c"Scala Native Roguelike"
      )

      while !WindowShouldClose() do
        BeginDrawing()
        ClearBackground(BLACK)

        engine.iteration

        DrawFPS(10, 10)

        EndDrawing()
      end while
    }
    CloseWindow()
  }
end go_rogue
