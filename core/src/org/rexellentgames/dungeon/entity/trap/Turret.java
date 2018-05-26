package org.rexellentgames.dungeon.entity.trap;

import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.entity.level.entities.SolidProp;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class Turret extends SolidProp {
	{
		sprite = "item (missing)";
		alwaysActive = true;
		collider = new Rectangle(1, 10, 14, 4);
	}

	public float a;
	public float last;
	private boolean s;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.last = reader.readFloat();
		this.a = reader.readFloat();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeFloat(this.last);
		writer.writeFloat(this.a);
	}

	@Override
	public void update(float dt) {
		super.update(dt);


		if (!s) {
			s = true;

			for (int x = 0; x < this.w / 16; x++) {
				for (int y = 0; y < this.h / 16; y++) {
					Dungeon.level.setPassable((int) (x + this.x / 16), (int) (y + (this.y + 8) / 16), false);
				}
			}
		}

		this.last += dt;

		if (this.last >= 3f) {
			this.last = 0;

			BulletEntity bullet = new BulletEntity();
			bullet.sprite = Graphics.getTexture("bullet (bullet bad)");

			float x = this.x + region.getRegionWidth() / 2;
			float y = this.y + region.getRegionHeight() / 2;

			bullet.x = x;
			bullet.y = y;
			bullet.damage = 2;
			bullet.letter = "bullet bad";

			float s = 3f;

			bullet.vel = new Point(
				(float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		}
	}
}