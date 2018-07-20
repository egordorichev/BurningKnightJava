package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Log;

public class UiClass extends UiButton {
	public final Player.Type type;
	private static String[] textures = new String[] { "ui-warrior", "ui-wizard", "ui-archer" };
	private TextureRegion region;
	private String name;
	private float nw;

	private TextureRegion top = Graphics.getTexture("ui-cart_top");
	private TextureRegion topLeft = Graphics.getTexture("ui-card_top_left");
	private TextureRegion topRight = Graphics.getTexture("ui-cart_top_right");
	private TextureRegion center = Graphics.getTexture("ui-card_center");
	private TextureRegion left = Graphics.getTexture("ui-card_left");
	private TextureRegion right = Graphics.getTexture("ui-card_right");
	private TextureRegion bottom = Graphics.getTexture("ui-card_bottom");
	private TextureRegion bottomLeft = Graphics.getTexture("ui-card_bottom_left");
	private TextureRegion bottomRight = Graphics.getTexture("ui-card_bottom_right");

	private TextureRegion warrior = Graphics.getTexture("ui-warrior");
	private TextureRegion ranger = Graphics.getTexture("ui-archer");
	private TextureRegion mage = Graphics.getTexture("ui-wizard");

	public UiClass(Player.Type type, int x, int y) {
		super(null, x, y);

		this.type = type;
		this.w = 96;
		this.h = 128;

		Log.info(this.type.toString());
		this.name = Locale.get(this.type.toString().toLowerCase());

		Graphics.layout.setText(Graphics.medium, this.name);
		this.nw = Graphics.layout.width;

		this.region = Graphics.getTexture(textures[this.id]);
	}

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

		float yy = this.y + h / 2 - (w - warrior.getRegionWidth() / 2) / 2 - 5;

		switch (this.type) {
			case WARRIOR: default:
				Graphics.render(warrior, this.x, yy, 0, warrior.getRegionWidth() / 2, warrior.getRegionHeight() / 2, false, false);
				break;
			case RANGER:
				Graphics.render(ranger, this.x, yy, 0, warrior.getRegionWidth() / 2, warrior.getRegionHeight() / 2, false, false);
				break;
			case WIZARD:
				Graphics.render(mage, this.x, yy, 0, warrior.getRegionWidth() / 2, warrior.getRegionHeight() / 2, false, false);
				break;
		}
	}

	private float mod = 1f;
}