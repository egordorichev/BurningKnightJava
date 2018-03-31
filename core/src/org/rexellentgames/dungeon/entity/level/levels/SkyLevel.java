package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.consumable.potion.Potion;
import org.rexellentgames.dungeon.entity.item.consumable.spell.Spell;
import org.rexellentgames.dungeon.entity.level.BetterLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.SkyEntrance;
import org.rexellentgames.dungeon.net.Network;

import java.util.ArrayList;

public class SkyLevel extends BetterLevel {
	public SkyLevel() {
		if (!Network.SERVER) {
			Terrain.loadTextures(0);
		}
	}

	@Override
	protected ArrayList<Creature> generateCreatures() {
		ArrayList<Creature> creatures = super.generateCreatures();

		for (int i = 0; i < 10; i++) {
			creatures.add(new Knight().generate());
		}

		return creatures;
	}

	@Override
	protected ArrayList<Item> generateItems() {
		ArrayList<Item> items = super.generateItems();

		for (int i = 0; i < 4; i++) {
			items.add(Potion.random());
		}

		for (int i = 0; i < 2; i++) {
			items.add(Spell.random());
		}

		for (int i = 0; i < 4; i++) {
			items.add(new Gold().randomize());
		}

		return items;
	}

	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		for (int i = 0; i < 10; i++) {
			rooms.add(RegularRoom.create());
		}

		this.entrance = new SkyEntrance();

		rooms.add(this.entrance);

		return rooms;
	}

	@Override
	protected Builder getBuilder() {
		/*return new LoopBuilder().setShape(2,
			Random.newFloat(0.4f, 0.7f),
			Random.newFloat(0f, 0.5f))*/
		return new LineBuilder().setAngle(90).setPathLength(0.3f, new float[]{0, 1, 0});
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.45f);
	}
}