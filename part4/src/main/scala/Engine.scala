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

  private var playerLocPrev = Position(X(0), Y(0))

  def iteration(using rc: RenderConfig) =
    EventHandler.dispatch.foreach(react)
    update_fov
    render

  def render(using RenderConfig) =
    state.gameMap.draw

    state.entities.foreach { e =>
      if state.gameMap.isVisible(e.pos) then draw(e, e.pos)
    }

    draw(state.player, state.player.pos)

  end render

  def react(a: Action) =
    a match
      case move: Movement =>
        val moved = state.player.move(move)
        if state.gameMap.isWalkable(moved.pos) then
          state = state.copy(player = moved)
      case Escape => sys.exit(0)

  def update_fov =
    if playerLocPrev != state.player.pos then
      scribe.debug(
        s"Updating fov: ${playerLocPrev.show} -> ${state.player.pos.show}"
      )
      val fov = Geometry.circleAround(
        state.player.pos,
        8,
        pos => !state.gameMap.isTransparent(pos) || !state.gameMap.inBounds(pos)
      )
      state.gameMap.setVisiblePoints(fov)
      fov.forEachPoint(state.gameMap.setExplored(_))
      playerLocPrev = state.player.pos
  end update_fov

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
