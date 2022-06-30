package roguelike

trait Random:
  def probability: Probability
  def int(from: Int, to: Int): Int

case class Tunnel(lines: Vector[Line]):
  inline def foreachPoint(inline f: Position => Unit) =
    lines.foreach { line =>
      line.forEachPoint(f)
    }

object Tunnel:
  def L_shaped(from: Position, to: Position)(using rand: Random) =
    val Position(x1, y1) = from
    val Position(x2, y2) = to

    val bend =
      if rand.probability.value < 0.5 then Position(x2, y1)
      else Position(x1, y2)

    val line1 = Line(from, bend)
    val line2 = Line(bend, to)

    Tunnel(Vector(line1, line2))
  end L_shaped
end Tunnel
