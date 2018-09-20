package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class ArrowProjectile extends BulletProjectile {
	@Override
	public void update(float dt) {
		super.update(dt);

		this.next -= dt;

		if (this.next <= 0) {
			if (this.did) {
				if (this.positions.size() == 0) {
					this.done = true;
					return;
				}

				this.positions.remove(0);

				if (this.positions.size() == 0) {
					this.done = true;
				}
			} else {
				this.next = 0.01f;
				this.positions.add(new Point(this.x, this.y));

				if (this.positions.size() > 20) {
					this.positions.remove(0);
				}
			}
		}
	}

	private boolean did;
	private float next;

	@Override
	public void render() {
		Graphics.startAlphaShape();

		Point last = null;

		for (Point point : positions) {
			if (last != null) {
				Graphics.shape.setColor(1, 1, 1, 1);
				Graphics.shape.line(last.x, last.y, point.x, point.y);
				Graphics.shape.setColor(1, 1, 1, 0.3f);
				Graphics.shape.rectLine(last.x, last.y, point.x, point.y, 3);
			}

			last = point;
		}

		Graphics.endAlphaShape();
	}

	@Override
	protected boolean breaksFrom(Entity entity) {
		if (super.breaksFrom(entity)) {
			this.did = true;
		}

		return false;
	}

	private ArrayList<Point> positions = new ArrayList<Point>();
}