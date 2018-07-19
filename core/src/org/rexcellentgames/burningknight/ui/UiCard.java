package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.state.ClassSelectState;
import org.rexcellentgames.burningknight.game.state.MainMenuState;
import org.rexcellentgames.burningknight.game.state.SlotSelectState;
import org.rexcellentgames.burningknight.util.Tween;

public class UiCard extends UiButton {
	public int id;
	public GameSave.Info info;

	public UiCard(int id, int x, int y) {
		super(null, x, y);

		this.id = id;
		this.w = 96;
		this.h = 128;
		this.scaleMod = 0.3f;

		this.info = GameSave.peek(this.id);

		if (!this.info.free && !this.info.error) {
			String typeName = this.info.type.toString();

			this.info.first = Locale.get(typeName.toLowerCase());
			Graphics.layout.setText(Graphics.medium, this.info.first);
			this.info.firstW = Graphics.layout.width;

			if (this.info.second == null) {
				this.info.second = "";
			}

			Graphics.layout.setText(Graphics.small, this.info.second);
			this.info.secondW = Graphics.layout.width;
		}
	}

	@Override
	public void onClick() {
		super.onClick();

		SaveManager.slot = this.id;

		if (this.info.free) {
			ClassSelectState.add();

			Tween.to(new Tween.Task(-Display.GAME_HEIGHT * 1.5f, MainMenuState.MOVE_T) {
				@Override
				public float getValue() {
					return MainMenuState.cameraY;
				}

				@Override
				public void setValue(float value) {
					MainMenuState.cameraY = value;
				}
			});
		} else {
			SlotSelectState.trans(this.info.depth);
		}
	}

	private TextureRegion top = Graphics.getTexture("ui-cart_top");
	private TextureRegion topLeft = Graphics.getTexture("ui-card_top_left");
	private TextureRegion topRight = Graphics.getTexture("ui-cart_top_right");
	private TextureRegion center = Graphics.getTexture("ui-card_center");
	private TextureRegion left = Graphics.getTexture("ui-card_left");
	private TextureRegion right = Graphics.getTexture("ui-card_right");
	private TextureRegion bottom = Graphics.getTexture("ui-card_bottom");
	private TextureRegion bottomLeft = Graphics.getTexture("ui-card_bottom_left");
	private TextureRegion bottomRight = Graphics.getTexture("ui-card_bottom_right");
	private TextureRegion cross = Graphics.getTexture("ui-new");

	private TextureRegion warrior = Graphics.getTexture("ui-warrior");
	private TextureRegion ranger = Graphics.getTexture("ui-archer");
	private TextureRegion mage = Graphics.getTexture("ui-wizard");

	@Override
	public void render() {
		float w = this.w * scale;
		float h = this.h * scale;

		float x = this.x - w / 2;
		float y = this.y - h / 2;

		float sx = (w - 10);
		float sy = (h - 11);

		Graphics.render(top, x + topLeft.getRegionWidth(), y + h - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topLeft, x, y + h - topLeft.getRegionHeight());
		Graphics.render(topRight, x + w - topRight.getRegionWidth(), y + h - topRight.getRegionHeight());

		Graphics.render(left, x, y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(right, x + w - right.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(center, x + left.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, sx, sy);

		Graphics.render(bottom, x+ bottomLeft.getRegionWidth(),
			y, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomLeft, x, y);
		Graphics.render(bottomRight, x + w - topRight.getRegionWidth(), y);

		if (this.info.free && !this.info.error) {
			Graphics.render(cross, this.x, this.y, 0, cross.getRegionWidth() / 2, cross.getRegionHeight() / 2, false, false, scale, scale);
		}

		if (!this.info.free && !this.info.error) {
			float yy = this.y + h / 2 - (w - warrior.getRegionWidth() / 2) / 2 - 5;

			switch (this.info.first) {
				case "Warrior": default:
					Graphics.render(warrior, this.x, yy, 0, warrior.getRegionWidth() / 2, warrior.getRegionHeight() / 2, false, false);
					break;
			}

			// Graphics.print(this.info.first, Graphics.medium, this.x - this.info.firstW / 2, this.y + h / 2- 32);
			Graphics.print(this.info.second, Graphics.small, this.x - this.info.secondW / 2, y + 16);
		}
	}
}