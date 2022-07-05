package roguelike

enum LineDrawingAlgorithm:
  case Bresenham

object Algorithms:
  def bresenham(
      p0: Position,
      p1: Position,
      stop: Position => Boolean = _ => false
  ) = PointIterator.cached { f =>
    val Position(x0, y0) = p0
    val Position(x1, y1) = p1

    val dx = dX(Math.abs(x0.value - x1.value))
    val sx = if x0.value < x1.value then 1 else -1

    val dy = dY(-Math.abs(y0.value - y1.value))
    val sy = if y0.value < y1.value then 1 else -1

    val error0 = dx.value + dy.value

    var x_cur = x0
    var y_cur = y0
    var error_cur = error0

    var continue = true
    while continue do
      val newPos = Position(x_cur, y_cur)

      f(newPos)

      val shouldStop =
        stop(newPos) ||
          (x_cur == x1 && y_cur == y1) ||
          (error_cur * 2 >= dy.value && x_cur == x1) ||
          (error_cur * 2 <= dx.value && y_cur == y1)

      if !shouldStop then
        if error_cur * 2 >= dy.value then
          error_cur += dy.value
          x_cur = X(x_cur.value + sx)

        if error_cur * 2 <= dx.value then
          error_cur += dx.value
          y_cur = Y(y_cur.value + sy)

      continue = !shouldStop
    end while

  }
  end bresenham
end Algorithms
