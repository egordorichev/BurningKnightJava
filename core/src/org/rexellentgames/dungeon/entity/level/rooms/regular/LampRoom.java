package org.rexellentgames.dungeon.entity.level.rooms.regular;

import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LampRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();

		ItemHolder holder = new ItemHolder();

		Painter.set(level, center, Terrain.WOOD); // todo: something better

		holder.x = center.x * 16 + (16 - 8) / 2;
		holder.y = center.y * 16 + (16 - 14) / 2 - 4;

		holder.setItem(new Lamp());

		Dungeon.level.addSaveable(holder);
		Dungeon.area.add(holder);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
		}

		// TODO
		// Camera.instance.clamp.add(new Rectangle(this.left * 16, this.top * 16, this.getWidth() * 16, this.getHeight() * 16));
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 2;
		}

		return 1;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 2;
	}

	@Override
	public int getMaxHeight() {
		return 11;
	}

	@Override
	public int getMaxWidth() {
		return 11;
	}
}