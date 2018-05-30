package org.rexellentgames.dungeon.entity.item.weapon.axe;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class Axe extends Weapon {
	{
		stackable = true;
		penetrates = true;
		ox = 0;
		oy = 0;
		auto = true;
	}

	private float added;
	private float ox;
	private float oy;
	protected int speed = 520;

	@Override
	public void generate() {
		super.generate();
		this.count = Random.newInt(3, 7);
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.added > 0) {
			float angle = this.added;

			if (this.owner != null) {
				if (this.owner instanceof Player) {
					float dx = this.owner.x + this.owner.w / 2 - Input.instance.worldMouse.x - 8;
					float dy = this.owner.y + this.owner.h / 2 - Input.instance.worldMouse.y - 8;
					float a = (float) Math.toDegrees(Math.atan2(dy, dx));

					angle += (flipped ? a : -a);

					angle = flipped ? angle : 180 - angle;
				} else if (this.owner instanceof Mob && this.added == 0) {
					Mob mob = (Mob) this.owner;

					if (mob.target != null && mob.saw && !mob.isDead()) {
						float dx = this.owner.x + this.owner.w / 2 - mob.target .x - mob.target.w / 2;
						float dy = this.owner.y + this.owner.h / 2 - mob.target .y - mob.target.h / 2;
						float a = (float) Math.toDegrees(Math.atan2(dy, dx));

						angle += (flipped ? a : -a);
					} else {
						angle += (flipped ? 0 : 180);
					}

					angle = flipped ? angle : 180 - angle;
				} else {
					angle += (flipped ? 0 : 180);
					angle = flipped ? angle : 180 - angle;
				}
			}

			TextureRegion sprite = this.getSprite();

			float xx = x + w / 2 + (flipped ? -w / 4 : w / 4);
			float yy = y + (this.ox == 0 ? h / 4 : h / 2);

			this.renderAt(xx - (flipped ? sprite.getRegionWidth() : 0), yy,
				angle, sprite.getRegionWidth() / 2 + (flipped ? this.ox : -this.ox), this.oy, flipped, false);
		}
	}

	@Override
	public void use() {
		super.use();

		Axe self = this;

		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI);
		float s = 60f;

		this.owner.vel.x += Math.cos(a) * s;
		this.owner.vel.y += Math.sin(a) * s;

		Tween.to(new Tween.Task(90, 0.2f) {
			@Override
			public float getValue() {
				return added;
			}

			@Override
			public void setValue(float value) {
				added = value;
			}

			@Override
			public void onEnd() {
				count -= 1;
				added = 0;

				AxeFx fx = new AxeFx();

				fx.region = getSprite();
				fx.type = self.getClass();
				fx.x = owner.x + (owner.w - 16) / 2;
				fx.y = owner.y + (owner.h - 16) / 2;
				fx.speed = speed;

				fx.owner = owner;
				fx.damage = rollDamage();
				fx.penetrates = penetrates;
				fx.axe = self;

				Dungeon.area.add(fx);
				endUse();
			}
		});
	}
}