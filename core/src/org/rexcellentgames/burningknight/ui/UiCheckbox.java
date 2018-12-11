package org.rexcellentgames.burningknight.ui;

public class UiCheckbox extends UiChoice {
	public UiCheckbox(String label, int x, int y) {
		super(label, x, y);

		this.setChoices(new String[] {
			"on", "off"
		});
	}

	public UiCheckbox setOn(boolean on) {
		this.setCurrent(on ? 0 : 1);
		return this;
	}

	public boolean isOn() {
		return getCurrent() == 0;
	}
}