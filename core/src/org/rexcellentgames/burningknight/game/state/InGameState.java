package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.blood.BloodLevel;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.levels.forest.ForestLevel;
import org.rexcellentgames.burningknight.entity.level.levels.ice.IceLevel;
import org.rexcellentgames.burningknight.entity.level.levels.library.LibraryLevel;
import org.rexcellentgames.burningknight.entity.level.levels.tech.TechLevel;
import org.rexcellentgames.burningknight.entity.level.rooms.PrebossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.BossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.BossEntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.secret.SecretRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.NpcSaveRoom;
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
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiMap;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class InGameState extends State {
	private Console console;
	public static TextureRegion noise;
	private static Music fire;
	private static Music water;
	private float volume = 0;
	private float flowVolume;

	@Override
	public void init() {
		if (Achievements.lastActive != null) {
			Achievements.lastActive.init();
		}

		Ui.ui.reset();

		if (fire == null) {
			fire = Audio.getMusic("OnFire");
			water = Audio.getMusic("water");
			noise = new TextureRegion(new Texture(Gdx.files.internal("noise.png")));
		}

		settingsX = 0;

		Ui.saveAlpha = 0;
		Audio.important = false;

		Dungeon.dark = 1;

		shader.begin();
		float a = Random.newFloat((float) (Math.PI * 2));

		shader.setUniformf("tx", (float) Math.cos(a));
		shader.setUniformf("ty", (float) Math.sin(a));
		shader.end();

		Dungeon.white = 0;

		Camera.did = false;

		pauseMenuUi = new Area(true);

		Collisions collisions = new Collisions();

		World.world.setContactListener(collisions);
		World.world.setContactFilter(collisions);

		this.setupUi();

		Dungeon.setBackground2(new Color(Level.colors[Dungeon.level.uid]));

		this.console = new Console();

		if (Dungeon.level instanceof DesertLevel) {
			Achievements.unlock(Achievements.REACH_DESERT);
			Achievements.unlock(Achievements.UNLOCK_DEW_VIAL);
		} else if (Dungeon.level instanceof LibraryLevel) {
			Achievements.unlock(Achievements.REACH_LIBRARY);
		} else if (Dungeon.level instanceof ForestLevel) {
			Achievements.unlock(Achievements.REACH_FOREST);
		} else if (Dungeon.level instanceof BloodLevel) {
			Achievements.unlock(Achievements.REACH_BLOOD);
		} else if (Dungeon.level instanceof IceLevel) {
			Achievements.unlock(Achievements.REACH_ICE);
		} else if (Dungeon.level instanceof TechLevel) {
			Achievements.unlock(Achievements.REACH_TECH);
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

		Dungeon.darkR = Dungeon.MAX_R;

		portalMod = 0;
		Tween.to(new Tween.Task(1, 1f) {
			@Override
			public float getValue() {
				return portalMod;
			}

			@Override
			public void setValue(float value) {
				portalMod = value;
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}

			@Override
			public void onEnd() {
				super.onEnd();
			}
		});

		if (resetMusic) {
			resetMusic = false;
			Audio.reset();
		}

		// Audio.play(toPlay);

		/*if (Dungeon.depth > 0) {
			horn();
		}*/
	}

	public static String toPlay;
	public static boolean resetMusic;

	private boolean wasHidden;
	private String depth;
	private float w;

	private float lastSave;
	private Tween.Task lastTask;
	private Tween.Task lastTaskSize;

	@Override
	public void setPaused(boolean paused) {
		super.setPaused(paused);

		Dungeon.dark = 1;
		Dungeon.darkR = Dungeon.MAX_R;

		if (this.isPaused()) {
			if (!Player.instance.isDead()) {
				if (mv == 0) {
					return;
				}

				this.mv = -256;
				depth = Dungeon.level.formatDepth();
				this.wasHidden = !UiMap.instance.isOpen();

				Graphics.layout.setText(Graphics.medium, depth);
				this.w = Graphics.layout.width;

				Graphics.layout.setText(Graphics.small, Random.getSeed());
				this.ww = Graphics.layout.width;

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

				Tween.to(new Tween.Task(0.2f * Settings.music, 0.3f) {
					@Override
					public float getValue() {
						return 1 * Settings.music;
					}

					@Override
					public void setValue(float value) {
						Audio.current.setVolume(value);
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
			}
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

			Tween.to(new Tween.Task(1f * Settings.music, 0.3f) {
				@Override
				public float getValue() {
					return 0.5f * Settings.music;
				}

				@Override
				public void setValue(float value) {
					Audio.current.setVolume(value);
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

		boolean old = (Dungeon.game.getState() instanceof LoadState);

		if (Player.instance != null && !Player.instance.isDead()) {
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

	private static long hsfx = -1;

	public static void horn() {
		if (Settings.sfx == 0) {
			return;
		}

		Sound sound = Audio.getSound("airhorn");
		sound.stop(hsfx);
		Camera.shake(5);
		hsfx = sound.play(Settings.sfx);
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
					Dungeon.level.addLight(x * 16, y * 16, 4f, 4f);
				}
			}
		}
	}

	public static boolean restart;
	public static boolean startTween;
	public static byte id;
	public static boolean newGame;
	public static boolean portal;

	private boolean set;

	public static boolean triggerPause;
	private float t;

	private boolean won;

	@Override
	public void update(float dt) {
		t += dt;

		if (t >= 0.1f && triggerPause && !isPaused()) {
			triggerPause = false;
			setPaused(true);
		}

		Dungeon.setBackground2((Level.colors[Dungeon.level.uid]));

		UiInventory.justUsed = Math.max(0, UiInventory.justUsed - 1);

		if (Dungeon.depth == -2) {
			Upgrade.updateEvent = false;
		}

		if (startTween) {
			Audio.play("Void");
			startTween = false;
			fromCenter = true;

			Tween.to(new Tween.Task(0, 1f) {
				@Override
				public float getValue() {
					return portalMod;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}

				@Override
				public void setValue(float value) {
					portalMod = value;
				}

				@Override
				public void onEnd() {
					if (newGame) {
						newGame = false;
						Dungeon.newGame(true, 1);
					} else if (restart) {
						restart = false;

						Dungeon.grayscale = 0;

						if (Dungeon.depth == -3) {
							Dungeon.newGame(false, -3);
						} else {
							Dungeon.newGame(true, 1);
						}
					} else if (portal) {
						portal = false;

						if (Dungeon.depth == -2) {
							Dungeon.goToSelect = true;
						/*} else if (Dungeon.depth == 4) {
							Dungeon.game.setState(new WonState());*/
						} else {
							Dungeon.goToLevel(Dungeon.depth + 1);
							Player.instance.rotating = false;
							Dungeon.loadType = Entrance.LoadType.GO_DOWN;
						}

						Camera.noMove = false;

						Dungeon.setBackground2(new Color(0, 0, 0, 1));
						Player.sucked = false;
					} else {
						if (Dungeon.depth == -2) {
							Dungeon.goToSelect = true;
						} else {
							GameSave.inventory = true;
							Dungeon.toInventory = true;
							Dungeon.loadType = Entrance.LoadType.GO_DOWN;
							Dungeon.ladderId = id;
						}
					}
				}
			});
		}

		/*if (Input.instance.wasPressed("active")) {
			Object nil = null;
			nil.getClass();
		}*/ // Crash simulation

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

			if (lastSave >= 180f) {
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

		if (Input.instance.wasPressed("F")) {
			horn();

			if (!won) {
				won = true;
				Ui.ui.onWin();
			}
		}
		
		if (Version.debug) {
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
						int level = Dungeon.depth;
						Dungeon.newGame(true, level);
						Dungeon.setBackground2(new Color(0, 0, 0, 1));
					}
				});
			}
		}


		if (Version.debug) {
			if (Input.instance.wasPressed("F5")) {
				for (Room room : Dungeon.level.getRooms()) {
					if (room instanceof SecretRoom) {
						BombEntity.make(room);
						Dungeon.level.loadPassable();
						Dungeon.level.addPhysics();

						Point point = room.getRandomCell();

						if (point != null) {
							Player.instance.tp(point.x * 16, point.y * 16);
						}

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

						if (point != null) {
							Player.instance.tp(point.x * 16, point.y * 16);
						}

						break;
					}
				}
			} else if (Input.instance.wasPressed("F8")) {
				for (Room room : Dungeon.level.getRooms()) {
					if (room instanceof NpcSaveRoom && room != Player.instance.room) {
						Point point = room.getRandomFreeCell();

						if (point != null) {
							Player.instance.tp(point.x * 16, point.y * 16);
						}

						break;
					}
				}
			}

			if (Input.instance.wasPressed("F3")) {
				Ui.hideUi = !Ui.hideUi;
			} else if (Input.instance.wasPressed("F9")) {
				Ui.hideCursor = !Ui.hideCursor;
			}

			if (Input.instance.wasPressed("O")) {
				Ui.upscale = 1;
			} else if (Input.instance.isDown("I")) {
				Ui.upscale = Math.max(0.1f, Ui.upscale - dt * 3);
			} else if (Input.instance.isDown("P")) {
				Ui.upscale += dt * 3;
			}
		}

		if (Player.instance != null && !Player.instance.isDead()) {
			last += dt;

			if (last >= 1f) {
				last = 0;

				if (portalMod == 1) {
					checkMusic();
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

		if (Player.instance != null && (Player.instance.getHp() < lastHp || Player.dullDamage)) {
			if (!setFrames) {
				Player.dullDamage = false;
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
						if (Player.instance.getHp() + Player.instance.getGoldenHearts() + Player.instance.getIronHearts() > 1) {
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

				/*
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
				}*/
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

	public static void checkMusic() {
		if (Dungeon.game.getState() instanceof InGameState) {
			if (Dungeon.depth == -2 || Player.instance.room instanceof ShopRoom) {
				Audio.play("Shopkeeper");
			} else if (Player.instance.room instanceof PrebossRoom) {
				Audio.play("Gobbeon");
			} else if (Player.instance.room instanceof SecretRoom) {
				Audio.play("Serendipity");
			/*} else if (((Dungeon.depth == -3 && BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive")) || forceBoss)) {
				Audio.highPriority("Rogue");
			} else if (BurningKnight.instance != null && BurningKnight.instance.rage && !BurningKnight.instance.dest) {
				Audio.play("Cursed legend");*/
			} else if ((BurningKnight.instance == null || !(BurningKnight.instance.dest))) {
				/*if (!Player.instance.isDead() && Dungeon.depth > -1 && BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive") && !BurningKnight.instance.rage) {
					Audio.play("Rogue");
				} else {*/
					Audio.play(Dungeon.level.getMusic());
				//}
			}
		}
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

			Graphics.render(noise, Camera.game.position.x - Display.GAME_WIDTH / 2, Camera.game.position.y - Display.GAME_HEIGHT / 2, 0, 0, 0, false, false);

			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();

			if (Dungeon.depth == -2) {
				for (Upgrade upgrade : Upgrade.all) {
					upgrade.renderSigns();
				}
			}
		}


		Player.instance.renderBuffs();
	}

	private float ww;

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
			Camera.ui.translate(settingsX, this.mv);
			Camera.ui.update();

			Graphics.batch.setProjectionMatrix(Camera.ui.combined);

			pauseMenuUi.render();

			Graphics.print(this.depth, Graphics.medium, Display.UI_WIDTH / 2 - w / 2, 128 + 32 + 16);
			Graphics.print(Random.getSeed(), Graphics.small, Display.UI_WIDTH / 2 - ww / 2, 128 + 32 + 12);

			Camera.ui.translate(-settingsX, -this.mv);
			Camera.ui.update();
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		}

		if (portalMod < 1) {
			renderPortalOpen();
		}

		Achievements.render();
		Ui.ui.renderCursor();
		Ui.renderSaveIcon(1);
	}

	private void setupUi() {
		UiInventory inventory = new UiInventory(Player.instance.getInventory());

		Dungeon.ui.add(inventory);
		Dungeon.ui.add(new UiMap());

		int y = -24 + 16;

		int m = 0;

		if (Dungeon.depth == -2) {
			m = 1;
		}

		settingsFirst = (UiButton) this.pauseMenuUi.add(new UiButton("resume", Display.UI_WIDTH / 2, 128 + 32 + y - m * 24) {
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

		if (Dungeon.depth != -2) {
			m++;
			this.pauseMenuUi.add(new UiButton("quick_restart", Display.UI_WIDTH / 2, 128 + 32 - 24 + y) {
				@Override
				public void onClick() {
					super.onClick();

					if (!restart) {
						startTween = true;
						restart = true;
					}
				}
			}.setSparks(true));
		}

		this.pauseMenuUi.add(new UiButton("settings", Display.UI_WIDTH / 2, 128 + 32 - 24 * (m + 1) + y) {
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

		this.pauseMenuUi.add(new UiButton("save_and_exit", Display.UI_WIDTH / 2, 128 + 32 - 24 * (m + 2) + y) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(0f * Settings.music, 0.3f) {
					@Override
					public float getValue() {
						return 0.5f * Settings.music;
					}

					@Override
					public void setValue(float value) {
						Audio.current.setVolume(value);
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});

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

		if (!Player.instance.isDead()) {
			this.pauseMenuUi.show();
			this.pauseMenuUi.selectFirst();
		}
	}
}