package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.save.GameSave;
import org.rexellentgames.dungeon.entity.level.save.SaveManager;

public class UiCard extends UiButton {
	public int id;
	public GameSave.Info info;
	private static Color color = Color.valueOf("#5d5d5d");

	public UiCard(int id, int x, int y) {
		super(null, x, y);

		this.id = id;
		this.w = 96;
		this.h = 128;
		this.scaleMod = 0.3f;

		this.info = GameSave.peek(this.id);

		if (!this.info.free && !this.info.error) {
			String typeName = this.info.type.toString();

			this.info.first = typeName.substring(0, 1).toUpperCase() + typeName.substring(1).toLowerCase();
			Graphics.layout.setText(Graphics.medium, this.info.first);
			this.info.firstW = Graphics.layout.width;

			Graphics.layout.setText(Graphics.small, this.info.second);
			this.info.secondW = Graphics.layout.width;
		}
	}

	@Override
	public void onClick() {
		super.onClick();

		SaveManager.slot = this.id;
		Player.toSet = Player.Type.WARRIOR; // todo: select char
		Dungeon.goToLevel(this.info.free ? 0 : this.info.depth);
	}

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		if (this.info.error) {
			Graphics.shape.setColor(color.r, color.g * 0.7f, color.b * 0.7f, 1);
		} else if (this.info.free) {
			Graphics.shape.setColor(color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, 1);
		} else {
			Graphics.shape.setColor(color.r, color.g, color.b, 1);
		}

		float w = this.w * scale;
		float h = this.h * scale;

		Graphics.shape.rect(this.x - w / 2, this.y - h / 2, w, h);
		Graphics.shape.setColor(1, 1, 1, 1);

		if (this.info.free && !this.info.error) {
			Graphics.shape.rect(this.x - w / 4, this.y - w / 16, w / 2, w / 8);
			Graphics.shape.rect(this.x - w / 16, this.y - w / 4, w / 8, w / 2);
		}

		Graphics.endShape();

		if (!this.info.free && !this.info.error) {
			Graphics.print(this.info.first, Graphics.medium, this.x - this.info.firstW / 2, this.y + h / 2- 32);
			Graphics.print(this.info.second, Graphics.small, this.x - this.info.secondW / 2, this.y + h / 2 - 32 - 12);
		}
	}
}