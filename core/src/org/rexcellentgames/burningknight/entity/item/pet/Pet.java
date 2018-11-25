package org.rexcellentgames.burningknight.entity.item.pet;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.util.Random;

public class Pet extends Autouse {
	public PetEntity create() {
		return new PetEntity();
	}

	@Override
	public void generate() {
		setCount(Random.newInt(1, 4));
	}

	@Override
	public void use() {
		super.use();

		PetEntity entity = create();

		double a = Random.newFloat() * Math.PI * 2;
		float d = 24f;

		entity.x = this.owner.x + this.owner.w / 2 + (float) (Math.cos(a) * d);
		entity.y = this.owner.y + this.owner.h / 2 + (float) (Math.sin(a) * d);

		Dungeon.area.add(entity);
		PlayerSave.add(entity);
	}
}