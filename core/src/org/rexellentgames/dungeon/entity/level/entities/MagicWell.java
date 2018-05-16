package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.entities.fx.WellFx;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class MagicWell extends SolidProp {
	{
		sprite = "biome-0 (tub)";
		collider = new Rectangle(4, 5, 30 - 8, 12);
	}

	private boolean s;
	private boolean used;
	private WellFx fx;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		used = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.used);
	}

	public void use() {
		this.used = true;

		int r = Random.newInt(3);

		switch (r) {
			case 0: default:
				Player.instance.modifyHp(Player.instance.getHpMax() - Player.instance.getHp());
				Log.info("[green]You take a sip and feel refreshed!");
				break;
			/*case 1:

				break;
			case 2:
				break;*/
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;
			for (int x = (int) (this.x / 16); x < Math.ceil((this.x + 32) / 16); x++) {
				Dungeon.level.setPassable(x, (int) ((this.y + 11) / 16), false);
				Dungeon.level.setPassable(x, (int) ((this.y) / 16), false);
			}
		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && !this.used) {
			this.fx = new WellFx(this, "take_a_sip");
			Dungeon.area.add(fx);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (this.fx != null && entity instanceof Player) {
			this.fx.remove();
			this.fx = null;
		}
	}
}