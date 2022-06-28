package roguelike

enum Action:
  case Escape
  case Movement(x: dX, y: dY)
