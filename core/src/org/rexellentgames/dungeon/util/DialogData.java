package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;

import java.util.ArrayList;

public class DialogData {
	public ArrayList<Dialog.Phrase> phrases = new ArrayList<>();
	private int current;
	private TypingLabel label;
	private static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
	private float delay = -1f;
	private static TextureRegion frame = Graphics.getTexture("dialog");
	private float a;

	public void start() {
		this.current = 0;

		Tween.to(new Tween.Task(1f, 0.3f) {
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
				next();
			}
		});
	}

	public void next() {
		Dialog.Phrase phrase = this.phrases.get(this.current);
		this.label = new TypingLabel("{SPEED=0.4}{COLOR=#FFFFFF}" + phrase.string, skin);
		this.label.setSize(Display.GAME_WIDTH - 96 - 16, 64);
		this.label.setPosition(80, Display.GAME_HEIGHT - 64 - 32);
		this.label.setWrap(true);

		this.label.setTypingListener(new TypingListener() {
			@Override
			public void event(String event) {

			}

			@Override
			public void end() {
				delay = 1f;
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

	public void update(float dt) {
		if (this.label == null) {
			return;
		}

		this.label.act(dt);

		if (this.delay != -1f) {
			this.delay -= dt;

			if (this.delay <= 0f) {
				this.delay = -1f;

				if (current == phrases.size() - 1) {
					Tween.to(new Tween.Task(0f, 0.3f) {
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
							Dialog.active = null;
						}
					});
				} else {
					current += 1;
					next();
				}
			}
		}
	}

	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		Graphics.render(frame, 16, Display.GAME_HEIGHT - 64 - 32);
		Graphics.render(this.phrases.get(this.current).region, 32, Display.GAME_HEIGHT - 64 - 16);
		Graphics.batch.setColor(1, 1, 1, 1);

		if (this.label != null) {
			this.label.draw(Graphics.batch, this.a);
		}
	}
}