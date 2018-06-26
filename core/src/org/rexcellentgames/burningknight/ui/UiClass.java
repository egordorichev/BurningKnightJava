package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.state.ClassSelectState;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

public class UiClass extends UiButton {
  private static String[] textures = new String[]{"ui-warrior", "ui-wizard", "ui-archer"};
  public final Player.Type type;
  private int id;
  private int ox;
  private int oy;
  private TextureRegion region;
  private String name;
  private float nw;
  private float mod = 1f;

  public UiClass(Player.Type type, int x, int y) {
    super(null, x, y);

    this.type = type;
    this.id = ClassSelectState.classes.size();
    this.ox = x;
    this.oy = y;
    this.w = 32;
    this.h = 32;    Log.info(this.type.toString());
    this.name = Locale.get(this.type.toString().toLowerCase());

    Graphics.layout.setText(Graphics.medium, this.name);
    this.nw = Graphics.layout.width;

    if (this.id == 0) {
      ClassSelectState.selected = this;
    }

    this.region = Graphics.getTexture(textures[this.id]);
  }

  @Override
  public void update(float dt) {
    float d = 80;

    double a = (((double) this.id) / ClassSelectState.classes.size() + ClassSelectState.add) * Math.PI * 2;

    this.x = (float) (this.ox + Math.cos(a) * d);
    this.y = (float) (this.oy + Math.sin(a) * d);
    this.mod = (float) (Math.cos(a) / 4 + 1f);

    super.update(dt);
  }

  @Override
  public void onClick() {
    super.onClick();

    Tween.to(new Tween.Task((float) (((double) 0 - this.id) / ClassSelectState.classes.size()), 0.5f) {
      @Override
      public float getValue() {
        return ClassSelectState.add;
      }

      @Override
      public void setValue(float value) {
        ClassSelectState.add = value;
      }
    });

    ClassSelectState.selected = this;
  }

  @Override
  public void render() {
    Graphics.batch.setProjectionMatrix(Camera.ui.combined);
    Graphics.render(region, this.x, this.y, 0, 16, 16, false, false, scale * mod, scale * mod);

    if (ClassSelectState.selected == this) {
      Graphics.print(this.name, Graphics.medium, Display.GAME_WIDTH / 2 + this.nw / 2,
        (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT * 2 + 64 + 8);
    }
  }
}