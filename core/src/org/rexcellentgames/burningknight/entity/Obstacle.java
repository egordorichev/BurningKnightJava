package org.rexcellentgames.burningknight.entity;

import java.util.ArrayList;

public class Obstacle {
	public static final boolean RENDER_DEBUG = true;
	public static ArrayList<Obstacle> all = new ArrayList<>();

	public float x;
	public float y;

	public void render() {

	}

	public static Obstacle make() {
		Obstacle obstacle = new Obstacle();

		all.add(obstacle);

		return obstacle;
	}

	public static Obstacle remove(Obstacle obstacle) {
		if (obstacle == null) {
			return null;
		}

		all.remove(obstacle);

		return null;
	}
}