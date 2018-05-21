package org.rexellentgames.dungeon.entity.level.entities.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.level.entities.MagicWell;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Dialog;
import org.rexellentgames.dungeon.util.Tween;

public class WellFx extends Entity {
	private MagicWell well;
	private String text;
	private float a;

	{
		depth = 30;
	}

	public WellFx(MagicWell well, String text) {
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
			this.remove();
			this.well.use();
		}
	}

	public void remove() {
		WellFx self = this;

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

				self.done = true;
			}
		});
	}
}