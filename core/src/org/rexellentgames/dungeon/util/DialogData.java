package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.input.Input;

import java.util.ArrayList;

public class DialogData {
	public ArrayList<Dialog.Phrase> phrases = new ArrayList<>();
	private int current;
	private TypingLabel label;
	private static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	private float a = 1;
	private int selected;
	private float oa;
	private Runnable fin = () -> {};
	private Runnable stop = () -> {};
	private Runnable start = () -> {};
	private float size;
	private float w;

	public void onEnd(Runnable onEnd) {
		this.fin = onEnd;
	}

	public void onStop(Runnable onStop) {
		this.stop = onStop;
	}

	public void onStart(Runnable onStart) {
		this.start = onStart;
	}

	private static Color color = Color.valueOf("#2a2f4e");

	public void render() {
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.startShape();

		if (size > 0) {
			Graphics.shape.setColor(0, 0, 0, 1);
			Graphics.shape.rect(0, 0, Display.GAME_WIDTH, size);
			Graphics.shape.rect(0, Display.GAME_HEIGHT - size, Display.GAME_WIDTH, size);
		}

		int y = Display.GAME_HEIGHT - 52 - 16 - 48;

		Graphics.shape.setColor(color.r, color.g, color.b, 1);
		Graphics.shape.rect((Display.GAME_WIDTH - this.w) / 2, y, this.w, 48);
		Graphics.endShape();

		if (this.label != null) {
			this.label.draw(Graphics.batch, this.a);
		}
	}

	public void start() {
		this.current = 0;
		this.w = 0;
		this.selected = 0;

		Tween.to(new Tween.Task(Display.GAME_WIDTH - 32,  1f) {
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
			}
		});

		Tween.to(new Tween.Task(52, 0.4f) {
			@Override
			public float getValue() {
				return size;
			}

			@Override
			public void setValue(float value) {
				size = value;
			}
		});
	}

	public void next() {
		this.selected = 0;

		Dialog.Phrase phrase = this.phrases.get(this.current);
		this.label = new TypingLabel("{COLOR=#FFFFFF}" + phrase.string, skin);
		this.label.setSize(Display.GAME_WIDTH - 96 - 16, 32);
		this.label.setPosition(32, Display.GAME_HEIGHT - 52 - 16 - 48 + 8);
		this.label.setWrap(true);
		this.start.run();

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
		Dialog.Phrase phrase = this.phrases.get(this.current);

		if (phrase.options == null) {
			this.toNext();
		} else if (this.label != null && !this.label.hasEnded()) {
			this.label.skipToTheEnd();
		} else {
			this.toNext();
		}
	}

	public void update(float dt) {
		if (this.label == null) {
			return;
		}

		this.label.act(dt);

		Dialog.Phrase phrase = this.phrases.get(this.current);

		if (phrase.options != null) {
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
		Dialog.Phrase phrase = phrases.get(current);

		if (this.oa != 1f && phrase.options != null) {
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
							return;
						}
					}
				}
			}
		});
	}

	public void end() {
		stop.run();

		Tween.to(new Tween.Task(0, 0.4f) {
			@Override
			public float getValue() {
				return size;
			}

			@Override
			public void setValue(float value) {
				size = value;
			}
		});


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
						Dialog.active = null;

						if (fin != null) {
							fin.run();
						}
					}
				});
			}
		});
	}
}