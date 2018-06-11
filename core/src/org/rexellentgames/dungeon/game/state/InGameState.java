package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Collisions;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.MusicManager;
import org.rexellentgames.dungeon.debug.Console;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.boss.Boss;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.save.SaveManager;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiEntity;
import org.rexellentgames.dungeon.ui.UiMap;
import org.rexellentgames.dungeon.util.Dialog;
import org.rexellentgames.dungeon.util.Fps;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class InGameState extends State {
	private UiInventory inventory;
	private Console console;
	private TextureRegion blood;
	private float a;
	private ArrayList<UiEntity> ui;
	private boolean showFps;
	public static boolean map = false;

	@Override
	public void init() {
		ui = new ArrayList<>();

		blood = Graphics.getTexture("blood_frame");

		Collisions collisions = new Collisions();

		World.world.setContactListener(collisions);
		World.world.setContactFilter(collisions);

		this.setupUi();

		Dungeon.background = Level.colors[Dungeon.level.uid];

		this.console = new Console();

		if (BurningKnight.instance != null && Dungeon.depth > 0) {
			BurningKnight.instance.findStartPoint();
		}

		Dungeon.darkR = 0;

		Tween.to(new Tween.Task(Dungeon.MAX_R, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.darkR;
			}

			@Override
			public void setValue(float value) {
				Dungeon.darkR = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();
				Player.instance.setUnhittable(false);
				Camera.instance.follow(Player.instance);
			}
		});

		// Dialog.active = BurningKnight.onLampTake;
		// Dialog.active.start();
	}

	@Override
	public void destroy() {
		super.destroy();
		this.console.destroy();

		if (Dungeon.reset) {
			Gdx.files.external(".bk/" + SaveManager.slot).deleteDirectory();
			Dungeon.reset = false;
		} else {
			SaveManager.save(SaveManager.Type.GAME);
			SaveManager.save(SaveManager.Type.LEVEL);
			SaveManager.save(SaveManager.Type.PLAYER);
		}

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}
	}

	private boolean set;
	private float last;

	@Override
	public void update(float dt) {
		if (map) {
			return;
		}

		this.console.update(dt);

		if (Input.instance.wasPressed("reset")) {
			Dungeon.newGame();
		}
		
		if (Input.instance.wasPressed("show_fps")) {
			this.showFps = !this.showFps;
		}

		last += dt;

		if (last >= 1f) {
			last = 0;

			if (Boss.all.size() > 1 && !BurningKnight.instance.getState().equals("unactive")) {
				MusicManager.play("Rogue");
			} else {
				MusicManager.play(Dungeon.level.getMusic());
			}
		}

		World.update(dt);

		if (Dialog.active != null) {
			Dialog.active.update(dt);
		}

		if (!set) {
			if (Player.instance != null) {
				Camera.instance.follow(Player.instance);
			}

			set = true;
		}

		if (this.a == 0 && Player.instance != null && Player.instance.getInvt() > 0) {
			Tween.to(new Tween.Task(1f, 0.2f) {
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
					Tween.to(new Tween.Task(0f, 0.8f) {
						@Override
						public float getValue() {
							return a;
						}

						@Override
						public void setValue(float value) {
							a = value;
						}
					});
				}
			});
		}
	}

	@Override
	public void renderUi() {
		if (this.a != 0) {
			Graphics.batch.setColor(1, 1, 1, this.a);
			Graphics.render(blood, 0, 0);
			Graphics.batch.setColor(1, 1, 1, 1);
		}

		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
		World.render();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Ui.ui.renderUi();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		this.console.render();
		Dungeon.ui.render();

		Ui.ui.render();

		if (Dialog.active != null) {
			Dialog.active.render();
		}

		Ui.ui.renderCursor();

		if (this.showFps && !this.isPaused()) {
			Graphics.print(Integer.toString(Gdx.graphics.getFramesPerSecond()), Graphics.medium, 3, Display.GAME_HEIGHT - 20);
		}
	}

	private void setupUi() {
		this.inventory = new UiInventory(Player.instance.getInventory());
		Dungeon.ui.add(this.inventory);

		Dungeon.ui.add(new UiMap());

		this.ui.add((UiEntity) Dungeon.ui.add(new UiButton("resume", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();
				setPaused(false);

				Camera.instance.shake(3);
			}
		}.setSparks(true)));

		this.ui.add((UiEntity) Dungeon.ui.add(new UiButton("settings", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				transition(() -> {
					SettingsState.fromGame = true;
					Dungeon.game.setState(new SettingsState());
				});
				Camera.instance.shake(3);
			}
		}.setSparks(true)));

		this.ui.add((UiEntity) Dungeon.ui.add(new UiButton("save_and_exit", Display.GAME_WIDTH / 2, 128 - 24 * 3) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);
				transition(() -> Dungeon.game.setState(new MainMenuState()));
			}
		}));

		for (UiEntity entity : this.ui) {
			entity.setActive(false);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		for (UiEntity entity : this.ui) {
			entity.setActive(true);
		}
	}

	@Override
	public void onUnpause() {
		super.onUnpause();

		for (UiEntity entity : this.ui) {
			entity.setActive(false);
		}
	}
}