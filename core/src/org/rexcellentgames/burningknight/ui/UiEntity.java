package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.entity.Entity;

public class UiEntity extends Entity {
	protected boolean isSelected;
	protected boolean isSelectable = true;

	{
		alwaysRender = true;
		alwaysActive = true;
	}

	public void select() {
		isSelected = true;
	}

	public void unselect() {
		isSelected = false;
	}

	public boolean isSelectable() {
		return isSelectable;
	}
}