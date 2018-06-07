package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.Tween;

import static org.rexellentgames.dungeon.entity.creature.buff.fx.FlameFx.orange;

public class HpFx extends Entity {
	public static Color bad = Color.valueOf("#ac3232");
	public static Color good = Color.valueOf("#99e550");

	private String text;
	private boolean low;
	private float a = 1f;
	public boolean crit;
	public boolean block;

	public HpFx(Creature creature, int change) {
		this.text = String.valueOf(Math.abs(change));

		GlyphLayout layout = new GlyphLayout(Graphics.small, this.text);

		if (this.block) {
			this.x = creature.x + (creature.w) / 2;
		} else {
			this.x = creature.x + (creature.w - layout.width) / 2 + 1;
		}

		this.y = creature.y + creature.h - 4;
		this.low = change < 0;
		this.depth = 15;

		Tween.to(new Tween.Task(this.y + creature.h * 1.5f, 0.5f, Tween.Type.BACK_OUT) {
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
				Tween.to(new Tween.Task(0, 0.3f, Tween.Type.QUAD_IN) {
					@Override
					public void setValue(float value) {
						a = value;
					}

					@Override
					public float getValue() {
						return a;
					}

					@Override
					public void onEnd() {
						done = true;
					}
				});
			}
		});
	}

	private static TextureRegion blockTexture = Graphics.getTexture("ui (block)");

	@Override
	public void render() {
		float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

		if (this.block) {
			Graphics.batch.setColor(c, c, c, this.a);
			Graphics.render(blockTexture, this.x - blockTexture.getRegionWidth() / 2, this.y - blockTexture.getRegionHeight() / 2);
			Graphics.batch.setColor(1, 1, 1, 1);
		} else {
			Color color = this.low ? (this.crit ? orange : bad) : good;

			Graphics.small.setColor(color.r * c, color.g * c, color.b * c, this.a);
			Graphics.small.draw(Graphics.batch, this.text, this.x, this.y);
			Graphics.small.setColor(1, 1, 1, 1);
		}
	}
}