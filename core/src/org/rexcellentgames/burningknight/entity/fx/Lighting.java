package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Lighting extends Laser {
	private ArrayList<Point> shift = new ArrayList<>();
	private int num;
	private double an;
	private Point target;

	@Override
	public void init() {
		super.init();

		this.an = Math.toRadians(this.a + 90);
		this.num = (int) (this.w / 16f);

		shade = new Color(0, 1, 1, 1);

		target = new Point(x + (float) Math.cos(this.an) * this.w, y + (float) Math.sin(this.an) * this.w);
	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.last += dt;

		if (this.last >= 0.05f) {
			this.last = 0;
			// this.shift.clear();
		}
	}

	@Override
	public void render() {
		int v = (int) (Math.ceil(this.w / 16) - 1);

		Point last = new Point(this.x, this.y);

		for (int i = 0; i < v + 1; i++) {
			if (this.shift.size() <= i) {
				this.shift.add(new Point(Random.newFloat(-5, 5), Random.newFloat(-5, 5)));
			}

			Point p = this.shift.get(i);
			Point point;

			if (v == i) {
				point = target;
			} else {
				point = new Point(last.x + p.x + (float) Math.cos(this.an) * 16, last.y + p.y + (float) Math.sin(this.an) * 16);
			}

			// renderFrom(last, point);

			last = point;
		}
	}
}