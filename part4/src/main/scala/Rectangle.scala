package roguelike

case class Rectangle(x: X, y: Y, width: X, height: Y):
  val topLeft = Position(x, y)
  val topRight = topLeft.change(dx = width.into(dX))
  val bottomLeft = topLeft.change(dy = height.into(dY))
  val bottomRight = bottomLeft.change(dx = width.into(dX))
  val diagonal = Line(topLeft, bottomRight)
  val center = diagonal.midpoint

  inline def intersects(other: Rectangle) =
    topLeft.x.value <= other.bottomRight.x.value &&
      bottomRight.x.value >= other.topLeft.x.value &&
      topLeft.y.value <= other.bottomRight.y.value &&
      bottomRight.y.value >= other.topLeft.y.value

  val perimeter = PointIterator.cached { f =>
    Vector(
      Line(topLeft, topRight),
      Line(topRight, bottomRight),
      Line(bottomRight, bottomLeft),
      Line(bottomLeft, topLeft)
    ).foreach { line =>
      line.points.forEachPoint(f)
    }
  }

end Rectangle
