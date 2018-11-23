package org.rexcellentgames.burningknight.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogData {
	public ArrayList<Dialog.Phrase> phrases = new ArrayList<>();
	private int current;
	private TypingLabel label;
	private static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	private float a = 1;
	private int selected;
	private float oa;
	private Runnable fin = new Runnable() {
		@Override
		public void run() {
		}
	};

	private Runnable stop = new Runnable() {
		@Override
		public void run() {
		}
	};

	private Runnable start = new Runnable() {
		@Override
		public void run() {
		}
	};

	private Runnable onSelect = new Runnable() {
		@Override
		public void run() {
		}
	};

	private float size;
	private float w;
	private float h = 48;

	public void onEnd(Runnable onEnd) {
		this.fin = onEnd;
	}

	public void onStop(Runnable onStop) {
		this.stop = onStop;
	}

	public void onStart(Runnable onStart) {
		this.start = onStart;
	}

	public void onSelect(Runnable onStart) {
		this.onSelect = onStart;
	}

	private TextureRegion top = Graphics.getTexture("ui-dialog_top");
	private TextureRegion topLeft = Graphics.getTexture("ui-dialog_top_left");
	private TextureRegion topRight = Graphics.getTexture("ui-dialog_top_right");
	private TextureRegion center = Graphics.getTexture("ui-dialog_center");
	private TextureRegion left = Graphics.getTexture("ui-dialog_left");
	private TextureRegion right = Graphics.getTexture("ui-dialog_right");
	private TextureRegion bottom = Graphics.getTexture("ui-dialog_bottom");
	private TextureRegion bottomLeft = Graphics.getTexture("ui-dialog_bottom_left");
	private TextureRegion bottomRight = Graphics.getTexture("ui-dialog_bottom_right");

	private TextureRegion optionsCenter = Graphics.getTexture("ui-option_center");
	private TextureRegion optionsLeft = Graphics.getTexture("ui-option_left");
	private TextureRegion optionsRight = Graphics.getTexture("ui-option_right");
	private TextureRegion optionsBottom = Graphics.getTexture("ui-option_bottom");
	private TextureRegion optionsBottomLeft = Graphics.getTexture("ui-option_bottom_left");
	private TextureRegion optionsBottomRight = Graphics.getTexture("ui-option_bottom_right");

	public void render() {
		if (size > 0) {
			Graphics.shape.setProjectionMatrix(Camera.ui.combined);
			Graphics.startShape();
			Graphics.shape.setColor(0, 0, 0, 1);
			Graphics.shape.rect(0, 0, Display.UI_WIDTH, size);
			Graphics.shape.rect(0, Display.UI_HEIGHT - size, Display.UI_WIDTH, size);
			Graphics.endShape();
		}

		int x = (int) ((Display.UI_WIDTH - this.w) / 2);

		float sx = (this.w - 8);

		if (this.label != null) {
			if (this.optionsH > 0) {
				Dialog.Phrase phrase = this.phrases.get(this.current);

				if (phrase.options != null && this.label.hasEnded()) {
					float sy = (this.optionsH - 4);
					int y = (int) (Display.UI_HEIGHT - 52 - 16 - this.h - optionsH);


					Graphics.render(optionsLeft, x, y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
					Graphics.render(optionsRight, x + this.w - right.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
					Graphics.render(optionsCenter, x + left.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, sx, sy);

					Graphics.render(optionsBottom, x+ bottomLeft.getRegionWidth(),
						y, 0, 0, 0, false, false, sx, 1);
					Graphics.render(optionsBottomLeft, x, y);
					Graphics.render(optionsBottomRight, x + this.w - topRight.getRegionWidth(), y);

					if (this.optionsA > 0) {
						for (int i = 0; i < phrase.options.length; i++) {
							Dialog.Option option = phrase.options[i];
							String str = option.string;
							boolean sl = i == selected;

							float tar = 1;

							if (sl) {
								str += " <";
								float c = (float) (0.6f + Math.cos(Dungeon.time * 4) / 3f);

								tar = c * 0.8f;
							}

							float dt = Gdx.graphics.getDeltaTime();
							option.c += (tar - option.c) * dt * 20;
							option.x += ((sl ? 4 : 0) - option.x) * dt * 20;

							Graphics.small.setColor(option.c, option.c, option.c, this.optionsA);

							Graphics.small.draw(Graphics.batch, str, x + option.x + 10, y - (i + 1) * 10 + optionsH + 4);
							Graphics.small.setColor(1, 1, 1, 1);
						}
					}
				}
			}
		}

		float sy = (this.h - 9);
		int y = (int) (Display.UI_HEIGHT - 52 - 16 - this.h);

		Graphics.render(top, x + topLeft.getRegionWidth(), y + this.h - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topLeft, x, y + this.h - topLeft.getRegionHeight());
		Graphics.render(topRight, x + this.w - topRight.getRegionWidth(), y + this.h - topRight.getRegionHeight());

		Graphics.render(left, x, y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(right, x + this.w - right.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(center, x + left.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, sx, sy);

		Graphics.render(bottom, x+ bottomLeft.getRegionWidth(),
			y, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomLeft, x, y);
		Graphics.render(bottomRight, x + this.w - topRight.getRegionWidth(), y);

		if (this.label != null) {
			this.label.draw(Graphics.batch, this.a);

			Graphics.print(this.phrases.get(this.current).owner, Graphics.small, x + 4, y + this.h + 3);
		}
	}

	private boolean mapWasOpen;
	private boolean mapWasLarge;


	public void start() {
		mapWasOpen = UiMap.instance.isOpen();
		mapWasLarge = UiMap.large;

		if (mapWasOpen) {
			if (mapWasLarge) {
				UiMap.instance.hideHuge();
			} else {
				UiMap.instance.hide();
			}
		}

		this.label = null;
		this.current = 0;
		this.a = 1;
		this.w = 0;
		this.selected = 0;

		Tween.to(new Tween.Task(Display.UI_WIDTH - 32,  0.4f) {
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
				next();
				busy = false;
			}
		});

		Tween.to(new Tween.Task(52, 0.1f) {
			@Override
			public float getValue() {
				return size;
			}

			@Override
			public void setValue(float value) {
				size = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();
			}
		});

		busy = true;
	}

	private boolean busy;

	private void readPhrase() {
		Dialog.Phrase phrase = this.phrases.get(this.current);
		this.label = new TypingLabel("{COLOR=#FFFFFF}{SPEED=0.75}" + phrase.string, skin);
		this.label.setSize(Display.UI_WIDTH - 64, 32);
		this.label.setPosition(32, Display.UI_HEIGHT - 52 - 16 - 48 + 8);
		this.label.setWrap(true);

		int deaths = GlobalSave.getInt("deaths");

		this.label.setVariable("deaths", deaths == 1 ? "1 time" : deaths + " times");

		for (Map.Entry<String, String> pair : vars.entrySet()) {
			this.label.setVariable(pair.getKey(), pair.getValue());
		}

		this.start.run();
	}

	private HashMap<String, String> vars = new HashMap<>();

	public void setVariable(String var, String val) {
		vars.put(var, val);
	}

	public void next() {
		this.selected = 0;
		readPhrase();

		this.label.setTypingListener(new TypingListener() {
			@Override
			public void event(String event) {

			}

			@Override
			public void end() {
				stop.run();

				Tween.to(new Tween.Task(1f, 0.2f) {
					@Override
					public float getValue() {
						return oa;
					}

					@Override
					public void setValue(float value) {
						oa = value;
					}
				});

				showOptions();
			}

			@Override
			public String replaceVariable(String variable) {
				return null;
			}

			@Override
			public void onChar(Character ch) {

			}
		});
	}

	public void skip() {
		if (busy) {
			return;
		}

		if (this.label != null && !this.label.hasEnded()) {
			this.label.skipToTheEnd();
			this.showOptions();
		} else {
			this.toNext();
		}
	}

	private float optionsH;
	private float optionsA;

	private void showOptions() {
		Dialog.Phrase phrase = this.phrases.get(this.current);

		if (phrase != null && phrase.options != null) {
			Tween.to(new Tween.Task((phrase.options.length + 1) * 10 + 4, 0.3f) {
				@Override
				public float getValue() {
					return optionsH;
				}

				@Override
				public void setValue(float value) {
					optionsH = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1, 0.1f) {
						@Override
						public float getValue() {
							return optionsA;
						}

						@Override
						public void setValue(float value) {
							optionsA = value;
						}
					});
				}
			});
		}
	}

	public void update(float dt) {
		if (this.label == null) {
			return;
		}

		this.label.act(dt);

		Dialog.Phrase phrase = this.phrases.get(this.current);

		if (phrase.options != null && this.label.hasEnded() && this.optionsA == 1) {
			int next = this.selected;

			if (Input.instance.wasPressed("left") ||
				Input.instance.wasPressed("up") || Input.instance.wasPressed("prev")) {

				next -= 1;

				if (next <= -1) {
					next += phrase.next.length;
				}
			}

			if (Input.instance.wasPressed("down") ||
				Input.instance.wasPressed("right") || Input.instance.wasPressed("next")) {

				next = (next + 1) % phrase.next.length;
			}

			if (next != this.selected) {
				this.selected = next;
			}
		}
	}

	public void toNext() {
		if (this.optionsA != 1) {
			Dialog.Phrase phrase = this.phrases.get(this.current);

			if (phrase.options == null) {
				if (phrase.next == null) {
					end();
				} else {
					String next = phrase.next[0];

					for (int i = 0; i < phrases.size(); i++) {
						Dialog.Phrase p = phrases.get(i);

						if (p != phrase && p.name.equals(next)) {
							current = i;
							next();
						}
					}
				}
			}

			return;
		}

		Tween.to(new Tween.Task(0, 0.1f) {
			@Override
			public float getValue() {
				return optionsA;
			}

			@Override
			public void setValue(float value) {
				optionsA = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return optionsH;
					}

					@Override
					public void setValue(float value) {
						optionsH = value;
					}

					@Override
					public void onEnd() {
						onSelect.run();

						final Dialog.Phrase phrase = phrases.get(current);

						if (oa != 1f && phrase.options != null) {
							return;
						}

						Tween.to(new Tween.Task(0f, 0.2f) {
							@Override
							public float getValue() {
								return oa;
							}

							@Override
							public void setValue(float value) {
								oa = value;
							}

							@Override
							public void onEnd() {
								if (phrase.options == null || phrase.next == null) {
									end();
								} else {
									String next = phrase.next[Math.min(phrase.next.length, selected)];

									for (int i = 0; i < phrases.size(); i++) {
										Dialog.Phrase p = phrases.get(i);

										if (p != phrase && p.name.equals(next)) {
											current = i;
											next();
										}
									}
								}
							}
						});
					}
				});
			}
		});
	}

	public int getSelected() {
		return selected;
	}

	public void end() {
		stop.run();

		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return size;
			}

			@Override
			public void setValue(float value) {
				size = value;
			}
		});

		busy = true;

		Tween.to(new Tween.Task(0, 0.2f) {
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
				Tween.to(new Tween.Task(0, 0.3f) {
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
						busy = false;
						Dialog.active = null;

						if (label != null) {
							label.remove();
							label = null;
						}

						if (mapWasOpen) {
							if (mapWasLarge) {
								UiMap.instance.openHuge();
							} else {
								UiMap.instance.show();
							}
						}

						if (fin != null) {
							fin.run();
						}
					}
				});
			}
		});
	}
}