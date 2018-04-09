package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Clown;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.consumable.potion.Potion;
import org.rexellentgames.dungeon.entity.item.consumable.spell.Spell;
import org.rexellentgames.dungeon.entity.level.BetterLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class HallLevel extends BetterLevel {
	public HallLevel() {
		if (!Network.SERVER) {
			Terrain.loadTextures(0);
		}

		this.addLight = Dungeon.depth == 0;
	}

	@Override
	protected ArrayList<Creature> generateCreatures() {
		ArrayList<Creature> creatures = super.generateCreatures();
		ArrayList<Class<? extends Mob>> spawns = new ArrayList<>();

		int count = Random.newInt(5, 7);

		if (Dungeon.depth != 0) {
			spawns.add(Knight.class);
		}

		if (Dungeon.depth > 1) {
			spawns.add(Clown.class);
		}

		if (spawns.size() == 0) {
			return creatures;
		}

		for (int i = 0; i < count; i++) {
			try {
				creatures.add(spawns.get(Random.newInt(spawns.size())).newInstance().generate());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return creatures;
	}

	@Override
	protected ArrayList<Item> generateItems() {
		ArrayList<Item> items = super.generateItems();

		if (Dungeon.depth != 0) {
			for (int i = 0; i < 2; i++) {
				items.add(Potion.random());
			}

			for (int i = 0; i < 1; i++) {
				items.add(Spell.random());
			}

			for (int i = 0; i < 3; i++) {
				items.add(new Gold().randomize());
			}
		}

		return items;
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.45f);
	}

	@Override
	protected Builder getBuilder() {
		if (Dungeon.depth == 0) {
			return new LineBuilder().setAngle(90).setPathLength(Dungeon.depth == 0 ? 0 : 0.2f, new float[]{0,1,0});
		} else {
			return new CastleBuilder();
		}
	}

	@Override
	protected int getNumRegularRooms() {
		return Dungeon.depth == 0 ? 0 : Random.newInt(Dungeon.depth + 3, Dungeon.depth * 2 + 2);
	}

	@Override
	protected int getNumConnectionRooms() {
		return 1;
	}
}