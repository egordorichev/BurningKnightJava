package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;

public class UiEntity extends Entity {
	protected boolean isSelected;
	protected boolean isSelectable = true;

	{
		alwaysRender = true;
		alwaysActive = true;
	}


	public boolean isSelected() {
		return isSelected;
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

	@Override
	public boolean isOnScreen() {
		OrthographicCamera camera = Camera.ui;

		float zoom = camera.zoom;

		return this.x + this.w * 2 >= camera.position.x - Display.GAME_WIDTH / 2 * zoom &&
			this.y + this.h * 2 >= camera.position.y - Display.GAME_HEIGHT / 2 * zoom &&
			this.x <= camera.position.x + Display.GAME_WIDTH / 2 * zoom &&
			this.y <= camera.position.y + this.h + Display.GAME_HEIGHT / 2 * zoom;

	}
}