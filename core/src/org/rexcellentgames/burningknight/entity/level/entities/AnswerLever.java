package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.special.LeverAnswerRoom;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class AnswerLever extends Lever {
	public static ArrayList<AnswerLever> all = new ArrayList<>();

	@Override
	public void init() {
		super.init();
		all.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
	}

	@Override
	protected void onToggle() {
		super.onToggle();

		if (this.did) {
			return;
		}

		for (AnswerLever lever : all) {
			if (lever.shouldBeOn && !lever.isOn()) {
				return;
			} else if (!lever.shouldBeOn && lever.isOn()) {
				return;
			}
		}

		for (AnswerLever lever : all) {
			lever.did = true;
			lever.remove();
			lever.done = true;

			for (int i = 0; i < 4; i++) {
				PoofFx fx = new PoofFx();

				fx.x = lever.x + lever.w / 2;
				fx.y = lever.y + lever.h / 2;

				Dungeon.area.add(fx);
			}
		}

		Log.info("Right!");

		for (Room room : Dungeon.level.getRooms()) {
			if (room instanceof LeverAnswerRoom) {
				if (Random.chance(Mimic.chance)) {
					Mimic chest = new Mimic();
					Point center = room.getCenter();

					chest.x = center.x * 16 + 1;
					chest.y = center.y * 16;
					chest.weapon = Random.chance(50);

					for (int i = 0; i < 10; i++) {
						PoofFx fx = new PoofFx();

						fx.x = chest.x + chest.w / 2;
						fx.y = chest.y + chest.h / 2;

						Dungeon.area.add(fx);
					}

					Dungeon.area.add(chest.add());
				} else {
					Chest chest = Chest.random();
					Point center = room.getCenter();

					chest.x = center.x * 16 + 1;
					chest.y = center.y * 16;
					chest.weapon = Random.chance(50);
					chest.setItem(chest.generate());
					chest.locked = false;

					for (int i = 0; i < 10; i++) {
						PoofFx fx = new PoofFx();

						fx.x = chest.x + chest.w / 2;
						fx.y = chest.y + chest.h / 2;

						Dungeon.area.add(fx);
					}

					Dungeon.area.add(chest.add());
				}

				return;
			}
		}
	}
}