package roguelike

import scala.util.chaining.*
import scala.annotation.targetName

object Geometry:
  inline def squareAround(loc: Position, halfSide: Int) =
    scribe.debug(s"Doing square around ${loc.show}")
    val width = X(halfSide * 2)
    val height = Y(halfSide * 2)
    val topLeft = loc.change(dx = dX(-halfSide), dy = dY(-halfSide))

    Rectangle(topLeft.x, topLeft.y, width, height)
  end squareAround

  inline def circleAround(
      loc: Position,
      radius: Int,
      stop: Position => Boolean
  ) =
    PointIterator.cached { f =>
      scribe.debug(s"Doing circle around ${loc.show}")
      squareAround(loc, halfSide = radius).perimeter.forEachPoint { pos =>
        val ray = Ray(loc, pos, stop).points.withinRadius(loc, radius - 1)

        ray.forEachPoint(f)
      }
    }
end Geometry

trait PointIterator:
  self =>
  def forEachPoint(f: Position => Unit): Unit

  def withinRadius(start: Position, radius: Int) =
    import QuickMaffs.*
    val radiusSquared = sq(radius)

    new PointIterator:
      override def forEachPoint(f: Position => Unit) =
        self.forEachPoint { point =>
          val dist =
            sq(point.x.value - start.x.value) +
              sq(point.y.value - start.y.value)

          if dist <= radiusSquared then f(point)
        }
    end new
  end withinRadius
end PointIterator

trait PointSet:
  def points: PointIterator

object PointIterator:
  def cached(compute: (Position => Unit) => Unit): PointIterator =
    new PointIterator:
      lazy val cache =
        val build = Vector.newBuilder[Position]
        val f = (p: Position) =>
          build += p; ()
        compute(f)
        build.result()

      def forEachPoint(f: Position => Unit) =
        cache.foreach(f)
end PointIterator

case class Ray(from: Position, to: Position, stop: Position => Boolean):
  private val bresenham =
    Algorithms.bresenham(from, to, stop)

  inline def points(using
      ld: LineDrawingAlgorithm = LineDrawingAlgorithm.Bresenham
  ) =
    ld match
      case LineDrawingAlgorithm.Bresenham => bresenham
end Ray

case class Line(from: Position, to: Position):
  inline def midpoint =
    Position(
      x = from.x.map(num => (num + to.x.value) / 2),
      y = from.y.map(num => (num + to.y.value) / 2)
    )

  val points = PointIterator.cached { f =>
    val signX =
      if to.x.value > from.x.value then 1
      else if to.x.value < from.x.value then -1
      else 0
    val signY =
      if to.y.value > from.y.value then 1
      else if to.y.value < from.y.value then -1
      else 0

    scribe.debug(
      s"Line from ${from.show} to ${to.show}, signX: $signX, signY: $signY"
    )

    var curPos = from

    f(curPos)

    while curPos != to do
      curPos = curPos.change(dx = dX(signX), dy = dY(signY))

      f(curPos)
    end while
  }
end Line

opaque type Position = Long
object Position:
  private inline val shift = 32

  inline def apply(x: X, y: Y): Position =
    val saneX = if x.value < 0 then 0 else x.value
    val saneY = if y.value < 0 then 0 else y.value
    (saneX.toLong << shift) + saneY.toLong

  def getX(p: Position): X = X((p >>> shift).toInt)
  def getY(p: Position): Y = Y(((p << shift) >>> shift).toInt)

  extension (p: Position)
    inline def x: X = getX(p)
    inline def y: Y = getY(p)
    inline def show: String = s"Position(${getX(p)}, ${getY(p)})"
    inline def copy(x: X, y: Y) = Position(x, y)
    inline def copy(x: X) = Position(x, getY(p))
    @targetName("copy_y")
    inline def copy(y: Y) = Position(getX(p), y)

    inline def change(dx: dX = dX(0), dy: dY = dY(0)) =
      Position(getX(p).map(_ + dx.value), getY(p).map(_ + dy.value))

    inline def pixelX(using rc: RenderConfig) =
      PixelX(getX(p).value * rc.tileSize)

    inline def pixelY(using rc: RenderConfig) =
      PixelY(getY(p).value * rc.tileSize)
  end extension

  def unapply(p: Position) = Option(getX(p) -> getY(p))

  def midpoint(x: X, y: Y) = Position(x.map(_ / 2), y.map(_ / 2))
end Position
