package javagames.completegame.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javagames.util.Matrix3x3f;
import javagames.util.Utility;
import sprite.CollidableSprite;

public class LevelStarting extends State {

  private CollidableSprite background;
  private final GameState  state;
  private double           time;

  public LevelStarting(final GameState state) {
    this.state = state;
  }

  @Override
  public void enter() {
    background = (CollidableSprite) controller.getAttribute("levelA");
    time = 0.0;
  }

  @Override
  public void updateObjects(final float delta) {
    time += delta;
    if (time > 2.0) {
      getController().setState(new LevelA(state));
    }
  }

  @Override
  public void render(final Graphics2D g, final Matrix3x3f view) {
    super.render(g, view);
    background.render(g, view);
    /*
     * Draw Hud here acme.drawScore(g, state.getScore()); acme.drawLives(g,
     * view, state.getLives());
     */
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.RED);
    Utility.drawCenteredString(g, app.getScreenWidth(),
        app.getScreenHeight() / 4, "L E V E L " + state.getLevel());
  }

}
