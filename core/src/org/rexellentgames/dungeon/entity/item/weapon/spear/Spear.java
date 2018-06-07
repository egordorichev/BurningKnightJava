package org.rexellentgames.dungeon.entity.item.weapon.spear;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.sword.Sword;
import org.rexellentgames.dungeon.game.input.Input;

public class Spear extends Sword {
	{
		knockback = 30f;
		useTime = 0.3f;
		timeA = 0.15f;
		timeB = 0.15f;
		auto = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setPaused(true);
		}

		float angle = 0;

		if (this.owner != null) {
			if (this.owner instanceof Player) {
				float dx = this.owner.x + this.owner.w / 2 - Input.instance.worldMouse.x - 8;
				float dy = this.owner.y + this.owner.h / 2 - Input.instance.worldMouse.y - 8;
				angle = (float) Math.toDegrees(Math.atan2(dy, dx)) + 90;
			} else if (this.owner instanceof Mob && this.added == 0) {
				Mob mob = (Mob) this.owner;

				if (mob.target != null && mob.saw && !mob.isDead()) {
					float dx = this.owner.x + this.owner.w / 2 - mob.target .x - mob.target.w / 2;
					float dy = this.owner.y + this.owner.h / 2 - mob.target .y - mob.target.h / 2;
					angle = (float) Math.toDegrees(Math.atan2(dy, dx));
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

		float a = (float) Math.toRadians(angle);
		float an = this.added / 8f;

		float xx = (float) (x + w / 2 + (flipped ? -w / 4 : w / 4) + Math.cos(a + Math.PI / 2) * (an - this.region.getRegionHeight() / 2));
		float yy = (float) (y + (this.ox == 0 ? h / 4 : h / 2) + Math.sin(a + Math.PI / 2) * (an - this.region.getRegionHeight() / 2));

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() : 0), yy,
			angle, sprite.getRegionWidth() / 2 + (flipped ? this.ox : -this.ox), this.oy, flipped, false);

		if (this.blockbox != null) {
			this.blockbox.setTransform(xx + (float) Math.cos(a) * (flipped ? 0 : ox * 2), yy + (float) Math.sin(a) * (flipped ? 0 : ox * 2), a);
		} else if (this.body != null) {
			this.body.setTransform(xx, yy, a);
		}
	}
}