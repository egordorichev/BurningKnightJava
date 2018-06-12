package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.CollisionHelper;

import java.awt.event.KeyEvent;

public class UiInput extends UiEntity implements InputProcessor {
	private String input = "";
	private String placeholder = "";
	private boolean open = false;

	public int w = 64;
	public int h = 12;
	private int pw = 0;

	public String getInput() {
		return this.input;
	}

	@Override
	public void init() {
		org.rexellentgames.dungeon.game.input.Input.multiplexer.addProcessor(this);
		this.calcPW();
	}

	private void calcPW() {
		Graphics.layout.setText(Graphics.medium, this.placeholder);
		this.pw = (int) Graphics.layout.width;
		this.w = this.pw;
		this.x = (Display.GAME_WIDTH - this.pw) / 2;
	}

	private void calcW() {
		Graphics.layout.setText(Graphics.medium, this.input);
		this.w = (int) Graphics.layout.width;
		this.x = (Display.GAME_WIDTH - this.w) / 2;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public void render() {
		String str = this.input;

		if (!this.open && str.isEmpty()) {
			str = this.placeholder;
			Graphics.medium.setColor(0.7f, 0.7f, 0.7f, 1f);
		}

		Graphics.medium.draw(Graphics.batch, this.open ? str + "|" : str, this.x, this.y + 12);
		Graphics.medium.setColor(1, 1, 1, 1);
	}

	public void onEnter(String input) {

	}

	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ENTER && this.open) {
			this.onEnter(this.input);
			this.open = false;

			if (this.input.isEmpty()) {
				this.calcPW();
			} else {
				this.calcW();
			}
		} else if (keycode == Input.Keys.BACKSPACE && this.open && this.input.length() > 0) {
			this.input = this.input.substring(0, this.input.length() - 1);
			this.calcW();
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (this.open && isPrintableChar(character)) {
			this.input += character;
			this.calcW();
		}

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean was = this.open;

		Vector2 mouse = org.rexellentgames.dungeon.game.input.Input.instance.uiMouse;
		this.open = (CollisionHelper.check((int) mouse.x, (int) mouse.y, (int) this.x, (int) this.y, this.w, this.h));

		if (was && !this.open) {
			if (this.input.isEmpty()) {
				this.calcPW();
			} else {
				this.calcW();
			}
		} else if (!was && this.open) {
			this.calcW();
		}

		return this.open;
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

	public static boolean isPrintableChar(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);

		return (!Character.isISOControl(c)) &&
			c != KeyEvent.CHAR_UNDEFINED &&
			block != null &&
			block != Character.UnicodeBlock.SPECIALS;
	}
}