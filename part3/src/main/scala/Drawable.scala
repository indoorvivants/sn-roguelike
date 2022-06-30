package roguelike

trait Drawable[T]:
  def draw(t: T, pos: Position): Unit
