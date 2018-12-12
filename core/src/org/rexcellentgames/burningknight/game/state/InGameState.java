package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.*;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.debug.Console;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.levels.library.LibraryLevel;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.BossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.BossEntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.treasure.TreasureRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.fx.WindFx;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.*;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class InGameState extends State {
	private Console console;
	private Area pauseMenuUi;
	public static TextureRegion noise;
	private static Music fire;
	private static Music water;
	private float volume = 0;
	private float flowVolume;

	public static boolean forceBoss;

	@Override
	public void init() {
		if (fire == null) {
			fire = Audio.getMusic("OnFire");
			water = Audio.getMusic("water");
			noise = new TextureRegion(new Texture(Gdx.files.internal("noise.png")));
		}

		settingsX = 0;

		Ui.saveAlpha = 0;
		Audio.important = false;

		Dungeon.dark = 1;

		Ui.controls.clear();

		shader.begin();
		float a = Random.newFloat((float) (Math.PI * 2));

		shader.setUniformf("tx", (float) Math.cos(a));
		shader.setUniformf("ty", (float) Math.sin(a));
		shader.end();

		Dungeon.white = 0;

		if (Input.instance.activeController != null) {
			Achievements.unlock(Achievements.UNLOCK_DENDY);
		}

		Camera.did = false;

		pauseMenuUi = new Area(true);

		Collisions collisions = new Collisions();

		World.world.setContactListener(collisions);
		World.world.setContactFilter(collisions);

		this.setupUi();

		Dungeon.setBackground2(new Color(Level.colors[Dungeon.level.uid]));

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
			}
		});

		if (Dungeon.level instanceof DesertLevel) {
			Achievements.unlock(Achievements.REACH_DESERT);
			Achievements.unlock(Achievements.UNLOCK_DEW_VIAL);
		} else if (Dungeon.level instanceof LibraryLevel) {
			Achievements.unlock(Achievements.REACH_LIBRARY);
		}

		if (BurningKnight.instance == null && !GameSave.defeatedBK && Dungeon.depth > -1) {
			BurningKnight knight = new BurningKnight();

			Dungeon.area.add(knight);
			PlayerSave.add(knight);
		}

		for (int i = 0; i < 15; i++) {
			Dungeon.area.add(new WindFx());
		}

		volume = 0;
	}

	private boolean wasHidden;
	private String depth;
	private float w;

	private float lastSave;
	private Tween.Task lastTask;
	private Tween.Task lastTaskSize;
	private boolean wasInSettings;

	@Override
	public void setPaused(boolean paused) {
		super.setPaused(paused);
		Dungeon.dark = 1;
		Dungeon.darkR = Dungeon.MAX_R;

		if (this.isPaused()) {
			this.mv = -256;
			depth = Dungeon.level.formatDepth();
			this.wasHidden = !UiMap.instance.isOpen();

			Graphics.layout.setText(Graphics.medium, depth);
			this.w = Graphics.layout.width;

			if (!wasHidden) {
				UiMap.instance.hide();
			}

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

			Tween.remove(lastTask);
			lastTask = Tween.to(new Tween.Task(0, 0.4f, Tween.Type.BACK_OUT) {
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


			Tween.remove(lastTaskSize);
			lastTaskSize = Tween.to(new Tween.Task(52, 0.2f) {
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
			if (wasInSettings) {
				wasInSettings = false;
				SaveManager.saveGames();
			}

			if (!wasHidden) {
				UiMap.instance.show();
			}

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


			Tween.remove(lastTaskSize);
			lastTaskSize = Tween.to(new Tween.Task(0, 0.2f) {
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


			Tween.remove(lastTask);
			lastTask = Tween.to(new Tween.Task(-256, 0.2f) {
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
		settingsX = 0;
		Dungeon.grayscale = 0;
		Dungeon.white = 0;
		Ui.saveAlpha = 0;
		this.console.destroy();
		Dungeon.battleDarkness = 0;

		Camera.instance.resetShake();

		if (Dungeon.reset || Player.instance == null || Player.instance.isDead()) {
			Gdx.files.external(SaveManager.SAVE_DIR + SaveManager.slot).deleteDirectory();
			Dungeon.reset = false;
		} else {
			boolean old = (Dungeon.game.getState() instanceof LoadState);

			SaveManager.save(SaveManager.Type.GAME, old);
			SaveManager.save(SaveManager.Type.LEVEL, old);
			SaveManager.save(SaveManager.Type.PLAYER, old);
		}

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}

		pauseMenuUi.destroy();

		fire.setVolume(0);
		fire.pause();

		water.setVolume(0);
		water.pause();
	}

	private float last;
	public static boolean burning;
	public static boolean flow;

	private void lightUp(Room room) {
		for (int x = room.left; x <= room.right; x++) {
			for (int y = room.top; y <= room.bottom; y++) {
				if ((x == room.left || x == room.right || y == room.top || y == room.bottom
				) && (Dungeon.level.checkFor(x, y, Terrain.PASSABLE) || Dungeon.level.checkFor(x, y, Terrain.HOLE))) {
					Dungeon.level.addLightInRadius(x * 16, y * 16, 2f, 3f, false);
				}

				if (y != room.top) {
					Dungeon.level.addLight(x * 16, y * 16, 4f, 1f);
				}
			}
		}
	}

	@Override
	public void update(float dt) {
		if (Dungeon.depth == -2) {
			Upgrade.Companion.setUpdateEvent(false);
		}

		if (!isPaused() && !Player.instance.isDead() && Dungeon.depth != -2) {
			GameSave.time += Gdx.graphics.getDeltaTime();
		} else {
			Dungeon.speed += (1 - Dungeon.speed) * dt * 5;
		}

		if (isPaused()) {
			Camera.instance.update(dt);
		} else {
			this.time += dt;
			this.lastSave += dt;

			if (lastSave >= 60f) {
				lastSave = 0;
				SaveManager.saveGame();
			}
		}

		Orbital.updateTime(dt);

		if (Player.instance.room != null) {
			lightUp(Player.instance.room);
			// todo: as a challenge
			// Dungeon.level.addLightInRadius(Player.instance.x + 8, Player.instance.y + 8, 0, 0, 0, 2f, 8f, false);
		}

		for (Room room : Dungeon.level.getRooms()) {
			if (room instanceof BossEntranceRoom) {
				lightUp(room);
				break;
			}
		}
		
		//if (Version.debug) {
			this.console.update(dt);

			if (Input.instance.wasPressed("F4")) {
				Dungeon.darkR = Dungeon.MAX_R;
				Player.instance.setUnhittable(true);
				Camera.follow(null);

				Vector3 vec = Camera.game.project(new Vector3(Player.instance.x + Player.instance.w / 2, Player.instance.y + Player.instance.h / 2, 0));
				vec = Camera.ui.unproject(vec);
				vec.y = Display.GAME_HEIGHT - vec.y / Display.UI_SCALE;

				Dungeon.darkX = vec.x / Display.UI_SCALE;
				Dungeon.darkY = vec.y;

				Tween.to(new Tween.Task(0, 0.3f, Tween.Type.QUAD_OUT) {
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
						Dungeon.newGame();
						Dungeon.setBackground2(new Color(0, 0, 0, 1));
					}
				});
			}

			if (Version.debug) {
				if (Input.instance.wasPressed("F5")) {
					for (Room room : Dungeon.level.getRooms()) {
						if (room instanceof ShopRoom && !room.hidden) {
							Point point = room.getRandomFreeCell();
							Player.instance.tp(point.x * 16, point.y * 16);

							break;
						}
					}
				} else if (Input.instance.wasPressed("F6")) {
					for (Room room : Dungeon.level.getRooms()) {
						if (room instanceof TreasureRoom) {
							Point point = room.getRandomFreeCell();

							Player.instance.tp(point.x * 16, point.y * 16);

							break;
						}
					}
				} else if (Input.instance.wasPressed("F7")) {
					for (Room room : Dungeon.level.getRooms()) {
						if (room instanceof BossRoom && room != Player.instance.room) {

							Point point = room.getRandomFreeCell();

							Player.instance.tp(point.x * 16, point.y * 16);

							break;
						}
					}
				}
			}
			if (Input.instance.wasPressed("F3")) {
				Ui.hideUi = !Ui.hideUi;
			} else if (Input.instance.wasPressed("F9")) {
				Ui.hideCursor = !Ui.hideCursor;
			}

			if (Version.debug) {
				if (Input.instance.wasPressed("O")) {
					Ui.upscale = 1;
				} else if (Input.instance.isDown("I")) {
					Ui.upscale = Math.max(0.1f, Ui.upscale - dt * 3);
				} else if (Input.instance.isDown("P")) {
					Ui.upscale += dt * 3;
				}
			}
		//}

		if (Player.instance != null && !Player.instance.isDead()) {
			last += dt;

			if (last >= 1f) {
				last = 0;

				if (((Dungeon.depth == -3 && BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive")) || forceBoss)) {
					Audio.highPriority("Rogue");
				} else if (BurningKnight.instance != null && BurningKnight.instance.rage && !BurningKnight.instance.dest) {
					Audio.play("Cursed legend");
				} else if ((BurningKnight.instance == null || !(BurningKnight.instance.dest))) {
					if (Dungeon.depth == -2 || Player.instance.room instanceof ShopRoom) {
						Audio.play("Shopkeeper");
					} else if (!Player.instance.isDead() && Dungeon.depth > -1 && BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive") && !BurningKnight.instance.rage) {
						Audio.play("Rogue");
					} else {
						Audio.play(Dungeon.level.getMusic());
					}
				}
			}
		}

		if (this.isPaused()) {
			pauseMenuUi.update(dt);
		} else {
			World.update(dt);
		}
			 
		if (Dialog.active != null) {
			Dialog.active.update(dt);
		}

		if (Player.instance != null && Player.instance.getHp() < lastHp) {
			if (!setFrames) {
				setFrames = true;

				Tween.to(new Tween.Task(0.1f, 0.1f) {
					@Override
					public float getValue() {
						return Dungeon.blood;
					}

					@Override
					public void setValue(float value) {
						Dungeon.blood = value;
					}

					@Override
					public void onEnd() {
						if (Player.instance.getHp() > 1) {
							Tween.to(new Tween.Task(0, 0.4f) {
								@Override
								public float getValue() {
									return Dungeon.blood;
								}

								@Override
								public void setValue(float value) {
									Dungeon.blood = value;
								}
							});
						}
					}
				});

				for (int i = 0; i < 64; i++) {
					Bloodsplat splat = new Bloodsplat();

					splat.x = Random.newFloat(Display.UI_WIDTH);

					if (splat.x < 32 || splat.x > Display.UI_WIDTH - 32) {
						splat.y = Random.newFloat(Display.UI_HEIGHT);

						if (splat.x < 32) {
							splat.x -= 16;
						} else {
							splat.x += 16;
						}
					} else if (Random.chance(50)) {
						splat.y = Random.newFloat(32) - 32;
					} else {
						splat.y = Display.UI_HEIGHT - Random.newFloat(32) + 32;
					}

					Dungeon.ui.add(splat);
				}
			}
		} else {
			setFrames = false;
		}

		if (Player.instance != null) {
			lastHp = Player.instance.getHp();
		}

		dark = Player.instance.isDead();

		if (!dark) {
			dark = Boss.all.size() > 0 && Player.instance.room instanceof BossRoom && !BurningKnight.instance.rage;

			if (!dark) {

				for (Mob mob : Mob.all) {
					if (mob.room == Player.instance.room) {
						dark = true;
						break;
					}
				}
			}
		}

		// Dungeon.battleDarkness += ((dark ? 0 : 1) - Dungeon.battleDarkness) * dt * 2;
		Dungeon.battleDarkness = 0;

		boolean none = volume <= 0.05f;
		volume += ((burning ? 1 : 0) - volume) * dt;

		try {
			if (volume > 0.05f && none) {
				fire.play();
			} else if (volume < 0.05f && !none) {
				fire.pause();
			}

			fire.setVolume(volume * Settings.sfx);
			burning = false;


			none = volume <= 0.05f;
			flowVolume += ((flow ? 1 : 0) - flowVolume) * dt;

			if (flowVolume > 0.05f && none) {
				water.play();
			} else if (flowVolume < 0.05f && !none) {
				water.pause();
			}

			water.setVolume(flowVolume * Settings.sfx * 0.25f);
		} catch (GdxRuntimeException e) {
			// Failed to allocate audio buffers
		}

		flow = false;
	}

	public static boolean dark = true;

	private int lastHp;
	private boolean setFrames;
	private float mv = - 256;
	private float size;

	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(), Gdx.files.internal("shaders/fog.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	private float time;

	@Override
	public void render() {
		super.render();

		if (Dungeon.depth > -3 && Settings.quality > 1) {
			Graphics.batch.end();
			Graphics.batch.setShader(shader);

			shader.begin();
			shader.setUniformf("time", time * 0.01f);
			shader.setUniformf("cx", Camera.game.position.x / 512);
			shader.setUniformf("cy", -Camera.game.position.y / 512);
			shader.end();

			Graphics.batch.begin();

			Graphics.batch.setColor(1, 1, 1, 0.5f);
			Graphics.render(noise, Camera.game.position.x - Display.GAME_WIDTH / 2, Camera.game.position.y - Display.GAME_HEIGHT / 2, 0, 0, 0, false, false);
			Graphics.batch.setColor(1, 1, 1, 1);

			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();

			if (Dungeon.depth == -2) {
				for (Upgrade upgrade : Upgrade.Companion.getAll()) {
					upgrade.renderSigns();
				}
			}
		}


		Player.instance.renderBuffs();
	}

	@Override
	public void renderUi() {
		Dungeon.ui.render();
		Ui.ui.render();

		if (this.size > 0) {
			Graphics.startShape();
			Graphics.shape.setColor(0, 0, 0, 1);
			Graphics.shape.rect(0, 0, Display.UI_WIDTH, size);
			Graphics.shape.rect(0, Display.UI_HEIGHT - size, Display.UI_WIDTH, size);
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

			Camera.ui.translate(settingsX, this.mv);
			Camera.ui.update();

			Graphics.batch.setProjectionMatrix(Camera.ui.combined);

			pauseMenuUi.render();

			Graphics.print(this.depth, Graphics.medium, Display.UI_WIDTH / 2 - w / 2, 128 + 32 + 16);

			Camera.ui.translate(-settingsX, -this.mv);
			Camera.ui.update();
		}

		Ui.ui.renderCursor();
	}

	private void setupUi() {
		UiInventory inventory = new UiInventory(Player.instance.getInventory());

		Dungeon.ui.add(inventory);
		Dungeon.ui.add(new UiMap());

		int y = -24 + 16;

		this.pauseMenuUi.add(new UiButton("resume", Display.UI_WIDTH / 2, 128 + 32 + y) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				setPaused(false);
			}

			@Override
			public void render() {
				super.render();

				if (settingsX == 0 && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("quick_restart", Display.UI_WIDTH / 2, 128 + 32 - 24 + y) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.grayscale = 0;

					if (Dungeon.depth == -3) {
						Dungeon.newGame(false, -3);
					} else {
						Dungeon.newGame(true, 1);
					}
				});
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("settings", Display.UI_WIDTH / 2, 128 + 32 - 24 * 2 + y) {
			@Override
			public void onClick() {
				super.onClick();
				addSettings();

				Tween.to(new Tween.Task(Display.UI_WIDTH, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}.setSparks(true));

		this.pauseMenuUi.add(new UiButton("save_and_exit", Display.UI_WIDTH / 2, 128 + 32 - 24 * 3 + y) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

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

	public static float settingsX;
	public boolean addedSettings;

	private void addSettings() {
		if (addedSettings) {
			return;
		}

		addedSettings = true;
		float s = 20;
		float st = 60 + 20f;

		this.pauseMenuUi.add(new UiButton("back", (int) (Display.UI_WIDTH * 1.5f), (int) st) {
			@Override
			public void render() {
				super.render();

				if (settingsX == Display.UI_WIDTH && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(0, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		});

		this.pauseMenuUi.add(new UiButton("audio", (int) (Display.UI_WIDTH * 1.5f), (int) (st + s * 2)) {
			@Override
			public void onClick() {
				super.onClick();
				addAudio();

				Tween.to(new Tween.Task(Display.UI_WIDTH * 2f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		});

		this.pauseMenuUi.add(new UiButton("controls", (int) (Display.UI_WIDTH * 1.5f), (int) (st + s * 3)) {
			@Override
			public void onClick() {
				super.onClick();
				addControls();

				Tween.to(new Tween.Task(Display.UI_WIDTH * 2f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		});

		this.pauseMenuUi.add(new UiButton("game", (int) (Display.UI_WIDTH * 1.5f), (int) (st + s * 4)) {
			@Override
			public void onClick() {
				super.onClick();
				addGame();

				Tween.to(new Tween.Task(Display.UI_WIDTH * 2f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		});

		this.pauseMenuUi.add(new UiButton("graphics", (int) (Display.UI_WIDTH * 1.5f), (int) (st + s * 5)) {
			@Override
			public void onClick() {
				super.onClick();
				addGraphics();

				Tween.to(new Tween.Task(Display.UI_WIDTH * 2f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		});
	}

	private ArrayList<Entity> currentSettings = new ArrayList<>();

	public void clear() {
		wasInSettings = true;
		UiChoice.maxW = 0;

		for (Entity e : currentSettings) {
			e.done = true;
		}
	}

			/*
Settings:
* Video:
	+ Screen shake
	+ Freeze frames
	+ Cursor (+ native)
	+ Cursor rotation
	+ Side art (new)
	+ Fullscreen
	+ Borderless window
	+ Flash frames
	+ Quality (Low, Good, Great)
	+ Colorblind mode
* Audio:
	+ Music volume
	+ Sfx volume
	+ Ui sfx
* Controls:
	+ Use
	+ Swap weapons
	+ Interact
	+ Active item
	+ Movement
	+ Roll
* Game:
  + Show FPS
  + Hide HUD
	+ Speedrun timer
	+ Speedrun mode
	+ Blood and Gore
	+ View credits
	+ Reset progress
	+ Vegan mode
		 */

	public void addControls() {
		clear();

		float s = 20;
		float st = 60 + 5f;

		currentSettings.add(pauseMenuUi.add(new UiButton("back", (int) (Display.UI_WIDTH * 2.5f), (int) (st)) {
			@Override
			public void render() {
				super.render();

				if (settingsX == Display.UI_WIDTH * 2 && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.UI_WIDTH * 1f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}));

		currentSettings.add(pauseMenuUi.add(new UiKey("use", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 6))));
		currentSettings.add(pauseMenuUi.add(new UiKey("switch", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 5))));
		currentSettings.add(pauseMenuUi.add(new UiKey("interact", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 4))));
		currentSettings.add(pauseMenuUi.add(new UiKey("active", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 3))));
		currentSettings.add(pauseMenuUi.add(new UiKey("roll", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 2))));
	}

	public void addGraphics() {
		clear();

		float s = 14;
		float st = 60;

		currentSettings.add(pauseMenuUi.add(new UiButton("back", (int) (Display.UI_WIDTH * 2.5f), (int) (st)) {
			@Override
			public void render() {
				super.render();

				if (settingsX == Display.UI_WIDTH * 2 && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.UI_WIDTH * 1f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("borderless_window", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 2)) {
			@Override
			public void onClick() {
				Settings.borderless = !Settings.borderless;
				((Lwjgl3Graphics) Gdx.graphics).setUndecorated(Settings.borderless);
				super.onClick();
			}
		}.setOn(Settings.borderless)));

		currentSettings.add(pauseMenuUi.add(new UiChoice("side_art", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 3)) {
			@Override
			public void onUpdate() {
				Settings.side_art = this.getCurrent();
			}
		}.setChoices(new String[] {
			"none", "1/8", "2/8", "3/8", "4/8", "5/8", "6/8", "7/8", "8/8"
		}).setCurrent(Settings.side_art)));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("rotate_cursor", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 4)) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.rotateCursor = this.isOn();
			}
		}.setOn(Settings.rotateCursor)));

		currentSettings.add(pauseMenuUi.add(new UiChoice("cursor", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 5)) {
			@Override
			public void onClick() {
				super.onClick();
				Settings.cursor = Settings.cursors[this.getCurrent()];
				Settings.cursorId = this.getCurrent();

				if (Settings.cursorId == Settings.cursors.length - 1) {
					Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
				} else {
					Gdx.graphics.setCursor(Dungeon.cursor);
				}
			}
		}.setChoices(new String[] {
			"1/9", "2/9", "3/9", "4/9", "5/9", "6/9", "7/9", "8/9", "native"
		}).setCurrent(Settings.getCursorId(Settings.cursor))));

		currentSettings.add(pauseMenuUi.add(new UiChoice("colorblind_mode", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 6)) {
			@Override
			public void onUpdate() {
				Dungeon.colorBlind = getCurrent();
			}
		}.setChoices(new String[] {
			"none", "protanope", "deuteranope", "tritanope"
		}).setCurrent((int) Dungeon.colorBlind)));

		currentSettings.add(pauseMenuUi.add(new UiSlider("freeze_frames", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 7)) {
			@Override
			public void onUpdate() {
				Settings.freeze_frames = this.val;
			}
		}.setValue(Settings.freeze_frames)));

		currentSettings.add(pauseMenuUi.add(new UiSlider("flash_frames", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 8)) {
			@Override
			public void onUpdate() {
				Settings.flash_frames = this.val;
			}
		}.setValue(Settings.flash_frames)));

		currentSettings.add(pauseMenuUi.add(new UiSlider("screenshake", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 9)) {
			@Override
			public void onUpdate() {
				Settings.screenshake = this.val;
			}
		}.setValue(Settings.screenshake)));

		currentSettings.add(pauseMenuUi.add(new UiChoice("quality", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 10)) {
			@Override
			public void onUpdate() {
				Settings.quality = getCurrent();
			}
		}.setChoices(new String[] {
			"bad", "good", "great"
		}).setCurrent(Settings.quality)));
	}

	public void addAudio() {
		clear();

		float s = 20;
		float st = 60 + 20f;

		currentSettings.add(pauseMenuUi.add(new UiButton("back", (int) (Display.UI_WIDTH * 2.5f), (int) (st)) {
			@Override
			public void render() {
				super.render();

				if (settingsX == Display.UI_WIDTH * 2 && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.UI_WIDTH * 1f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("uisfx", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 2)) {
			@Override
			public void onClick() {
				Settings.uisfx = !Settings.uisfx;
				super.onClick();
			}
		}.setOn(Settings.uisfx)));

		currentSettings.add(pauseMenuUi.add(new UiSlider("sfx", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 3)) {
			@Override
			public void onUpdate() {
				Settings.sfx = this.val;
			}
		}.setValue(Settings.sfx)));

		currentSettings.add(pauseMenuUi.add(new UiSlider("music", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 4)) {
			@Override
			public void onUpdate() {
				Settings.music = this.val;
				Audio.update();
			}
		}.setValue(Settings.music)));
	}

	public void addGame() {
		clear();

		float s = 18;
		float st = 60 - 2.5f;

		currentSettings.add(pauseMenuUi.add(new UiButton("back", (int) (Display.UI_WIDTH * 2.5f), (int) (st)) {
			@Override
			public void render() {
				super.render();

				if (settingsX == Display.UI_WIDTH * 2 && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.UI_WIDTH * 1f, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}));

		currentSettings.add(pauseMenuUi.add(new UiButton("view_credits", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 2)) {
			@Override
			public void onClick() {
				super.onClick();
				// todo
			}
		}));

		currentSettings.add(pauseMenuUi.add(new UiButton("reset_progress", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 3)) {
			@Override
			public void onClick() {
				super.onClick();

				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return Dungeon.dark;
					}

					@Override
					public void setValue(float value) {
						Dungeon.dark = value;
					}

					@Override
					public void onEnd() {
						SaveManager.deleteAll();
						Dungeon.depth = -2;
						Dungeon.loadType = Entrance.LoadType.GO_DOWN;
						Player.instance = null;
						Player.ladder = null;
						BurningKnight.instance = null;
						Dungeon.game.setState(new MainMenuState());
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});

				// todo: warning window
			}
		}));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("speedrun_mode", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 4)) {
			@Override
			public void onClick() {
				Settings.speedrun_mode = !Settings.speedrun_mode;
				super.onClick();
			}
		}.setOn(Settings.speedrun_mode)));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("speedrun_timer", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 5)) {
			@Override
			public void onClick() {
				Settings.speedrun_timer = !Settings.speedrun_timer;
				Dungeon.tweenTimer(Settings.speedrun_timer);
				super.onClick();
			}
		}.setOn(Settings.speedrun_timer)));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("blood_gore", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 6)) {
			@Override
			public void onClick() {
				Settings.blood = !Settings.blood;
				Settings.gore = !Settings.gore;
				super.onClick();
			}
		}.setOn(Settings.gore)));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("vegan_mode", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 7)) {
			@Override
			public void onClick() {
				Settings.vegan = !Settings.vegan;
				super.onClick();
			}
		}.setOn(Settings.vegan)));

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("show_fps", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 8)) {
			@Override
			public void update(float dt) {
				super.update(dt);

				setOn(Dungeon.fpsY != 0);
			}

			@Override
			public void onClick() {
				Tween.to(new Tween.Task(Dungeon.fpsY == 0 ? 18 : 0, 0.3f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return Dungeon.fpsY;
					}

					@Override
					public void setValue(float value) {
						Dungeon.fpsY = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});

				super.onClick();
			}
		}.setOn(Dungeon.fpsY != 0)));
	}

	@Override
	public void onPause() {
		super.onPause();
    this.pauseMenuUi.show();
	}
}