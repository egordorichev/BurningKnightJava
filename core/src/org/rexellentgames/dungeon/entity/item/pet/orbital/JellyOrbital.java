package org.rexellentgames.dungeon.entity.item.pet.orbital;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.pet.Pet;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.util.Tween;

public class JellyOrbital extends Pet {
	{
		name = Locale.get("jelly");
		description = Locale.get("jelly_desc");
	}

	@Override
	public PetEntity create() {
		return new Orbital() {
			private Tween.Task xlast;
			private Tween.Task ylast;

			@Override
			protected void onHit(Entity entity) {
				super.onHit(entity);

				if (this.xlast != null) {
					Tween.remove(this.xlast);
					Tween.remove(this.ylast);

					this.xlast = null;
					this.ylast = null;
				}

				this.xlast = Tween.to(new Tween.Task(1.3f, 0.3f) {
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
						xlast = Tween.to(new Tween.Task(1f, 0.7f, Tween.Type.ELASTIC_OUT) {
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

				this.ylast = Tween.to(new Tween.Task(0.7f, 0.3f) {
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
						ylast = Tween.to(new Tween.Task(1f, 0.7f, Tween.Type.ELASTIC_OUT) {
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
		};
	}
}