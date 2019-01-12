package org.rexcellentgames.burningknight.entity.pattern;

import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.game.Area;

import java.util.ArrayList;

public class BulletPattern extends Entity {
	public ArrayList<BulletProjectile> bullets = new ArrayList<>();
	protected Vector2 velocity = new Vector2();

	{
		alwaysActive = true;
		alwaysRender = true;
	}

	public static void fire(BulletPattern pattern, float x, float y, float a, float speed) {
		pattern.x = x;
		pattern.y = y;

		pattern.velocity.x = (float) (Math.cos(a) * speed);
		pattern.velocity.y = (float) (Math.sin(a) * speed);

		Dungeon.area.add(pattern);
	}

	@Override
	public void init() {
		super.init();

		if (bullets.size() > 0) {
			updateBullets(0);
		}
	}

	@Override
	public void render() {
		super.render();
		bullets.sort(Area.comparator);
		renderBullets();
	}

	@Override
	public void renderShadow() {
		super.renderShadow();
		renderShadows();
	}

	protected float t;

	@Override
	public void update(float dt) {
		super.update(dt);

		dt *= Mob.shotSpeedMod;

		t += dt;

		x += velocity.x * dt;
		y += velocity.y * dt;

		updateBullets(dt);
		doLogic(dt);
	}

	public void addBullet(BulletProjectile bullet) {
		bullets.add(bullet);

		bullet.canBeRemoved = true;
		bullet.ignoreBodyPos = true;
		bullet.renderCircle = false;
		bullet.hasPattern = true;
		bullet.i = bullets.size() - 1;

		bullet.init();
	}

	protected void updateBullets(float dt) {
		for (int i = bullets.size() - 1; i >= 0; i--) {
			BulletProjectile b = bullets.get(i);

			doLogic(b, i);
			b.update(dt);

			if (b.done) {
				onBulletRemove(b);
				b.destroy();
				bullets.remove(b);
			}
		}

		if (bullets.size() == 0) {
			done = true;
		}
	}

	protected void onBulletRemove(BulletProjectile bullet) {

	}

	protected void renderBullets() {
		for (BulletProjectile b : bullets) {
			b.render();
		}
	}

	protected void renderShadows() {
		for (BulletProjectile b : bullets) {
			b.renderShadow();
		}
	}

	protected void doLogic(BulletProjectile bullet, int i) {

	}

	protected void doLogic(float dt) {

	}

	@Override
	public void destroy() {
		destroyBullets();
	}

	protected void destroyBullets() {
		for (BulletProjectile b : bullets) {
			b.done = true;
			b.destroy();
		}

		bullets.clear();
	}
}