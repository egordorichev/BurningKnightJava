package org.rexellentgames.dungeon.entity.item.pet;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.autouse.Autouse;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.level.save.PlayerSave;
import org.rexellentgames.dungeon.util.Random;

public class Pet extends Autouse {
	public PetEntity create() {
		return new PetEntity();
	}

	@Override
	public void use() {
		super.use();

		setCount(count - 1);

		PetEntity entity = create();

		double a = Random.newFloat() * Math.PI * 2;
		float d = 24f;

		entity.x = this.owner.x + this.owner.w / 2 + (float) (Math.cos(a) * d);
		entity.y = this.owner.y + this.owner.h / 2 + (float) (Math.sin(a) * d);

		Dungeon.area.add(entity);
		PlayerSave.add(entity);
	}
}