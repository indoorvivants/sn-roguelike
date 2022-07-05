
opaque type Position1 = Long
object Position1:
  def apply(x: X, y: Y): Position1 = 
    x.value.toLong << 32 + y.value.toLong

  private final val Y_MASK = (Int.MaxValue.toLong * 2 + 1)
  private final val X_MASK = Y_MASK << 32

  extension (p: Position1) 
    inline def x = X(((p & X_MASK) >> 32).toInt)
    inline def y = Y(((p & Y_MASK) >> 32).toInt)

  def midpoint(x: X, y: Y) = Position1(x.map(_ / 2), y.map(_ / 2))

  inline def pixelX(x: X)(using rc: RenderConfig) =
    PixelX(x.value * rc.tileSize)

  inline def pixelY(y: Y)(using rc: RenderConfig) =
    PixelY(y.value * rc.tileSize)
    

val x = 1

x + 1
