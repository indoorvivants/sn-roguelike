package roguelike

import scala.scalanative.unsafe.Zone

class Colors(using Zone):
  val WHITE = color(255, 255, 255, 255)
  val GREEN = color(0, 255, 0, 255)
  val YELLOW = color(255, 249, 0, 255)
  val RED = color(255, 0, 0, 255)
  val BLUE = color(0, 0, 255, 255)
  val BLACK = color(0, 0, 0, 255)
