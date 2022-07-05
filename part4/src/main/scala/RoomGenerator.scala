package roguelike

case class GeneratedRooms(
    rooms: Vector[Rectangle],
    playerLocation: Position,
    tunnels: Vector[Tunnel]
)

object RoomGenerator:
  def generate(maxRooms: Int, min_size: Cells, max_size: Cells, game: GameMap)(
      using rand: Random
  ): GeneratedRooms =
    val rooms = Vector.newBuilder[Rectangle]
    var loc = Position(X(0), Y(0))

    (0 to maxRooms).foreach { _ =>
      val roomWidth = rand.int(min_size.value, max_size.value)
      val roomHeight = rand.int(min_size.value, max_size.value)

      val x = X(rand.int(0, game.width.value - roomWidth - 1))
      val y = Y(rand.int(0, game.height.value - roomHeight - 1))

      val room = Rectangle(x, y, X(roomWidth), Y(roomHeight))

      val intersects = rooms.result().exists(_.intersects(room))

      if !intersects then
        if rooms.result().size == 0 then loc = room.center

        rooms += room
    }

    val tunnels = Vector.newBuilder[Tunnel]

    rooms.result().toList.sliding(2).foreach {
      case prev :: next :: Nil =>
        tunnels += Tunnel.L_shaped(prev.center, next.center)
      case _ =>
    }

    GeneratedRooms(rooms.result(), loc, tunnels.result())
  end generate
end RoomGenerator
