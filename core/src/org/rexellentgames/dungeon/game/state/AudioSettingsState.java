package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiCheckbox;
import org.rexellentgames.dungeon.ui.UiSlider;

public class AudioSettingsState extends State {
	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiSlider("menu_label (music)", Display.GAME_WIDTH / 2, 128 + 24) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
				Graphics.playSfx("menu/select");
			}

			@Override
			public void onUpdate() {
				Settings.music = this.val;
			}
		}.setValue(Settings.music));

		Dungeon.area.add(new UiSlider("menu_label (sfx)", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
				Graphics.playSfx("menu/select");
			}

			@Override
			public void onUpdate() {
				Settings.sfx = this.val;
			}
		}.setValue(Settings.sfx));

		Dungeon.area.add(new UiCheckbox("menu_label (uisfx)", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
				Settings.uisfx = !Settings.uisfx;
				super.onClick();
			}
		}.setOn(Settings.uisfx));

		Dungeon.area.add(new UiButton("menu_label (back)", Display.GAME_WIDTH / 2, 128 - 24 * 2) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");
				Dungeon.game.setState(new SettingsState());
				Camera.instance.shake(3);
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}