package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class NpcDialog extends Entity {
	private Npc npc;
	private String message;
	private String full;

	{
		depth = 14;
		alwaysActive = true;
		alwaysRender = true;
	}

	public NpcDialog(Npc npc, String message) {
		this.npc = npc;
		this.w = 4f;
		this.h = 0;
		this.setMessage(message);
	}

	public void setMessage(String message) {
		this.full = message;
		this.message = "";
		this.open();
	}

	// private static Color color = Color.valueOf("#0e071b");

	private TextureRegion top = Graphics.getTexture("bubble-top");
	private TextureRegion topLeft = Graphics.getTexture("bubble-top_left");
	private TextureRegion topRight = Graphics.getTexture("bubble-top_right");
	private TextureRegion center = Graphics.getTexture("bubble-center");
	private TextureRegion left = Graphics.getTexture("bubble-left");
	private TextureRegion right = Graphics.getTexture("bubble-right");
	private TextureRegion bottom = Graphics.getTexture("bubble-bottom");
	private TextureRegion bottomLeft = Graphics.getTexture("bubble-bottom_left");
	private TextureRegion bottomRight = Graphics.getTexture("bubble-bottom_right");

	@Override
	public void render() {
		float x = Math.round(this.npc.x + this.npc.w / 2 + this.x);
		float y = Math.round(this.npc.y + this.npc.h + 8);

		float sx = (this.w - topLeft.getRegionWidth() * 2) / ((float) top.getRegionWidth());
		float sy = (this.h - left.getRegionHeight()) / ((float) left.getRegionHeight());

		Graphics.render(top, x - this.w / 2 + topLeft.getRegionWidth(), y + this.h / 2 - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topLeft, x - this.w / 2, y + this.h / 2 - topLeft.getRegionHeight());
		Graphics.render(topRight, x + this.w / 2 - topRight.getRegionWidth(), y + this.h / 2 - topRight.getRegionHeight());

		Graphics.render(left, x - this.w / 2, y - this.h / 2 + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(right, x + this.w / 2 - right.getRegionWidth(), y - this.h / 2 + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);

		Graphics.render(center, x - this.w / 2 + left.getRegionWidth(), y - this.h / 2 + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, sx, sy);

		Graphics.render(bottom, x - this.w / 2 + bottomLeft.getRegionWidth(), y - this.h / 2, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomLeft, x - this.w / 2, y - this.h / 2);
		Graphics.render(bottomRight, x + this.w / 2 - topRight.getRegionWidth(), y - this.h / 2);

		/*Graphics.startShape();
		Graphics.shape.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.setColor(color);

		Graphics.shape.rect(x - this.w / 2 - 1,
			y - 1, this.w + 2, this.h + 2);

		Graphics.shape.triangle(x - 3, y, x + 3, y, x, y - Math.min(this.h / 4f, 4) - 1);

		Graphics.shape.setColor(1, 1, 1, 1);

		Graphics.shape.rect(x - this.w / 2,
			y, this.w, this.h);

		Graphics.shape.triangle(x - 2, y, x + 2, y, x, y - Math.min(this.h / 4f, 4));
		Graphics.endShape();

		if (this.a > 0) {
			Graphics.small.setColor(1, 1, 1, this.a);
			Graphics.write(this.message, Graphics.small, this.npc.x + this.npc.w / 2 + this.x - this.w / 2 + 6,
				this.npc.y + this.npc.h + this.h - 8);
			Graphics.small.setColor(1, 1, 1, 1);
		}*/
	}

	private Tween.Task last;
	private float a;

	public void open() {
		a = 1;
		toRemove = false;

		if (this.last != null) {
			Tween.remove(this.last);
			this.last = null;
		}

		if (this.h < 16) {
			Tween.to(new Tween.Task(Graphics.small.getLineHeight() + 12, 0.2f) {
				@Override
				public float getValue() {
					return h;
				}

				@Override
				public void setValue(float value) {
					h = value;
				}

				@Override
				public void onEnd() {
					if (toRemove) {
						return;
					}

					open();
				}
			});
		} else {
			if (toRemove) {
				return;
			}

			Graphics.layout.setText(Graphics.small, this.message);

			if (Graphics.layout.height > this.h - 16) {
				Tween.to(new Tween.Task(Graphics.layout.height + 16, 0.1f) {
					@Override
					public float getValue() {
						return h;
					}

					@Override
					public void setValue(float value) {
						h = value;
					}
				});
			}

			Tween.to(new Tween.Task(Graphics.layout.width + 16, 0.05f, Tween.Type.LINEAR) {
				@Override
				public float getValue() {
					return w;
				}

				@Override
				public void setValue(float value) {
					w = value;
				}

				@Override
				public void onEnd() {
					if (toRemove) {
						return;
					}

					if (message.length() != full.length()) {
						message = full.substring(0, message.length() + 1);

						if ((message.length() == full.length() && !did) || message.length() < full.length()) {
							if (message.length() == full.length()) {
								did = true;
							}

							open();
						}
					}
				}
			});
		}
	}

	private boolean did;

	private boolean toRemove;

	public void remove() {
		toRemove = true;

		if (this.last != null) {
			Tween.remove(this.last);
			this.last = null;
		}

		this.last = Tween.to(new Tween.Task(0, 0.2f) {
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
				last = Tween.to(new Tween.Task(4f, 0.4f) {
					@Override
					public float getValue() {
						return w;
					}

					@Override
					public void setValue(float value) {
						w = value;
					}

					@Override
					public void onEnd() {
						last = Tween.to(new Tween.Task(0, 0.2f) {
							@Override
							public float getValue() {
								return h;
							}

							@Override
							public void setValue(float value) {
								h = value;
							}

							@Override
							public void onEnd() {
								setDone(true);
							}
						});
					}
				});
			}
		});
	}
}