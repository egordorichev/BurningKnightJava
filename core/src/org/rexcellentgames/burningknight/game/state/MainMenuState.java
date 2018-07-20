package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Tween;
import v4lk.lwbd.util.Beat;

import java.util.ArrayList;

public class MainMenuState extends State {
	private Beat[] beats;
	public static MainMenuState instance;
	private static TextureRegion logo = Graphics.getTexture("artwork_logo (sticker)");
	private static TextureRegion bg = Graphics.getTexture("menu-bk");
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float logoX = 0;
	private float logoY = Display.GAME_HEIGHT;
	private float versionX;
	private float versionY = -32;
	public static float cameraX = Display.GAME_WIDTH / 2;
	public static float cameraY = Display.GAME_HEIGHT / 2;
	public static float MOVE_T = 0.2f;
	private float bgX = -180;

	public static UiEntity first;

	@Override
	public void init() {
		SettingsState.added = false;
		InputSettingsState.added = false;
		SlotSelectState.added = false;
		GraphicsSettingsState.added = false;
		AudioSettingsState.added = false;
		ClassSelectState.added = false;

		cameraX = Display.GAME_WIDTH / 2;
		cameraY = Display.GAME_HEIGHT / 2;

		Audio.play("Burning Knight");

		Dungeon.buildDiscordBadge();

		float v = 64;
		logoX = v;

		instance = this;
		Dungeon.area.add(Camera.instance);
		Camera.target = null;

		/*try {
			File audioFile = Gdx.files.internal("music/Fatiga.mp3").file();
			FileInputStream stream = new FileInputStream(audioFile);
			Decoder decoder = new JLayerMp3Decoder(stream);
			this.beats = BeatDetector.detectBeats(decoder);
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("play", -128, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				SlotSelectState.add();
				Dungeon.ui.select(SlotSelectState.first);

				Tween.to(new Tween.Task(-Display.GAME_HEIGHT / 2, MainMenuState.MOVE_T) {
					@Override
					public float getValue() {
						return cameraY;
					}

					@Override
					public void setValue(float value) {
						cameraY = value;
					}
				});
			}
		}.setSparks(true)));

		first = buttons.get(0);
		Dungeon.ui.select(first);

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("settings", (int) (Display.GAME_WIDTH + 128 + v), (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				super.onClick();
				SettingsState.add();
				Dungeon.ui.select(SettingsState.first);

				Tween.to(new Tween.Task(Display.GAME_WIDTH * 1.5f, MainMenuState.MOVE_T) {
					@Override
					public float getValue() {
						return cameraX;
					}

					@Override
					public void setValue(float value) {
						cameraX = value;
					}
				});
			}
		}));

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("exit", -128, (int) (128 - 24 * 3.5f)) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				transition(new Runnable() {
					@Override
					public void run() {
						Gdx.app.exit();
					}
				});
			}
		}));

		Tween.to(new Tween.Task(0, 1f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return logoY;
			}

			@Override
			public void setValue(float value) {
				logoY = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				for (final UiButton button : buttons) {
					Tween.to(new Tween.Task(Display.GAME_WIDTH / 2 + v, 0.6f, Tween.Type.BACK_OUT) {
						@Override
						public float getValue() {
							return button.x;
						}

						@Override
						public void setValue(float value) {
							button.x = value;
						}
					});
				}
			}
		});

		Tween.to(new Tween.Task(0, MainMenuState.MOVE_T) {
			@Override
			public float getValue() {
				return versionY;
			}

			@Override
			public void setValue(float value) {
				versionY = value;
			}
		}).delay(0.7f);

		Tween.to(new Tween.Task(0, 0.3f) {
			@Override
			public float getValue() {
				return bgX;
			}

			@Override
			public void setValue(float value) {
				bgX = value;
			}
		});
	}

	@Override
	public void renderUi() {
		super.render();

		Camera.ui.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
		Camera.ui.update();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Graphics.render(bg, bgX, 0);

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);


		float sx = (float) (0.8f + Math.sin(Dungeon.time / 1.5f) / 40);
		float sy = (float) (0.8f + Math.cos(Dungeon.time) / 40);
		float a = (float) (Math.cos(Dungeon.time * 0.7f) * 3f);

		Graphics.render(logo, Display.GAME_WIDTH / 2 + logoX, 180 + logoY, a, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);
		Graphics.print(Version.string, Graphics.small, 2 + versionX, versionY + 2);
		Dungeon.ui.render();
		Ui.ui.renderCursor();
	}
}