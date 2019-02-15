package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class ThrowingDagger extends Weapon {
	private boolean forward;
	private float max = 100f;

	{
		useTime = 2f;
	}

	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		sprite = "item-dagger_" + letter;
		damage = 3;
		name = Locale.get("throwing_dagger_" + letter);
		description = Locale.get("throwing_dagger_desc");

		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 7;
	}

	public ThrowingDagger() {
		setStats();
	}

	@Override
	public void upgrade() {
		super.upgrade();
		setStats();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		setStats();
	}

	@Override
	public void use() {
		super.use();

		this.forward = true;
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity == this.owner) {
			return;
		}

		if (this.added > 2f && (entity == null || entity instanceof Creature)) {
			forward = false;
		}
	}

	private float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped, boolean back) {
		float dt = Gdx.graphics.getDeltaTime() * 2;
		float d = this.max - this.added;

		if (this.forward) {
			if (d <= 10f) {
				this.forward = false;
			} else {
				this.added += d * dt * 1.5;
			}
		} else if (this.added > 0) {
			this.added = Math.max(this.added - d * dt * 7, 0);
		} else {
			endUse();
		}

		Point aim = this.owner.getAim();
		float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
		an = Gun.angleLerp(this.lastAngle, an, 0.05f, this.owner != null && this.owner.freezed);
		this.lastAngle = an;
		float a = (float) Math.toDegrees(an);
		float dst = added;

		TextureRegion sprite = this.getSprite();

		float xx = (float) (x + w / 2 + (flipped ? -w / 4 : w / 4) + Math.cos(an + Math.PI / 2) * dst);
		float yy = (float) (y + h / 4 + Math.sin(an + Math.PI / 2) * dst);

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() : 0), yy,
			back ? (flipped ? -45 : 45) : a, sprite.getRegionWidth() / 2, 0, flipped, false);

		if (this.body != null) {
			World.checkLocked(this.body).setTransform(xx, yy, an);
		}
	}
}