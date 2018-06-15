package org.rexcellentgames.burningknight.debug;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.ui.UiInput;

import java.util.ArrayList;

public class Console implements InputProcessor {
	private String input = "";
	private boolean open;
	public static Console instance;
	private ArrayList<ConsoleCommand> commands = new ArrayList<>();

	public Console() {
		instance = this;

		org.rexcellentgames.burningknight.game.input.Input.multiplexer.addProcessor(this);

		this.commands.add(new HelpCommand());
		this.commands.add(new GiveCommand());
		this.commands.add(new HealCommand());
		this.commands.add(new GodModeCommand());
		this.commands.add(new ResetCommand());
		this.commands.add(new LevelCommand());
		this.commands.add(new LightCommand());
		this.commands.add(new DebugCommand());
		this.commands.add(new ArcadeCommand());
		this.commands.add(new ShadowCommand());
		this.commands.add(new DieCommand());
		this.commands.add(new PassableCommand());
	}

	public void destroy() {
		org.rexcellentgames.burningknight.game.input.Input.multiplexer.removeProcessor(this);
	}

	public void update(float dt) {

	}

	public void render() {
		if (this.open) {
			Graphics.print(this.input + "|", Graphics.medium, 2, 20);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			this.open = !this.open;
			org.rexcellentgames.burningknight.game.input.Input.instance.blocked = this.open;
		} else if (keycode == Input.Keys.ENTER && this.open) {
			String string = this.input;
			this.input = "";
			this.open = false;
			org.rexcellentgames.burningknight.game.input.Input.instance.blocked = false;
			this.runCommand(string);
		} else if (keycode == Input.Keys.BACKSPACE && this.open && this.input.length() > 0) {
			this.input = this.input.substring(0, this.input.length() - 1);
		}

		return false;
	}

	public void runCommand(String input) {
		if (!input.startsWith("/")) {
			String string = Player.instance.getName() + ": " + input;

			return;
		}

		String[] parts = input.split("\\s+");
		String name = parts[0];

		for (ConsoleCommand command : this.commands) {
			if (command.getName().equals(name) || command.getShortName().equals(name)) {
				String[] args = new String[parts.length - 1];

				System.arraycopy(parts, 1, args, 0, args.length);

				command.run(this, args);

				return;
			}
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (this.open && UiInput.isPrintableChar(character)) {
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