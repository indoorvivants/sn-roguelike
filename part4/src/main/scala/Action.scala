package roguelike

enum Action:
  case Escape
  case Movement(dx: dX, dy: dY)
