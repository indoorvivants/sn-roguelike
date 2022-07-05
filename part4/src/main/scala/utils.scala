package roguelike

import raylib.types.*
import scalanative.unsigned.*
import scalanative.unsafe.*

inline def color(r: Int, g: Int, b: Int, alpha: Int = 255)(using Zone) =
  !Color(r.toUByte, g.toUByte, b.toUByte, alpha.toUByte)
