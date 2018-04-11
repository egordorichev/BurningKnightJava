package org.rexellentgames.dungeon.entity.item.weapon.magic;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.fx.Note;
import org.rexellentgames.dungeon.game.input.Input;

public class NoteBook extends MagicWeapon {
	{
		name = Locale.get("note_book");
		description = Locale.get("note_book_desc");
		damage = 6;
		mana = 10;
		useTime = 0.2f;
	}

	@Override
	public void use() {
		super.use();

		if (this.delay != 0) {
			Note note = new Note();
			float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
			float dy = Input.instance.worldMouse.y - this.owner.h / 2 - this.owner.y;
			float a = (float) Math.atan2(dy, dx);

			note.bad = false;

			note.x = (float) (this.owner.x + (this.owner.w - 10) / 2 + Math.cos(a) * 8);
			note.y = (float) (this.owner.y + (this.owner.h - 10) / 2 + Math.sin(a) * 8);
			note.a = a;

			Dungeon.area.add(note);
		}
	}
}