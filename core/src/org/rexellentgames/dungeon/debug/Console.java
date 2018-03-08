package org.rexellentgames.dungeon.debug;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Console implements InputProcessor {
	private String input = "";
	private boolean open;
	private ArrayList<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();

	public Console() {
		org.rexellentgames.dungeon.game.input.Input.multiplexer.addProcessor(this);

		this.commands.add(new HelpCommand());
		this.commands.add(new GiveCommand());
		this.commands.add(new HealCommand());
		this.commands.add(new GodModeCommand());
		this.commands.add(new GenerateCommand());
		this.commands.add(new LevelCommand());
	}

	public void destroy() {
		org.rexellentgames.dungeon.game.input.Input.multiplexer.removeProcessor(this);
	}

	public void update(float dt) {

	}

	public void render() {
		if (this.open) {
			Graphics.small.draw(Graphics.batch, this.input + "|", 2, 8);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (!this.open && keycode == Input.Keys.SLASH) {
			this.open = true;
			org.rexellentgames.dungeon.game.input.Input.instance.blocked = true;
		} else if (keycode == Input.Keys.ESCAPE) {
			this.open = false;
			org.rexellentgames.dungeon.game.input.Input.instance.blocked = false;
		} else if (keycode == Input.Keys.ENTER && this.open) {
			String string = this.input;
			this.input = "";
			this.open = false;
			org.rexellentgames.dungeon.game.input.Input.instance.blocked = false;
			this.runCommand(string);
		} else if (keycode == Input.Keys.BACKSPACE && this.open && this.input.length() > 0) {
			this.input = this.input.substring(0, this.input.length() - 1);
		}

		return false;
	}

	private void runCommand(String input) {
		String[] parts = input.split("\\s+");
		String name = parts[0];

		for (ConsoleCommand command : this.commands) {
			if (command.getName().equals(name) || command.getShortName().equals(name)) {
				String[] args = new String[parts.length - 1];

				for (int i = 0; i < args.length; i++) {
					args[i] = parts[i + 1];
				}

				command.run(this, args);

				return;
			}
		}

		UiLog.instance.print("Unknown command '" + input + "'");
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	private boolean isPrintableChar(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);

		return (!Character.isISOControl(c)) &&
			c != KeyEvent.CHAR_UNDEFINED &&
			block != null &&
			block != Character.UnicodeBlock.SPECIALS;
	}

	@Override
	public boolean keyTyped(char character) {
		if (this.open && this.isPrintableChar(character)) {
			this.input += character;
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

	public ArrayList<ConsoleCommand> getCommands() {
		return this.commands;
	}
}