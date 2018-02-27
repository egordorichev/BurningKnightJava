package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;

public class Weapon extends Item {
	protected Body body;
	protected int damage = 1;

	@Override
	public void use() {
		super.use();

		this.createHitbox();
	}

	@Override
	public void secondUse() {
		super.secondUse();

		this.createHitbox();
	}

	protected void createHitbox() {
		World world = Player.instance.getArea().getState().getWorld();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = world.createBody(def);
		PolygonShape poly = new PolygonShape();

		int w = 32;
		int h = 32;

		poly.set(new Vector2[]{
			new Vector2(0, 0), new Vector2(w, 0),
			new Vector2(0, h), new Vector2(w, h)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = true;

		body.createFixture(fixture);
		body.setUserData(this);
		poly.dispose();

		this.body.setTransform(Player.instance.x + (Player.instance.isFlipped() ? -8 : 8), Player.instance.y - 8, 0);
	}

	@Override
	public void endUse() {
		this.body.getWorld().destroyBody(this.body);
		this.body = null;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature && entity != Player.instance) {
			Creature creature = (Creature) entity;
			creature.modifyHp(-this.damage);
		}
	}
}