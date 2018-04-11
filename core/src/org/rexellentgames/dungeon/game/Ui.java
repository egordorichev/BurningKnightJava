package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.InGameState;
import org.rexellentgames.dungeon.util.Tween;

public class Ui {
	public static Ui ui;

	private TextureRegion cursor;
	private TextureRegion topFrame;
	private TextureRegion bar;
	private TextureRegion rage;
	private TextureRegion lock;
	private float last;
	private float lastLock;
	private float lastRage;

	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui (cursor)");
		topFrame = Graphics.getTexture("ui (top frame)");
		bar = Graphics.getTexture("bk_health");
		rage = Graphics.getTexture("bk_rage");
		lock = Graphics.getTexture("bk_lock");
	}

	public void render() {
		if (Dungeon.game.getState() instanceof InGameState) {
			if (BurningKnight.instance != null) {
				if (this.last != BurningKnight.instance.getHp()) {
					Tween.to(new Tween.Task(BurningKnight.instance.getHp(), 0.3f) {
						@Override
						public float getValue() {
							return last;
						}

						@Override
						public void setValue(float value) {
							last = value;
						}
					});
				}

				if (this.lastRage != BurningKnight.instance.rageLevel) {
					Tween.to(new Tween.Task(BurningKnight.instance.rageLevel, 0.3f) {
						@Override
						public float getValue() {
							return lastRage;
						}

						@Override
						public void setValue(float value) {
							lastRage = value;
						}
					});
				}

				if (this.lastLock != BurningKnight.instance.getLock()) {
					Tween.to(new Tween.Task(BurningKnight.instance.getLock(), 0.3f) {
						@Override
						public float getValue() {
							return lastLock;
						}

						@Override
						public void setValue(float value) {
							lastLock = value;
						}
					});
				}

				bar.setRegionWidth((int) (this.last / BurningKnight.instance.getHpMax() * Display.GAME_WIDTH));
				Graphics.render(bar, 0, 0, 0, 0, 0, false, false);

				rage.setRegionWidth((int) (Math.min(this.last, this.lastRage) / BurningKnight.instance.getHpMax() * Display.GAME_WIDTH));
				Graphics.render(rage, 0, 0, 0, 0, 0, false, false);

				lock.setRegionWidth((int) (this.lastLock / BurningKnight.instance.getHpMax() * Display.GAME_WIDTH));
				Graphics.render(lock, 0, 0, 0, 0, 0, false, false);
			}

			if (Player.instance != null && Player.instance.isDead()) {
				Graphics.print("Game over!", Graphics.medium, 128);
				Graphics.print("Press space to restart", Graphics.medium, (float) (108 + Math.sin(Dungeon.time * 3) * 4));

				if (Input.instance.wasPressed("action")) {
					Dungeon.newGame();
				}
			}
		}

		// Cursor

		Graphics.render(this.cursor, Input.instance.uiMouse.x,
			Input.instance.uiMouse.y,
			Dungeon.time * 60, 8, 8, false, false);
	}

	public void renderUi() {
		Graphics.render(this.topFrame, 0, Display.GAME_HEIGHT - topFrame.getRegionHeight());
	}
}