package org.rexcellentgames.burningknight.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Tween;

public class Healthbar {
	private static TextureRegion frame = Graphics.getTexture("ui-bkbar-frame");
	private static TextureRegion bar = Graphics.getTexture("ui-bkbar-fill");
	private TextureRegion skull;
	public float y = Display.UI_HEIGHT;
	public boolean tweened = false;
	private float lastV;
	private float lastBV;
	private float max = 1000;
	private float sx = 1;
	private float sy = 1;
	public float targetValue = 16;
	public Boss boss;
	public boolean done;
	private float invt;

	public Healthbar() {
		this.invt = 0f;
	}

	public void update(float dt) {
		if (skull == null) {
			skull = Graphics.getTexture(this.boss.texture);
		}

		this.invt = Math.max(0, this.invt - dt);
		this.done = this.boss.isDead() && this.y >= Display.UI_HEIGHT;

		if (((int) this.lastBV) > boss.getHp()) {
			Tween.to(new Tween.Task(0.95f, 0.1f) {
				@Override
				public float getValue() {
					return sx;
				}

				@Override
				public void setValue(float value) {
					sx = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1f, 0.3f, Tween.Type.BACK_OUT) {
						@Override
						public float getValue() {
							return sx;
						}

						@Override
						public void setValue(float value) {
							sx = value;
						}
					});
				}
			});

			Tween.to(new Tween.Task(1.1f, 0.1f) {
				@Override
				public float getValue() {
					return sy;
				}

				@Override
				public void setValue(float value) {
					sy = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1f, 0.3f, Tween.Type.BACK_OUT) {
						@Override
						public float getValue() {
							return sy;
						}

						@Override
						public void setValue(float value) {
							sy = value;
						}
					});
				}
			});
			this.invt = 0.3f;
		}

		max = boss.getHpMax();
		this.lastV += (boss.getHp() - this.lastV) / 60f;
		this.lastBV += (boss.getHp() - this.lastBV) / 4f;

		boolean d = boss.isDead() || boss.getState().equals("unactive") || boss.rage;

		if (d && this.tweened) {
			tweened = false;
			Tween.to(new Tween.Task(Display.UI_HEIGHT, 0.5f) {
				@Override
				public float getValue() {
					return y;
				}

				@Override
				public void setValue(float value) {
					y = value;
				}
			});
		} else if (!d && !this.tweened) {
			tweened = true;

			Tween.to(new Tween.Task(Display.UI_HEIGHT - this.targetValue, 0.5f, Tween.Type.BACK_OUT) {
				@Override
				public float getValue() {
					return y;
				}

				@Override
				public void setValue(float value) {
					y = value;
				}
			}.delay(0.1f));
		}
	}

	public void render() {
		if (y != Display.UI_HEIGHT) {
			TextureRegion r = new TextureRegion(bar);

			Graphics.batch.setColor(0, 0, 0, 1);
			Graphics.render(r, Display.UI_WIDTH / 2, y + bar.getRegionHeight() - 1, 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);
			Graphics.batch.setColor(0.5f, 0.5f, 0.5f, 1);

			r.setRegionWidth((int) Math.ceil(this.lastV / max * bar.getRegionWidth()));
			Graphics.render(r, Display.UI_WIDTH / 2, y + bar.getRegionHeight() - 1, 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);

			Graphics.batch.setColor(1, 1, 1, 1);
			float s = this.lastBV / max * bar.getRegionWidth();

			r.setRegionWidth((int) Math.ceil(s));

			if (this.invt > 0.02f) {
				Graphics.batch.end();
				Mob.shader.begin();
				Mob.shader.setUniformf("u_a", 1f);
				Mob.shader.setUniformf("u_color", ColorUtils.WHITE);
				Mob.shader.end();
				Graphics.batch.setShader(Mob.shader);
				Graphics.batch.begin();
			}

			Graphics.render(r, Display.UI_WIDTH / 2, y + bar.getRegionHeight() - 1, 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);

			if (this.invt > 0.02f) {
				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			Graphics.render(frame, Display.UI_WIDTH / 2, y + frame.getRegionHeight() - 5, 0, frame.getRegionWidth() / 2, frame.getRegionHeight(), false, false, sx, sy);
			Graphics.render(skull, Display.UI_WIDTH / 2 - bar.getRegionWidth() / 2 + s, y + 2, 0, skull.getRegionWidth() / 2, skull.getRegionHeight() / 2, false, false, sx, sy);
		}
	}
}