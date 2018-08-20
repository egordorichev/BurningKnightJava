package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Tween;

public class UiMap extends UiEntity {
	{
		depth = 16;
		isSelectable = false;
	}

	public static UiMap instance;

	public static boolean large;
	private float xc;
	private float yc;
	private static TextureRegion frame = Graphics.getTexture("ui-minimap");
	private float my;
	private UiButton plus;
	private UiButton minus;

	private UiButton plusLarge;
	private UiButton minusLarge;

	private UiButton hide;
	private UiButton show;
	private float ly;

	private boolean hadOpen;

	public boolean isOpen() {
		return large || my == 0;
	}

	public void hide() {
		Tween.to(new Tween.Task(96, 0.4f) {
			@Override
			public float getValue() {
				return my;
			}

			@Override
			public void setValue(float value) {
				my = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				did = false;
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}
		});

		did = true;
		GlobalSave.put("hide_minimap", true);
	}

	public void remove() {
		if (!this.did) {
			toRemove = true;

			if (this.my == 0) {
				this.hide();
				GlobalSave.put("hide_minimap", false);
			}

			if (large) {
				this.hideHuge();
			}
		}
	}

	public void show() {
		Tween.to(new Tween.Task(0, 0.4f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return my;
			}

			@Override
			public void setValue(float value) {
				my = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				did = false;
			}
		});

		did = true;
		GlobalSave.put("hide_minimap", false);
	}

	private boolean did;

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}

	@Override
	public void init() {
		super.init();

		instance = this;
		setSize();

		if (GlobalSave.isTrue("hide_minimap")) {
			this.my = 96;
		}

		final UiMap self = this;

		hide = new UiImageButton("ui-hide_button", (int) x + 4, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();
				hide();
			}

			@Override
			public void render() {
				if (!large && Dungeon.depth >= 0) {
					this.y = self.y + my - 3;
					super.render();
				}
			}

			@Override
			public void update(float dt) {
				if (!large && Dungeon.depth >= 0) {
					super.update(dt);
				}
			}
		};

		Dungeon.ui.add(hide);

		show = new UiImageButton("ui-show", Display.GAME_WIDTH - 4 - 9, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();
				show();
			}

			private float vl;

			@Override
			public void update(float dt) {
				if (!large && Dungeon.depth >= 0) {
					super.update(dt);
				}
			}

			@Override
			public void render() {
				if (!large && !Player.instance.isDead() && Dungeon.depth >= 0) {
					float dx = Input.instance.uiMouse.x - this.x - 4;
					float dy = Input.instance.uiMouse.y - this.y - 4;
					float d = (float) Math.sqrt(dx * dx + dy * dy);
					float dt = Gdx.graphics.getDeltaTime() * 8;

					if (d > 100f) {
						vl += (1 - vl) * dt;
					} else {
						vl += (-vl) * dt;
					}

					this.y = 96 - my + Display.GAME_HEIGHT - MathUtils.clamp(0, 8, 8 - vl * 8);
					super.render();
				}
			}
		};

		Dungeon.ui.add(show);

		this.plus = new UiImageButton("ui-plus", (int) x + 39, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();

				plus();
			}

			@Override
			public void update(float dt) {
				if (!large && Dungeon.depth >= 0) {
					super.update(dt);
				}
			}

			@Override
			public void render() {
				if (!large && Dungeon.depth >= 0) {
					this.y = self.y + my - 3;
					super.render();
				}
			}
		};

		Dungeon.ui.add(plus);

		this.minus = new UiImageButton("ui-minus", (int) x + 51, (int) y - 3) {
			@Override
			public void onClick() {
				super.onClick();

				minus();
			}

			@Override
			public void update(float dt) {
				if (!large && Dungeon.depth >= 0) {
					super.update(dt);
				}
			}

			@Override
			public void render() {
				if (!large && Dungeon.depth >= 0) {
					this.y = self.y + my - 3;
					super.render();
				}
			}
		};

		Dungeon.ui.add(minus);

		this.plusLarge = new UiImageButton("ui-plus", (int) (x + w - 30), (int) y) {
			@Override
			public void onClick() {
				super.onClick();

				plus();
			}

			@Override
			public void update(float dt) {
				if (large && Dungeon.depth >= 0) {
					super.update(dt);
				}
			}

			public void render() {
				if (large && Dungeon.depth >= 0) {
					y = self.y + ly - 2;
					super.render();
				}
			}
		};

		Dungeon.ui.add(plusLarge);

		this.minusLarge = new UiImageButton("ui-minus", (int) (x + w - 20), (int) y) {
			@Override
			public void onClick() {
				super.onClick();

				minus();
			}

			@Override
			public void update(float dt) {
				if (large && Dungeon.depth >= 0) {
					super.update(dt);
				}
			}

			public void render() {
				if (large && Dungeon.depth >= 0) {
					super.render();
					y = self.y + ly - 2;
				}
			}
		};

		Dungeon.ui.add(minusLarge);
	}

	private float speed = 0.5f;

	protected void plus() {
		zoom = Math.min(2f, zoom + speed);
		GlobalSave.put("minimap_zoom", zoom);
	}

	protected void minus() {
		zoom = Math.max(0.25f, zoom - speed);
		GlobalSave.put("minimap_zoom", zoom);
	}

	public void setSize() {
		this.w = large ? Display.GAME_WIDTH - 20 : 64;
		this.h = large ? Display.GAME_HEIGHT - 20 : Math.min(this.w, Display.GAME_HEIGHT);

		this.x = Math.round(Display.GAME_WIDTH - this.w - (large ? 10 : 4));
		this.y = Math.round(Display.GAME_HEIGHT - this.h - (large ? 10 : 4));
	}

	@Override
	public void update(float dt) {
		if (Dungeon.depth < 0) {
			return;
		}

		super.update(dt);

		if (Dialog.active == null) {
			if (Input.instance.wasPressed("zoom_out")) {
				minus();
			}

			if (Input.instance.wasPressed("zoom_in")) {
				plus();
			}

			if (!did && Input.instance.wasPressed("toggle_minimap") && !large) {
				if (my == 0) {
					hide();
				} else {
					show();
				}
			}

			if (large) {
				float s = 30f * (1 / zoom);

				if (Input.instance.isDown("left")) {
					xc += s * dt;
				}

				if (Input.instance.isDown("right")) {
					xc -= s * dt;
				}

				if (Input.instance.isDown("up")) {
					yc -= s * dt;
				}

				if (Input.instance.isDown("down")) {
					yc += s * dt;
				}

				float ix = -Input.instance.getAxis("mouseX") * s;
				float iy = Input.instance.getAxis("mouseY") * s;

				if (Math.sqrt(ix * ix + iy * iy) > 0.8) {
					xc += ix * dt;
					yc += iy * dt;
				}
			}

			if (Input.instance.wasPressed("map") && !Dungeon.game.getState().isPaused() && !did) {
				if (!large) {
					openHuge();
				} else {
					hideHuge();
				}
			}
		}
	}

	public void openHuge() {
		xc = 0;
		yc = 0;

		if (!large && my != 96) {
			hadOpen = true;
			did = true;

			Tween.to(new Tween.Task(96, 0.1f) {
				@Override
				public float getValue() {
					return my;
				}

				@Override
				public void setValue(float value) {
					my = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();

					doLarge();
				}
			});
		} else {
			hadOpen = false;
			doLarge();
		}
	}

	private void doLarge() {
		large = true;
		setSize();

		ly = -Display.GAME_HEIGHT;

		Tween.to(new Tween.Task(0, 0.3f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return ly;
			}

			@Override
			public void setValue(float value) {
				ly = value;
			}

			@Override
			public void onEnd() {
				did = false;
			}
		});
	}

	public void hideHuge() {
		Tween.to(new Tween.Task(-Display.GAME_HEIGHT, 0.1f) {
			@Override
			public float getValue() {
				return ly;
			}

			@Override
			public void setValue(float value) {
				ly = value;
			}

			@Override
			public void onEnd() {
				large = false;
				setSize();
				did = false;

				if (!toRemove && hadOpen) {
					Tween.to(new Tween.Task(0, 0.2f, Tween.Type.BACK_OUT) {
						@Override
						public float getValue() {
							return my;
						}

						@Override
						public void setValue(float value) {
							my = value;
						}

						@Override
						public void onEnd() {
							did = false;
						}
					});

					did = true;
				}
			}
		});

		hadOpen = false;
		did = true;
	}

	private boolean toRemove;

	private float zoom = GlobalSave.getFloat("minimap_zoom") == 0 ? 1f : GlobalSave.getFloat("minimap_zoom");
	private Color bg = Color.valueOf("#2a2f4e");
	private Color border = Color.valueOf("#0e071b");

	private static TextureRegion topLeft = Graphics.getTexture("ui-map_top_left");
	private static TextureRegion top = Graphics.getTexture("ui-map_top");
	private static TextureRegion topRight = Graphics.getTexture("ui-map_top_right");

	private static TextureRegion left = Graphics.getTexture("ui-map_left");
	private static TextureRegion right = Graphics.getTexture("ui-map_right");

	private static TextureRegion bottomLeft = Graphics.getTexture("ui-map_bottom_left");
	private static TextureRegion bottom = Graphics.getTexture("ui-map_bottom");
	private static TextureRegion bottomRight = Graphics.getTexture("ui-map_bottom_right");

	private static Color color = Color.valueOf("#424c6e");

	@Override
	public void render() {
		if ((my == 96 && !large) || Dungeon.depth < 0) {
			return;
		}

		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (large) {
			Graphics.shape.setColor(color.r, color.g, color.b, 0.8f);
			Graphics.shape.rect(this.x + 1, this.y + 1 + ly, this.w - 1, this.h - 1);
		} else {
			Graphics.shape.setColor(bg);
			Graphics.shape.rect(this.x + 1, this.y + this.my + 1, this.w - 2, this.h - 2);
		}

		Graphics.endAlphaShape();
		Graphics.batch.end();

		Graphics.shadows.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
			Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

		Graphics.text.begin();

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
		float s = Math.round((large ? 6 : 4) * zoom);

		float px = Player.instance.x + Player.instance.w / 2f - (large ? xc * s : 0);
		float py = Player.instance.y + Player.instance.h / 2f - (large ? yc * s : 0);

		float mx = -px / (16f / s) + this.w / 2 + (large ? xc : 0);
		float my = -py / (16f / s) + this.h / 2 + (large ? yc : 0);

		float o = 1f;// * zoom;

		int xx;
		int yy;

		Graphics.shape.setColor(border);

		float cx = px - w * 2 / zoom;
		float cy = py - h * 2 / zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + w * 4 / zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + h * 4 / zoom) / 16) + 1);

		xx = Math.max(0, sx);

		for (int x = Math.max(0, sx); x < Math.min(fx, Level.getWidth()); x++) {
			yy = Math.max(0, sy);

			for (int y = Math.max(0, sy); y < Math.min(fy, Level.getHeight()); y++) {

				if (Dungeon.level.explored(x, y)) {
					Graphics.shape.rect(xx * s - o + mx, yy * s - o + my, s + o * 2, s + o * 2);
				}

				yy ++;
			}

			xx ++;
		}

		xx = Math.max(0, sx);

		for (int x = Math.max(0, sx); x < Math.min(fx, Level.getWidth()); x++) {
			yy = Math.max(0, sy);

			for (int y = Math.max(0, sy); y < Math.min(fy, Level.getHeight()); y++) {
				if (Dungeon.level.explored(x, y)) {
					int i = Level.toIndex(x, y);
					byte t = Dungeon.level.liquidData[i];

					if (t == 0) {
						t = Dungeon.level.get(i);
					}

					Graphics.shape.setColor(Terrain.getColor(t));
					Graphics.shape.rect(xx * s + mx, yy * s + my, s, s);
				}

				yy ++;
			}

			xx ++;
		}

		float plx = Player.instance.x / 16f;
		float ply = Player.instance.y / 16f;

		Graphics.shape.setColor(0, 1, 0, 1);
		Graphics.shape.rect(plx * s + s / 4f + mx, ply * s + s / 4f + my, s / 2f, s / 2f);
		Graphics.shape.setColor(1, 1, 1, 1);

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		Graphics.text.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
			Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

		Graphics.shadows.begin();

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.begin();

		if (!large) {
			Graphics.batch.draw(texture, this.x + 1, this.y + 1 + this.my, this.w - 2, this.h - 2,
				0, 0, (int) this.w - 2, (int) this.h - 2, false, true);

			Graphics.render(frame, x, y + this.my);
		} else {
			Graphics.batch.draw(texture, this.x + 1, this.y + 1 + ly, this.w - 2, this.h - 2,
				0, 0, (int) this.w - 2, (int) this.h - 2, false, true);

			renderLarge();
		}
	}

	private void renderLarge() {
		float sx = (this.w - 10);
		float sy = (this.h - 10);

		Graphics.render(bottomLeft, x, y + ly);
		Graphics.render(bottom, x + bottomLeft.getRegionWidth(), y + ly, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomRight, x + this.w - bottomRight.getRegionWidth(), y + ly);

		Graphics.render(left, x, y + ly + bottomLeft.getRegionHeight(), 0, 0, 0,  false, false, 1, sy);
		Graphics.render(right, x + this.w - right.getRegionWidth(), y + ly + bottomLeft.getRegionHeight(), 0, 0, 0,  false, false, 1, sy);

		Graphics.render(topLeft, x, y + ly + h - topLeft.getRegionHeight());
		Graphics.render(top, x + topLeft.getRegionWidth(), y + ly + h - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topRight, x + this.w - topRight.getRegionWidth(), y + ly + h - topLeft.getRegionHeight());
	}
}