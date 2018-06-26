package org.rexcellentgames.burningknight.entity.level.entities.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.entities.UsableProp;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class WellFx extends Entity {
  private UsableProp well;
  private String text;
  private float a;

  {
    depth = 30;
  }

  public WellFx(UsableProp well, String text) {
    this.well = well;
    this.text = Locale.get(text);

    GlyphLayout layout = new GlyphLayout(Graphics.medium, this.text);

    this.x = well.x + 16 - layout.width / 2;
    this.y = well.y + well.h;

    this.depth = 15;

    Tween.to(new Tween.Task(1, 0.1f, Tween.Type.QUAD_OUT) {
      @Override
      public float getValue() {
        return a;
      }

      @Override
      public void setValue(float value) {
        a = value;
      }
    });
  }

  @Override
  public void render() {
    float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

    Graphics.medium.setColor(c, c, c, this.a);
    Graphics.print(this.text, Graphics.medium, this.x, this.y);
    Graphics.medium.setColor(1, 1, 1, 1);

    if (Input.instance.wasPressed("action") && Dialog.active == null) {
      if (this.well.use()) {
        this.remove();
      }
    }
  }

  public void remove() {
    Tween.to(new Tween.Task(0, 0.2f, Tween.Type.QUAD_IN) {
      @Override
      public float getValue() {
        return a;
      }

      @Override
      public void setValue(float value) {
        a = value;
      }

      @Override
      public void onEnd() {
        super.onEnd();
        setDone(true);
      }
    });
  }
}