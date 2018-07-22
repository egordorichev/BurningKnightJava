package org.rexcellentgames.burningknight.entity.creature.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Tween;

public class UiBuff {
	public float x;
	public float y;
	public Buff buff;
	public Player owner;

	private float scale;

	public UiBuff() {
		Tween.to(new Tween.Task(1f, 0.4f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return scale;
			}

			@Override
			public void setValue(float value) {
				scale = value;
			}
		});
	}

	private boolean done;

	public void remove() {
		if (done) {
			return;
		}

		done = true;
		final UiBuff self = this;

		Tween.to(new Tween.Task(0f, 0.4f) {
			@Override
			public float getValue() {
				return scale;
			}

			@Override
			public void setValue(float value) {
				scale = value;
			}

			@Override
			public void onEnd() {
				owner.uiBuffs.remove(self);
			}
		});
	}

	private boolean hover;

	public void render(int i, float y) {
		int x = 4 + i * 12;
		y = y + 9 + 11;

		boolean h = this.hover;
		boolean hover = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, x, (int) y, 8, 8);
		this.hover = hover;

		if (hover && !h) {
			Tween.to(new Tween.Task(1.4f, 0.2f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});
		} else if (!hover && h) {
			Tween.to(new Tween.Task(1f, 0.2f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});
		}

		if (this.hover) {
			Player.instance.ui.hoveredBuff = this;
		}

		TextureRegion sprite = this.buff.getSprite();
		Graphics.render(sprite, x + sprite.getRegionWidth() / 2, y + sprite.getRegionHeight() / 2, 0, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, scale, scale);
	}

	public String getInfo() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.buff.getName());
		builder.append("[gray]\n");
		builder.append(this.buff.getDescription());
		builder.append('\n');

		if (this.buff.infinite) {
			builder.append("Infinite");
		} else {
			builder.append(Math.max(0, Math.floor(this.buff.getDuration() - this.buff.getTime())));
			builder.append(" seconds left");
		}

		return builder.toString();
	}
}