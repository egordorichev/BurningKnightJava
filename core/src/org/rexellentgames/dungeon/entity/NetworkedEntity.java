package org.rexellentgames.dungeon.entity;

import org.rexellentgames.dungeon.util.geometry.Point;

public class NetworkedEntity extends Entity {
	public Point vel = new Point();
	private boolean sleeping;
	protected Point oldPosition = new Point();
	private int id;

	public void setId(int id) {
		this.id = id;
	}

	public String getParam() {
		return "";
	}

	public int getId() {
		return this.id;
	}

	public boolean isSleeping() {
		return this.sleeping;
	}

	@Override
	public void update(float dt) {
		this.sleeping = (this.x != this.oldPosition.x || this.y != this.oldPosition.y);

		super.update(dt);

		oldPosition.x = this.x;
		oldPosition.y = this.y;
	}
}