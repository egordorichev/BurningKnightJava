package org.rexellentgames.dungeon.entity;

import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Entity extends Point {
	protected Area area;
	protected int depth = 0;

	public void init() {

	}

	public void destroy() {

	}

	public void update(float dt) {

	}

	public void render() {

	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getDepth() {
		return this.depth;
	}
}