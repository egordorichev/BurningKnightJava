package org.rexcellentgames.burningknight.entity.trap;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.util.AnimationData;
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
		four.render(this.x, this.y, false, false, 8, 0, 0, sx, sy);
	}

	@Override
	protected void send() {
		for (int i = 0; i < 4; i++) {
			BulletProjectile bullet = new BulletProjectile();
			bullet.sprite = Graphics.getTexture("bullet-bad");
			bullet.anim = getAnimation().get("projectile");

			float x = this.x + 8;
			float y = this.y + 8;

			bullet.x = x;
			bullet.y = y;
			bullet.damage = 2;
			bullet.w = 4;
			bullet.h = 4;
			bullet.letter = "bad";
			bullet.bad = true;

			this.modify(bullet);

			float s = 1.5f * 30f;

			float a = (float) (this.a + i * Math.PI / 2);

			bullet.vel = new Point(
				(float) Math.cos(a) * s, (float) Math.sin(a) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		}
	}
}