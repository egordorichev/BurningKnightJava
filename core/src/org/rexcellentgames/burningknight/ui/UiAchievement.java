package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class UiAchievement extends Entity {
  public String text;
  public String extra;
  private float w1;
  private float w2;
  private float a = 1;

  {
    alwaysActive = true;
    alwaysRender = true;
    depth = 32;
  }

  @Override
  public void init() {
    super.init();

    this.w = 0;
    Graphics.layout.setText(Graphics.small, this.text);
    this.w1 = Graphics.layout.width;

    if (this.extra != null) {
      Graphics.layout.setText(Graphics.small, this.extra);
      this.w2 = Graphics.layout.width;
      this.h += Graphics.layout.height + 4;
    }

    this.w = Math.max(this.w2, this.w1) + 16;
    this.y = -this.h;
    this.x = Display.GAME_WIDTH - 2 - this.w;

    Tween.to(new Tween.Task(2, 0.5f, Tween.Type.BACK_OUT) {
      @Override
      public float getValue() {
        return y;
      }

      @Override
      public void setValue(float value) {
        y = value;
      }

      @Override
      public void onEnd() {
        Tween.to(new Tween.Task(-h, 0.5f, Tween.Type.BACK_OUT) {
          @Override
          public float getValue() {
            return y;
          }

          @Override
          public void setValue(float value) {
            y = value;
          }

          @Override
          public void onEnd() {
            setDone(true);
          }
        }).delay(3);
      }
    });
  }

  @Override
  public void render() {
    Graphics.startShape();
    Graphics.shape.setProjectionMatrix(Camera.ui.combined);
    Graphics.shape.setColor(1, 1, 1, 1);
    Graphics.shape.rect(this.x, this.y, this.w, this.h);
    Graphics.endShape();

    Graphics.batch.setProjectionMatrix(Camera.ui.combined);

    if (this.a > 0) {
      Graphics.small.setColor(1, 1, 1, this.a);
      Graphics.small.draw(Graphics.batch, this.text, this.x + (this.w - this.w1) / 2, this.y + this.h - 4);

      if (this.extra != null) {
        Graphics.small.draw(Graphics.batch, this.extra, this.x + (this.w - this.w2) / 2, this.y + 12);
      }

      Graphics.small.setColor(1, 1, 1, 1);
    }
  }
}