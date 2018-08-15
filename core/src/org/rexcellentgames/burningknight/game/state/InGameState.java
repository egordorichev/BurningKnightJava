package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Collisions;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.debug.Console;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.secret.SecretRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.TreasureRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.Bloodsplat;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiMap;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class InGameState extends State {
	private UiInventory inventory;
	private Console console;
	private Area pauseMenuUi;
	private static float fpsY;

	@Override
	public void init() {
		// ModManager.INSTANCE.load();

		pauseMenuUi = new Area(true);

		Collisions collisions = new Collisions();

		World.world.setContactListener(collisions);
		World.world.setContactFilter(collisions);

		this.setupUi();

		Dungeon.setBackground2(Level.colors[Dungeon.level.uid]);

		this.console = new Console();

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
				Camera.follow(Player.instance);
			}
		});

		if (BurningKnight.instance == null) {
			BurningKnight knight = new BurningKnight();

			Dungeon.area.add(knight);
			PlayerSave.add(knight);
			knight.attackTp = true;
		}
	}

	@Override
	public void setPaused(boolean paused) {
		super.setPaused(paused);

		if (this.isPaused()) {
			this.mv = -256;

			Tween.to(new Tween.Task(1, 0.3f) {
				@Override
				public float getValue() {
					return Dungeon.grayscale;
				}

				@Override
				public void setValue(float value) {
					Dungeon.grayscale = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

			Tween.to(new Tween.Task(0, 0.4f, Tween.Type.BACK_OUT) {
				@Override
				public float getValue() {
					return mv;
				}

				@Override
				public void setValue(float value) {
					mv = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

			Tween.to(new Tween.Task(52, 0.2f) {
				@Override
				public float getValue() {
					return size;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}

				@Override
				public void setValue(float value) {
					size = value;
				}
			});
		} else {
			Tween.to(new Tween.Task(0, 0.3f) {
				@Override
				public float getValue() {
					return Dungeon.grayscale;
				}

				@Override
				public void setValue(float value) {
					Dungeon.grayscale = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

			Tween.to(new Tween.Task(0, 0.2f) {
				@Override
				public float getValue() {
					return size;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}

				@Override
				public void setValue(float value) {
					size = value;
				}
			});

			Tween.to(new Tween.Task(-256, 0.2f) {
				@Override
				public float getValue() {
					return mv;
				}

				@Override
				public void setValue(float value) {
					mv = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();
					pauseMenuUi.hide();
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.console.destroy();

		if (Dungeon.reset) {
			Gdx.files.external(".bk/" + SaveManager.slot).deleteDirectory();
			Dungeon.reset = false;
		} else {
			boolean old = (Dungeon.game.getState() instanceof LoadState);

			Log.info("Old " + old);

			SaveManager.save(SaveManager.Type.GAME, old);
			SaveManager.save(SaveManager.Type.LEVEL, old);
			SaveManager.save(SaveManager.Type.PLAYER, old);
		}

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}

		pauseMenuUi.destroy();
	}

	private boolean set;
	private float last;


	@Override
	public void update(float dt) {
		if (!Player.instance.isDead()) {
			GameSave.time += Gdx.graphics.getDeltaTime();
		}

		if (Player.instance.room != null) {
			for (int x = Player.instance.room.left; x <= Player.instance.room.right; x++) {
				for (int y = Player.instance.room.top; y <= Player.instance.room.bottom; y++) {
					if ((x == Player.instance.room.left || x == Player.instance.room.right || y == Player.instance.room.top || y == Player.instance.room.bottom
					) && (Dungeon.level.checkFor(x, y, Terrain.PASSABLE) || Dungeon.level.checkFor(x, y, Terrain.HOLE))) {
						Dungeon.level.addLightInRadius(x * 16, y * 16, 0, 0, 0, 2f, 2f, false);
					}

					if (y != Player.instance.room.top) {
						Dungeon.level.addLight(x * 16, y * 16, 0, 0, 0, 2f, 2f);
					}
				}
			}
		}
		
		if (Version.debug) {
			this.console.update(dt);

			if (Input.instance.wasPressed("reset")) {
				Dungeon.newGame();
			}

			if (Input.instance.wasPressed("to_shop")) {
				for (Room room : Dungeon.level.getRooms()) {
					if (room instanceof ShopRoom && !room.hidden) {
						Point point = room.getRandomFreeCell();
						Player.instance.tp(point.x * 16, point.y * 16);

						break;
					}
				}
			}

			if (Input.instance.wasPressed("to_treasure")) {
				for (Room room : Dungeon.level.getRooms()) {
					if (room instanceof TreasureRoom) {
						Point point = room.getRandomFreeCell();

						Player.instance.tp(point.x * 16, point.y * 16);

						break;
					}
				}
			}

			if (Input.instance.wasPressed("to_secret")) {
				for (Room room : Dungeon.level.getRooms()) {
					if (room instanceof SecretRoom && room != Player.instance.room) {
						if (room.hidden) {
							for (int x = room.left; x <= room.right; x++) {
								for (int y = room.top; y <= room.bottom; y++) {
									if (Dungeon.level.get(x, y) == Terrain.CRACK) {
										Dungeon.level.set(x, y, Terrain.FLOOR_A);
									}
								}
							}


							BombEntity.make(room);
							room.hidden = false;
							Dungeon.level.loadPassable();
							Dungeon.level.addPhysics();
						}

						Point point = room.getRandomFreeCell();

						Player.instance.tp(point.x * 16, point.y * 16);

						break;
					}
				}
			}
		}

		if (Input.instance.wasPressed("show_fps")) {
			Tween.to(new Tween.Task(fpsY == 0 ? 18 : 0, 0.3f, Tween.Type.BACK_OUT) {
				@Override
				public float getValue() {
					return fpsY;
				}

				@Override
				public void setValue(float value) {
					fpsY = value;
				}
			});

			Achievements.unlock(Achievements.TEST);
		}

		last += dt;

		if (last >= 1f) {
			last = 0;

			if (Boss.all.size() > 1 && !BurningKnight.instance.getState().equals("unactive")) {
				Audio.play("Rogue");
			} else {
				Audio.play(Dungeon.level.getMusic());
			}
		}

		World.update(dt);

		if (this.isPaused()) {
			pauseMenuUi.update(dt);
		}
			 
		if (Dialog.active != null) {
			Dialog.active.update(dt);
		}

		if (!set) {
			if (Player.instance != null) {
				Camera.follow(Player.instance);
			}

			set = true;
		}

		if (Player.instance != null && Player.instance.getInvt() > 0) {
			if (!setFrames) {
				setFrames = true;

				for (int i = 0; i < 64; i++) {
					Bloodsplat splat = new Bloodsplat();

					splat.x = Random.newFloat(Display.GAME_WIDTH);

					if (splat.x < 32 || splat.x > Display.GAME_WIDTH - 32) {
						splat.y = Random.newFloat(Display.GAME_HEIGHT);

						if (splat.x < 32) {
							splat.x -= 16;
						} else {
							splat.x += 16;
						}
					} else if (Random.chance(50)) {
						splat.y = Random.newFloat(32) - 32;
					} else {
						splat.y = Display.GAME_HEIGHT - Random.newFloat(32) + 32;
					}

					Dungeon.ui.add(splat);
				}
			}
		} else {
			setFrames = false;
		}
	}

	private boolean setFrames;
	private Color bg = Color.valueOf("#1a1932");
	private float mv = - 256;
	private float size;

	@Override
	public void renderUi() {
		Dungeon.ui.render();
		Ui.ui.render();

		if (this.size > 0) {
			Graphics.startShape();
			Graphics.shape.setColor(0, 0, 0, 1);
			Graphics.shape.rect(0, 0, Display.GAME_WIDTH, size);
			Graphics.shape.rect(0, Display.GAME_HEIGHT - size, Display.GAME_WIDTH, size);
			Graphics.endShape();
		}

		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		World.render();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		this.console.render();

		if (Dialog.active != null) {
			Dialog.active.render();
		}

		if (this.mv > -256) {
			/*Graphics.shape.setProjectionMatrix(Camera.nil.combined);
			Graphics.startAlphaShape();
			Graphics.shape.setColor(bg.r, bg.g, bg.b, this.alp);
			Graphics.shape.rect(0, 0, Display.GAME_WIDTH, Display.GAME_HEIGHT);
			Graphics.endAlphaShape();*/

			Camera.ui.translate(0, this.mv);
			Camera.ui.update();

			Graphics.batch.setProjectionMatrix(Camera.ui.combined);

			pauseMenuUi.render();

			Camera.ui.translate(0, -this.mv);
			Camera.ui.update();
		}

		if (fpsY > 0) {
			Graphics.print(Integer.toString(Gdx.graphics.getFramesPerSecond())

//					+ " " +
//					String.format("%03d", GameSave.killCount) + " " +
//					String.format("%02d", (int) Math.floor(GameSave.time / 360)) + ":" +
//					String.format("%02d", (int) Math.floor(GameSave.time / 60)) + ":" +
//					String.format("%02d", (int) Math.floor(GameSave.time % 60)) + ":" +
//					String.format("%02d", (int) Math.floor(GameSave.time % 1 * 100))

				, Graphics.medium, 3, Display.GAME_HEIGHT - fpsY);
		}

		Ui.ui.renderCursor();
	}

	private void setupUi() {
		this.inventory = new UiInventory(Player.instance.getInventory());
		
		Dungeon.ui.add(this.inventory);

		Dungeon.ui.add(new UiMap());

		this.pauseMenuUi.add(new UiButton("resume", Display.GAME_WIDTH / 2, 128+ 32) {
			@Override
			public void onClick() {
				super.onClick();
				
				setPaused(false);

				Camera.shake(3);
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("settings", Display.GAME_WIDTH / 2, 128+ 32 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				
				transition(() -> {
					Dungeon.grayscale = 0;
					Dungeon.game.setState(new MainMenuState(true));
					SettingsState.add();
					MainMenuState.cameraX = Display.GAME_WIDTH * 1.5f;
				});
				
				Camera.shake(3);
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("save_and_exit", Display.GAME_WIDTH / 2, 128 + 32 - 24 * 3) {
			@Override
			public void onClick() {
				super.onClick();
				
				Camera.shake(3);
				
				transition(() -> {
					Dungeon.grayscale = 0;
					Dungeon.game.setState(new MainMenuState());
				});
			}
		});

		for (Entity entity : this.pauseMenuUi.getEntities()) {
			entity.setActive(false);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
    this.pauseMenuUi.show();
	}
}