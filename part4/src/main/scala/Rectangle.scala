package roguelike

case class RectangularRoom(x: X, y: Y, width: X, height: Y):
  val topLeft = Position(x, y)
  val bottomRight = Position(x.map(_ + width.value), y.map(_ + height.value))
  val diagonal = Line(topLeft, bottomRight)
  val center = diagonal.midpoint

  inline def intersects(other: RectangularRoom) =
    topLeft.x.value <= other.bottomRight.x.value &&
      bottomRight.x.value >= other.topLeft.x.value &&
      topLeft.y.value <= other.bottomRight.y.value &&
      bottomRight.y.value >= other.topLeft.y.value
end RectangularRoom
