package org.rexcellentgames.burningknight.entity.trap;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.fx.FireFx;
import org.rexcellentgames.burningknight.entity.fx.FireFxPhysic;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class FourSideTurret extends Turret {
	private AnimationData four;
	boolean str = true;

	{
		region = Item.missing;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.str = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.str);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (four == null) {
			four = getAnimation().get("turret_4_directions");
		}

		this.four.setFrame(str ? 0 : 1);
	}

	@Override
	public void render() {
		if (four == null) {
			four = getAnimation().get("turret_4_directions");
		}

		four.render(this.x, this.y, false, false, 8, 0, 0, sx, sy);
	}

	@Override
	protected void sendFlames() {
		for (int i = 0; i < 4; i++) {
			FireFx fx = (Random.chance(50) && this.t >= 1.5f) ? new FireFxPhysic() : new FireFx();


			float d = 5;
			float a = (float) (this.a + i * (Math.PI / 2));

			fx.x = x + Random.newFloat(-4, 4) + 8 + (float) (Math.cos(a) * d);
			fx.y = y + Random.newFloat(-4, 4) + 8 + (float) (Math.sin(a) * d);

			float f = this.t >= 1.5f ? 120f : 40f;

			fx.vel.x = (float) (Math.cos(a) * f);
			fx.vel.y = (float) (Math.sin(a) * f);

			Dungeon.area.add(fx);
		}
	}

	@Override
	protected void send() {
		this.tween();
		this.playSfx(this.type == 3 ? "fire" : "gun_machinegun");

		if (this.type == 3) {
			this.on = true;
			this.t = 0;
			return;
		}

		for (int i = 0; i < 4; i++) {
			BulletProjectile bullet = new BulletProjectile();
			bullet.sprite = Graphics.getTexture("bullet-bad");
			// bullet.anim = getAnimation().get("projectile");

			float x = this.x + 8;
			float y = this.y + 8;

			bullet.x = x;
			bullet.y = y;
			bullet.damage = 2;
			bullet.w = 12;
			bullet.h = 12;
			bullet.letter = "bad";
			bullet.bad = true;

			this.modify(bullet);

			float s = 1.5f * 30f;

			float a = (float) (this.a + i * Math.PI / 2);

			bullet.velocity = new Point(
				(float) Math.cos(a) * s, (float) Math.sin(a) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		}
	}
}