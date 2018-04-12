package org.rexellentgames.dungeon.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LabelButton extends TextButton {
  private LabelButtonStyle style;
  
  public LabelButton(String text, Skin skin) {
    super(text, skin.get(LabelButtonStyle.class));

    style = skin.get(LabelButtonStyle.class);
    
    setSkin(skin);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
  }
  
  // This is used in skin configuration json
  public static class LabelButtonStyle extends TextButtonStyle {
    public LabelButtonStyle() {
    }
    
    public LabelButtonStyle(BitmapFont font, Color fontColor, Color downFontColor, Color overFontColor, Color disabledFontColor) {
      super(null, null, null, font);
      this.font = font;
      this.fontColor = fontColor;
      this.downFontColor = downFontColor;
      this.overFontColor = overFontColor;
      this.disabledFontColor = disabledFontColor;
    }
  }
}
