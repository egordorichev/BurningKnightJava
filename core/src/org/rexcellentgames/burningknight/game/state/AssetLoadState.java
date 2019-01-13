package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Assets;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.util.Log;

public class AssetLoadState extends State {
	public static final boolean START_TO_MENU = !Version.debug;
	public static final boolean QUICK = true;
	public static boolean done = false;
	private float a;
	public static TextureRegion logo;

	private float t;
	private boolean did;

	@Override
	public void update(float dt) {
		t += dt;

		if (t > 0.01f && !did) {
			did = true;

			if (QUICK || !START_TO_MENU) {
				Assets.finishLoading();
				finish();
				tweened = true;
			}
		}

		super.update(dt);

		if (!tweened) {
			this.a = Math.min(1, a + dt);
		}

		if (!tweened && Assets.updateLoading()) {
			finish();
			tweened = true;
		}
	}

	@Override
	public void init() {
		super.init();
		logo = new TextureRegion(new Texture(Gdx.files.internal("rexcellent_logo_pixel.png")));
	}

	private boolean tweened;

	private void finish() {
		done = true;

		if (!START_TO_MENU) {
			Gdx.graphics.setTitle(Dungeon.title);
			GameSave.Info info = GameSave.peek(SaveManager.slot);
			Log.error("Game slot was " + (info.free ? "free" : "not free"));
			Dungeon.goToLevel((info.free ? -2 : info.depth));
			return;
		}

		Gdx.graphics.setTitle(Dungeon.title);

		if (START_TO_MENU) {
			if (Version.showAlphaWarning) {
				Dungeon.game.setState(new AlphaWarningState());
			} else {
				Dungeon.game.setState(new MainMenuState());
			}
		}
	}

	@Override
	public void render() {
		super.render();

		Graphics.render(logo, (Display.GAME_WIDTH - 128) / 2, (Display.GAME_HEIGHT - 128) / 2);

		/*

		Graphics.batch.setColor(1, 1, 1, this.a);
		Graphics.batch.draw(region, (Display.GAME_WIDTH - region.getWidth()) / 2, (Display.GAME_HEIGHT - region.getHeight()) / 2);
*/

		Gdx.graphics.setTitle(Dungeon.title + " " + Math.floor(Assets.manager.getProgress() * 100) + "%");
	}
}
