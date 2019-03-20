package org.rexcellentgames.burningknight.debug;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.ui.UiInput;

import java.util.ArrayList;

public class Console implements InputProcessor {
	private String input = "";
	private boolean open;
	public static Console instance;
	private ArrayList<ConsoleCommand> commands = new ArrayList<>();
	private ArrayList<String> history = new ArrayList<>();
	private int historyIndex = 0;
	private String savedString;

	public class Line {
		public String text;
		public float time;
	}

	public ArrayList<Line> lines = new ArrayList<>();

	public Console() {
		instance = this;

		org.rexcellentgames.burningknight.game.input.Input.multiplexer.addProcessor(this);

		this.commands.add(new GiveCommand());
		this.commands.add(new HealCommand());
		this.commands.add(new GodModeCommand());
		this.commands.add(new LevelCommand());
		this.commands.add(new LightCommand());
		this.commands.add(new DebugCommand());
		this.commands.add(new DieCommand());
		this.commands.add(new PassableCommand());
		this.commands.add(new RoomDebugCommand());
		this.commands.add(new ZoomCommand());
		this.commands.add(new HurtCommand());
	}

	public void destroy() {
		org.rexcellentgames.burningknight.game.input.Input.multiplexer.removeProcessor(this);
	}
	
	public void update(float dt) {
		for (int i = this.lines.size() - 1; i >= 0; i--) {
			Line line = this.lines.get(i);
			line.time += dt;

			if (line.time >= 5f) {
				this.lines.remove(i);
			}
		}
	}

	public void print(String str) {
		Line line = new Line();
		line.text = str;

		this.lines.add(0, line);
	}

	public void render() {
		if (!Ui.hideUi) {
			for (int i = 0; i < this.lines.size(); i++) {
				Line line = this.lines.get(i);
				Graphics.print(line.text, Graphics.small, 2, 2 + (i + (this.open ? 1 : 0)) * 10);
			}
		}

		if (this.open) {
			Graphics.print(this.input + "|", Graphics.small, 2, 2);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.UP) {
			if (historyIndex == 0) {
				savedString = this.input;
			}

			if (this.historyIndex + 1 <= history.size()) {
				input = history.get(historyIndex);
				this.historyIndex++;
			}
		} else if (keycode == Input.Keys.DOWN) {
			if (historyIndex == 0) {
				return false;
			}

			historyIndex--;

			if (historyIndex == 0) {
				input = this.savedString;
				this.savedString = null;
			}
		} else if (keycode == Input.Keys.F1 && Version.debug) {
			this.open = !this.open;
			Audio.playSfx(this.open ? "menu/select" : "menu/exit");

			if (this.open && Dungeon.game.getState() instanceof InGameState) {
				Dungeon.game.getState().setPaused(true);
			}

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
			input = "/" + input;
		}

		history.add(0, input);

		String[] parts = input.split("\\s+");
		String name = parts[0];

		for (ConsoleCommand command : this.commands) {
			if (command.name.equals(name) || command.shortName.equals(name)) {
				String[] args = new String[parts.length - 1];

				System.arraycopy(parts, 1, args, 0, args.length);
				command.run(this, args);

				return;
			}
		}

		this.print("[red]Unknown command");
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