package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.input.Input;

public class UiKey extends UiButton {
	private String keyId;
	private String secondLabel;
	private int secondW;

	public UiKey(String label, int x, int y) {
		super(label, x, y);
		this.keyId = label;

		setSecond();
		w += 32f;
		UiChoice.maxW = Math.max(w, UiChoice.maxW);
	}

	@Override
	public void onClick() {
		Player.instance.playSfx("menu/change_parameter");
		Input.listener = this;
		setSecond();
	}

	protected void setSecond() {
		if (Input.listener == this) {
			this.secondLabel = Locale.get("assign_key");
		} else {
			this.secondLabel = Input.instance.getBinding(this.keyId);

			if (this.secondLabel == null) {
				this.secondLabel = Locale.get("none");
			} else if (this.secondLabel.equals("Mouse0")) {
				this.secondLabel = "LMB";
			} else if (this.secondLabel.equals("Mouse1")) {
				this.secondLabel = "RMB";
			}
		}

		w -= secondW;

		Graphics.layout.setText(Graphics.medium, this.secondLabel);
		this.secondW = (int) Graphics.layout.width;

		w += secondW;
	}

	@Override
	public void setLabel(String label) {
		super.setLabel(label);

	}

	public void set(String id) {
		Input.listener = null;
		Player.instance.playSfx("menu/select");
		Input.instance.rebind(this.keyId, Input.instance.getBinding(this.keyId), id);
		setSecond();
	}

	@Override
	public void render() {
		float w = UiChoice.maxW;
		Graphics.medium.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);
		Graphics.medium.draw(Graphics.batch, this.label, this.x - w / 2 + 4, this.y - this.h / 2 + 16);
		Graphics.medium.draw(Graphics.batch, this.secondLabel, this.x + w / 2 - this.secondW + 2, this.y - this.h / 2 + 16);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}