package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.rexcellentgames.burningknight.assets.Audio;

public class UiTextInput extends UiButton implements InputProcessor {
	private String lb;

	public UiTextInput(String label, int x, int y) {
		super(label, x, y);

		lb = this.label;
		org.rexcellentgames.burningknight.game.input.Input.multiplexer.addProcessor(this);
	}

	private boolean active;
	public String input = "";

	@Override
	public void destroy() {
		super.destroy();
		org.rexcellentgames.burningknight.game.input.Input.multiplexer.removeProcessor(this);
	}

	@Override
	public void onClick() {
		super.onClick();

		active = !active;

		if (active) {
			setLabel(lb + " " + input + "_");
		} else {
			setLabel(lb + " " + input);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (active) {
			if (keycode == Input.Keys.ENTER) {
				active = false;
				setLabel(lb + " " + input);
				Audio.playSfx("menu/select");
			} else if (keycode == Input.Keys.BACKSPACE && input.length() > 0) {
				this.input = this.input.substring(0, this.input.length() - 1);
				setLabel(lb + " " + input + "_");
				Audio.playSfx("menu/moving");
			} else if (keycode == Input.Keys.V && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				int len = getMaxLength();

				if (len != -1 && input.length() >= len) {
					return false;
				}

				String str = Gdx.app.getClipboard().getContents();

				for (int i = 0; i < str.length(); i++) {
					if (validate(str.charAt(i)) == '\0') {
						return false;
					}
				}

				if (len != -1 && str.length() + input.length() > len) {
					str = str.substring(0, len - (str.length() + input.length()));
				}

				input += str;
				setLabel(lb + " " + input + "_");
				Audio.playSfx("menu/moving");
			}
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	public char validate(char ch) {
		return UiInput.isPrintableChar(ch) ? ch : '\0';
	}

	@Override
	public boolean keyTyped(char character) {
		if (active && validate(character) != '\0') {
			int len = getMaxLength();

			if (len != -1 && input.length() >= len) {
				return false;
			}

			input += validate(character);
			setLabel(lb + " " + input + "_");
			Audio.playSfx("menu/moving");
		}

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public int getMaxLength() {
		return -1;
	}
}