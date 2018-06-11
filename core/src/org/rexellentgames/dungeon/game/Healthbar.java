package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.mob.boss.Boss;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Healthbar {
	private static TextureRegion frame = Graphics.getTexture("ui-bkbar-frame");
	private static TextureRegion bar = Graphics.getTexture("ui-bkbar-fill");
	private TextureRegion skull;
	private static TextureRegion lock = Graphics.getTexture("ui-bkbar-lock");
	private static Animation animations = Animation.make("ui-bkbar-flame");
	public float y = Display.GAME_HEIGHT;
	public boolean tweened = false;
	private float lastV;
	private float lastBV;
	private float max = 1000;
	private float sx = 1;
	private float sy = 1;
	private float last;
	public float targetValue = 16;
	public Boss boss;
	private boolean bk;
	public boolean done;

	public void update(float dt) {
		if (skull == null) {
			skull = Graphics.getTexture(this.boss.texture);
			this.bk = this.boss.texture.equals("ui-bkbar-skull");
		}

		this.last += dt;
		this.done = this.boss.isDead() && this.y >= Display.GAME_HEIGHT;

		if (((int) this.lastBV) != boss.getHp()) {
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
		}

		max = boss.getHpMax();
		this.lastV += (boss.getHp() - this.lastV) / 60f;
		this.lastBV += (boss.getHp() - this.lastBV) / 4f;

		boolean d = boss.isDead() || boss.getState().equals("unactive");

		if (d && this.tweened) {
			tweened = false;
			Tween.to(new Tween.Task(Display.GAME_HEIGHT, 0.5f) {
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

			Tween.to(new Tween.Task(Display.GAME_HEIGHT - this.targetValue, 0.5f, Tween.Type.BACK_OUT) {
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
		if (y != Display.GAME_HEIGHT) {
			TextureRegion r = new TextureRegion(bar);

			Graphics.batch.setColor(0, 0, 0, 1);
			Graphics.render(r, Display.GAME_WIDTH / 2, y + bar.getRegionHeight(), 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);
			Graphics.batch.setColor(0.5f, 0.5f, 0.5f, 1);

			r.setRegionWidth((int) Math.ceil(this.lastV / max * bar.getRegionWidth()));
			Graphics.render(r, Display.GAME_WIDTH / 2, y + bar.getRegionHeight(), 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);

			Graphics.batch.setColor(1, 1, 1, 1);

			float s = this.lastBV / max * bar.getRegionWidth();

			r.setRegionWidth((int) Math.ceil(s));
			Graphics.render(r, Display.GAME_WIDTH / 2, y + bar.getRegionHeight(), 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);

			/*for (int i = 0; i < 6; i++) {
				Graphics.render(lock, Display.GAME_WIDTH / 2 - bar.getRegionWidth() / 2 + i * lock.getRegionWidth() + lock.getRegionWidth() / 2, y + lock.getRegionHeight() / 2, 0, lock.getRegionWidth() / 2, bar.getRegionHeight() / 2, false, false);
			}*/

			Graphics.render(frame, Display.GAME_WIDTH / 2, y + frame.getRegionHeight() - 5, 0, frame.getRegionWidth() / 2, frame.getRegionHeight(), false, false, sx, sy);

			// todo: scale?
			Graphics.render(skull, Display.GAME_WIDTH / 2 - bar.getRegionWidth() / 2 + s, y + 2, 0, skull.getRegionWidth() / 2, skull.getRegionHeight() / 2, false, false, sx, sy);

			if (this.bk && this.last > 0.2f) {
				Part part = new Part();
				this.last = 0;
				part.x = Random.newFloat(r.getRegionWidth()) + Display.GAME_WIDTH / 2 - bar.getRegionWidth() / 2;
				part.y = -Random.newFloat(bar.getRegionHeight() * 1.5f) + y + bar.getRegionHeight();
				part.depth = 32;
				part.alwaysRender = true;
				part.alwaysActive = true;
				part.shadow = false;
				part.animation = animations.get("idle");
				part.vel = new Point(0, 0.6f);

				Dungeon.ui.add(part);
			}
		}
	}
}