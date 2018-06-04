package org.rexellentgames.dungeon.entity.item.pet.impl;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.util.Tween;

public class JellyOrbitalImpl extends Orbital {
	private Tween.Task xlast;
	private Tween.Task ylast;

	@Override
	protected void onHit(Entity entity) {
		if (entity instanceof BulletEntity) {
			((BulletEntity) entity).vel.x *= -1;
			((BulletEntity) entity).vel.y *= -1;
		} else if (entity instanceof Fireball) {
			((Fireball) entity).vel.x *= -1;
			((Fireball) entity).vel.y *= -1;
		}

		if (this.xlast != null) {
			Tween.remove(this.xlast);
			Tween.remove(this.ylast);

			this.xlast = null;
			this.ylast = null;
		}

		this.xlast = Tween.to(new Tween.Task(1.3f, 0.2f) {
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

		this.ylast = Tween.to(new Tween.Task(0.7f, 0.2f) {
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