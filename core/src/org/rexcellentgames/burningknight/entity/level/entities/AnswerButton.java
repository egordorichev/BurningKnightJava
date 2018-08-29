package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.special.ButtonPuzzleRoom;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class AnswerButton extends Button {
	public boolean shouldBeDown;
	public static ArrayList<AnswerButton> all = new ArrayList<>();

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
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		shouldBeDown = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(shouldBeDown);
	}

	@Override
	public void onPress() {
		super.onPress();

		if (this.shouldBeDown) {
			for (AnswerButton button : all) {
				if (button.shouldBeDown && !button.isDown()) {
					return;
				} else if (!button.shouldBeDown && button.isDown()) {
					return;
				}
			}

			Log.info("Right!");

			for (Room room : Dungeon.level.getRooms()) {
				if (room instanceof ButtonPuzzleRoom) {
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
}