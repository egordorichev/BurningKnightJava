package org.rexcellentgames.burningknight.entity.item.weapon.axe;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.AxeProjectile;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Tween;

public class Axe extends Weapon {
	{
		stackable = true;
		penetrates = true;
		ox = 0;
		oy = 0;
		auto = true;

		String letter = "a";

		name = Locale.get("axe_" + letter);
		description = Locale.get("axe_desc");
		damage = 3;
		penetrates = true;
		sprite = "item-axe_" + letter;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_AXE");
	}

	private float added;
	private float ox;
	private float oy;
	protected int speed = 520;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped, boolean back) {
		float angle = -this.added;

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

		this.renderAt(xx, yy,
			back ? (flipped ? -45 : 45) : angle, sprite.getRegionWidth() / 2 + (flipped ? this.ox : -this.ox), this.oy,
			false, false, flipped ? -1f : 1f, 1f);
	}

	protected boolean canBeConsumed() {
		return false;
	}

	@Override
	public void use() {
		super.use();

		final Axe self = this;

		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI);
		float s = 60f;

		float knockbackMod = owner.knockbackMod;

		this.owner.knockback.x += Math.cos(a) * s * knockbackMod;
		this.owner.knockback.y += Math.sin(a) * s * knockbackMod;

		Tween.to(new Tween.Task(130, 0.3f) {
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
				if (canBeConsumed()) { count -= 1; }
				added = 0;
				owner.playSfx("throw");

				AxeProjectile fx = new AxeProjectile();

				fx.region = getSprite();
				fx.type = self.getClass();
				fx.x = owner.x + (owner.w - 16) / 2;
				fx.y = owner.y + (owner.h) / 2;
				fx.speed = (int) (speed);

				fx.owner = owner;
				fx.damage = rollDamage();
				fx.penetrates = penetrates;
				fx.axe = self;

				Dungeon.area.add(fx);
				endUse();
			}
		});
	}

	@Override
	protected void createHitbox() {

	}
}