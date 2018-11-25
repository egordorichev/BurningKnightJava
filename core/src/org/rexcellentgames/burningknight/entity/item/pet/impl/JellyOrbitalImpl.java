package org.rexcellentgames.burningknight.entity.item.pet.impl;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.util.Tween;

public class JellyOrbitalImpl extends Orbital {
	private Tween.Task xlast;
	private Tween.Task ylast;

	{
		sprite = "item-jelly";
	}

	@Override
	protected void onHit(Entity entity) {
		if (entity instanceof Projectile && ((Projectile) entity).bad) {
			((Projectile) entity).velocity.x *= -1;
			((Projectile) entity).velocity.y *= -1;
			((Projectile) entity).bad = false;
		}

		if (this.xlast != null) {
			Tween.remove(this.xlast);
			Tween.remove(this.ylast);

			this.xlast = null;
			this.ylast = null;
		}

		this.xlast = Tween.to(new Tween.Task(1.3f, 0.1f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}

			@Override
			public void onEnd() {
				xlast = Tween.to(new Tween.Task(1f, 1f, Tween.Type.ELASTIC_OUT) {
					@Override
					public float getValue() {
						return sx;
					}

					@Override
					public void setValue(float value) {
						sx = value;
					}

					@Override
					public void onEnd() {
						xlast = null;
					}
				});
			}
		});

		this.ylast = Tween.to(new Tween.Task(0.7f, 0.1f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}

			@Override
			public void onEnd() {
				ylast = Tween.to(new Tween.Task(1f, 1f, Tween.Type.ELASTIC_OUT) {
					@Override
					public float getValue() {
						return sy;
					}

					@Override
					public void setValue(float value) {
						sy = value;
					}

					@Override
					public void onEnd() {
						ylast = null;
					}
				});
			}
		});
	}
}