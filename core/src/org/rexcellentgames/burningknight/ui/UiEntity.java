package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.game.state.State;

public class UiEntity extends Entity {
	protected boolean isSelected;
	protected boolean isSelectable = true;
	protected boolean wasSelected;

	{
		alwaysRender = true;
		alwaysActive = true;
	}


	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
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

		return this.x + this.w * 2 >= camera.position.x - Display.GAME_WIDTH / 2 * zoom + State.settingsX &&
			this.y + this.h * 2 >= camera.position.y - Display.GAME_HEIGHT / 2 * zoom &&
			this.x <= camera.position.x + Display.GAME_WIDTH / 2 * zoom + State.settingsX &&
			this.y <= camera.position.y + this.h + Display.GAME_HEIGHT / 2 * zoom;

	}
}