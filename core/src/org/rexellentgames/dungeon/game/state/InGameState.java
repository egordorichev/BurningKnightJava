package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import org.rexellentgames.dungeon.Collisions;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.MusicManager;
import org.rexellentgames.dungeon.debug.Console;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.levels.HubLevel;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.ui.UiBar;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiEntity;
import org.rexellentgames.dungeon.util.Dialog;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class InGameState extends State {
	private UiInventory inventory;
	private Console console;
	private int w;
	private TextureRegion blood;
	private float a;
	private ArrayList<UiEntity> ui;

	@Override
	public void init() {
		ui = new ArrayList<>();

		blood = Graphics.getTexture("blood_frame");

		Collisions collisions = new Collisions();

		World.world.setContactListener(collisions);
		World.world.setContactFilter(collisions);

		if (!Network.SERVER) {
			this.setupUi();
		}

		this.console = new Console();

		if (BurningKnight.instance != null && Dungeon.depth > 0 && (Network.SERVER || Network.NONE)) {
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
			Gdx.files.external(".burningknight/").deleteDirectory();
			Dungeon.reset = false;
		} else {
			if (Network.SERVER || Network.NONE) {
				Dungeon.level.save(Level.DataType.PLAYER);
				Dungeon.level.save(Level.DataType.LEVEL);
			}
		}

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}
	}

	private boolean set;
	private float last;

	@Override
	public void update(float dt) {
		this.console.update(dt);

		last += dt;

		if (last >= 3f) {
			last = 0;
			boolean found = false;

			for (Mob mob : Mob.every) {
				if (mob.onScreen) {
					MusicManager.play("Born to do rogueries");
					found = true;
					break;
				}
			}

			if (!found) {
				MusicManager.play("gobbeon");
			}
		}

		World.update(dt);

		UiLog.instance.update(dt);

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

		if (Input.instance.wasPressed("z")) {
			final OrthographicCamera cam = Camera.instance.getCamera();

			Tween.to(new Tween.Task(cam.zoom * 1.3f, 0.1f) {
				@Override
				public float getValue() {
					return cam.zoom;
				}

				@Override
				public void setValue(float value) {
					cam.zoom = value;
				}

				@Override
				public void onEnd() {
					Graphics.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
			});
		} else if (Input.instance.wasPressed("c")) {
			final OrthographicCamera cam = Camera.instance.getCamera();

			Tween.to(new Tween.Task(cam.zoom * 0.7f, 0.1f) {
				@Override
				public float getValue() {
					return cam.zoom;
				}

				@Override
				public void setValue(float value) {
					cam.zoom = value;
				}

				@Override
				public void onEnd() {
					Graphics.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

		if (Dialog.active != null) {
			Dialog.active.render();
		}

		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
		World.render();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Ui.ui.renderUi();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		this.console.render();
		Ui.ui.render();
		Dungeon.ui.render();
	}

	private void setupUi() {
		this.inventory = new UiInventory(Player.instance.getInventory());
		Dungeon.ui.add(this.inventory);

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
				transition(new Runnable() {
					@Override
					public void run() {
						SettingsState.fromGame = true;
						Dungeon.game.setState(new SettingsState());
					}
				});
				Camera.instance.shake(3);
			}
		}.setSparks(true)));

		this.ui.add((UiEntity) Dungeon.ui.add(new UiButton("save_and_exit", Display.GAME_WIDTH / 2, 128 - 24 * 3) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);
				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.game.setState(new MainMenuState());
					}
				});
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