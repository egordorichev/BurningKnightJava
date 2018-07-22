package org.rexcellentgames.burningknight.entity;

import org.rexcellentgames.burningknight.assets.Graphics;

public class CircleObstacle extends Obstacle {
	public float r;

	@Override
	public void render() {
		Graphics.shape.circle(this.x, this.y, this.r);
	}

	public static CircleObstacle make(float r) {
		CircleObstacle obstacle = new CircleObstacle();
		obstacle.r = r;

		all.add(obstacle);

		return obstacle;
	}
}