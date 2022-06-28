package roguelike

case class State(
    entities: Set[Entity],
    player: Entity,
    gameMap: GameMap
)

import Action.*

class Engine private (var state: State):
  private def draw[T](t: T, pos: Position)(using d: Drawable[T]) =
    d.draw(t, pos)

  def render(using RenderConfig) =
    state.gameMap.draw

    state.entities.foreach { e =>
      draw(e, e.pos)
    }

    draw(state.player, state.player.pos)

  end render

  def react(a: Action) =
    a match
      case move: Movement =>
        val moved = state.player.move(move)
        if state.gameMap.isWalkable(moved.pos.x, moved.pos.y) then
          state = state.copy(player = state.player.move(move))
      case Escape => sys.exit(0)

end Engine

object Engine:
  def start(entities: Set[Entity], player: Entity, gameMap: GameMap)(
      f: Engine => Unit
  ) =
    val eng = new Engine(
      State(
        entities,
        player,
        gameMap
      )
    )

    f(eng)
  end start
end Engine
