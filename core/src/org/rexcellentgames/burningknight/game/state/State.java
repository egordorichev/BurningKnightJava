package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Noise;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.rooms.special.NpcSaveRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.*;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class State {
	protected Area pauseMenuUi;
	protected float portalMod;

	protected void renderPortalOpen() {
		if (player == null) {
			player = Graphics.getTexture("props-gobbo_full");
		}

		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.nil.combined);
		Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360, 360, 360);
		Dungeon.setBackground2(new Color(cl.r * 0.04f, cl.g * 0.04f,
			cl.b * 0.04f, 1f));

		float vv = Math.max(0, 1 - portalMod);

		for (int i = 0; i < 65; i++) {
			float s = i * 0.015f;
			float mx = (Noise.instance.noise(Dungeon.time * 0.25f + s) * 96) * vv;
			float my = (Noise.instance.noise(3 + Dungeon.time * 0.25f + s) * 96) * vv;
			float v = ((float) i) / 65f;

			Color color = ColorUtils.HSV_to_RGB((Dungeon.time * 20 - i * 1.4f) % 360, 360, 360);
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 1f);

			float a = (float) (Math.PI * i * 0.2f) + Dungeon.time * 2f;
			float c = fromCenter ? (-194 * portalMod) : (portalMod * Display.UI_WIDTH);
			float w = i * 2 + 64 + c;

			if (w <= 0) {
				continue;
			}

			float d = i * 2.5f * (i * 0.01f + 0.99f);

			if (fromCenter) {
				d = d * (1 - portalMod);
			} else {
				d += c;
			}

			if (d <= 0) {
				continue;
			}

			float x = (float) (Math.cos(a) * d) + Display.GAME_WIDTH / 2 + mx * (((float) 56 - i) / 56);
			float y = (float) (Math.sin(a) * d) + Display.GAME_HEIGHT / 2 + my * (((float) 56 - i) / 56);

			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 1f, 1f, (float) Math.toDegrees(a + 0.1f));
			//Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 1f);
			//Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 0.9f, 0.9f, (float) Math.toDegrees(a + 0.1f));
		}


		float i = 32;
		float mx = (Noise.instance.noise(Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56 - i) / 56);
		float my = (Noise.instance.noise(3 + Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56 - i) / 56);

		Graphics.endAlphaShape();

		if (!(this instanceof MainMenuState || this instanceof InGameState)) {
			/*Graphics.batch.setProjectionMatrix(Camera.nil.combined);
			Graphics.render(player, Display.GAME_WIDTH / 2 + mx, Display.GAME_HEIGHT / 2 + my, Dungeon.time * 650, 8, 8, false, false);
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);*/
		}

		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
	}

	protected void renderPortal() {
		if (player == null) {
			player = Graphics.getTexture("props-gobbo_full");
		}

		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.nil.combined);
		Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360, 360, 360);
		Dungeon.setBackground2(new Color(cl.r * 0.04f, cl.g * 0.04f,
			cl.b * 0.04f, 1f));

		for (int i = 0; i < 65; i++) {
			float s = i * 0.015f;
			float mx = (Noise.instance.noise(Dungeon.time * 0.25f + s) * 96);
			float my = (Noise.instance.noise(3 + Dungeon.time * 0.25f + s) * 96);
			float v = ((float) i) / 65f;

			Color color = ColorUtils.HSV_to_RGB((Dungeon.time * 20 - i * 1.4f) % 360, 360, 360);
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 1f);

			float a = (float) (Math.PI * i * 0.2f) + Dungeon.time * 2f;
			float w = i * 2 + 64;
			float d = i * 2.5f * (i * 0.01f + 0.99f);
			float x = (float) (Math.cos(a) * d) + Display.GAME_WIDTH / 2 + mx * (((float) 56 - i) / 56);
			float y = (float) (Math.sin(a) * d) + Display.GAME_HEIGHT / 2 + my * (((float) 56 - i) / 56);

			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 1f, 1f, (float) Math.toDegrees(a + 0.1f));
			//Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 1f);
			//Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 0.9f, 0.9f, (float) Math.toDegrees(a + 0.1f));
		}


		float i = 32;
		float mx = (Noise.instance.noise(Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56 - i) / 56);
		float my = (Noise.instance.noise(3 + Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56 - i) / 56);

		Graphics.endAlphaShape();

		if (!(this instanceof MainMenuState)) {
			/*Graphics.batch.setProjectionMatrix(Camera.nil.combined);
			Graphics.render(player, Display.GAME_WIDTH / 2 + mx, Display.GAME_HEIGHT / 2 + my, Dungeon.time * 650, 8, 8, false, false);
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);*/
		}

		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
	}

	private boolean paused;
	public static TextureRegion player;
	protected boolean fromCenter;

	public void onPause() {

	}

	public void onUnpause() {

	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;

		if (this.paused) {
			this.onPause();
		} else {
			this.onUnpause();
		}
	}

	public State() {

	}

	public void init() {

	}

	public void destroy() {

	}

	public void update(float dt) {

	}

	public void render() {

	}

	public void renderUi() {

	}

	public void resize(int width, int height) {

	}

	public static void transition(final Runnable runnable) {
		Tween.to(new Tween.Task(0, 0.2f) {
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
				runnable.run();

				Tween.to(new Tween.Task(1, 0.2f) {
					@Override
					public float getValue() {
						return Dungeon.dark;
					}

					@Override
					public void setValue(float value) {
						Dungeon.dark = value;
					}
				});
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}
		});
	}

	public static float settingsX;
	public boolean addedSettings;

	public void addSettings() {
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
	protected boolean wasInSettings;

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

		float s = 13;
		float st = 60 - 5f;

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
				Gdx.graphics.setUndecorated(Settings.borderless);
				super.onClick();
			}
		}.setOn(Settings.borderless)));

		currentSettings.add(pauseMenuUi.add(new UiChoice("side_art", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 3)) {
			@Override
			public void onUpdate() {
				Settings.side_art = this.getCurrent();

				if (getCurrent() != 0) {
					final float upscale = Math.min(((float) Gdx.graphics.getWidth()) / Display.GAME_WIDTH, ((float) Gdx.graphics.getHeight()) / Display.GAME_HEIGHT) * Ui.upscale;

					Gdx.graphics.setWindowedMode((int) upscale * Display.GAME_WIDTH, (int) upscale * Display.GAME_WIDTH);
				}
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

		currentSettings.add(pauseMenuUi.add(new UiCheckbox("fullscreen", (int) (Display.UI_WIDTH * 2.5f), (int) (st + s * 11)) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.fullscreen = this.isOn();

				if (Settings.fullscreen) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				} else {
					Gdx.graphics.setWindowedMode(Display.UI_WIDTH_MAX * 2, Display.UI_HEIGHT_MAX * 2);
				}
			}

			@Override
			public void render() {
				setOn(Settings.fullscreen);
				super.render();
			}
		}.setOn(Settings.fullscreen)));
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
						GameSave.runId = 0;
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
			private int clicks;

			@Override
			public void onClick() {
				clicks++;

				if (clicks == 15) {
					for (String id : NpcSaveRoom.saveOrder) {
						GlobalSave.put("npc_" + id + "_saved", true);
					}

					GlobalSave.put("all_npcs_saved", true);
					Audio.playSfx("curse_lamp", 1f, 1f);
				}

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
}