package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Tween;

public class State {
  private boolean paused;

  public State() {

  }

  protected static void transition(final Runnable runnable) {
    Dungeon.darkX = Input.instance.uiMouse.x;
    Dungeon.darkY = Input.instance.uiMouse.y;

    Tween.to(new Tween.Task(0, 0.2f) {
      @Override
      public float getValue() {
        return Dungeon.darkR;
      }

      @Override
      public void setValue(float value) {
        Dungeon.darkR = value;
      }

      @Override
      public void onEnd() {
        runnable.run();

        Tween.to(new Tween.Task(Dungeon.MAX_R, 0.2f) {
          @Override
          public float getValue() {
            return Dungeon.darkR;
          }

          @Override
          public void setValue(float value) {
            Dungeon.darkR = value;
          }
        });
      }
    });
  }

  public void onPause() {

  }

  public void onUnpause() {

  }

  public boolean isPaused() {
    return paused;
  }

  public void setPaused(boolean paused) {
    this.paused = paused;

    if (this.paused) {
      this.onPause();
    } else {
      this.onUnpause();
    }
  }

  public void init() {

  }

  public void destroy() {

  }

  public void update(float dt) {

  }

  public void render() {

  }

  public void renderUi() {

  }

  public void resize(int width, int height) {

  }
}