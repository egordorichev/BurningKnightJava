package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
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

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.setColor(1, 1, 1, 1);

		float x = this.npc.x + this.npc.w / 2 + this.x;
		float y = this.npc.y + this.npc.h + 8;
		Graphics.shape.rect(x - this.w / 2,
			y, this.w, this.h);

		Graphics.shape.triangle(x - 2, y, x + 2, y, x, y - Math.min(this.h / 4f, 4));
		Graphics.endShape();

		if (this.a > 0) {
			Graphics.small.setColor(1, 1, 1, this.a);
			Graphics.small.draw(Graphics.batch, this.message, this.npc.x + this.npc.w / 2 + this.x - this.w / 2 + 6,
				this.npc.y + this.npc.h + 8 + this.h - 8);
			Graphics.small.setColor(1, 1, 1, 1);
		}
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

						if (message.length() != full.length()) {
							open();
						}
					}
				}
			});
		}
	}

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