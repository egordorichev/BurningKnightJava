package org.rexellentgames.dungeon.entity.creature.buff;

public class InvisibilityBuff extends Buff {
	{
		name = "Invisible";
		description = "No one will notice you";
		sprite = "ui (invisible buff)";
		duration = 60f;
	}

	@Override
	public void onStart() {
		this.owner.invisible = true;
		this.owner.a = 0.5f;
	}

	@Override
	protected void onEnd() {
		this.owner.invisible = false;
		this.owner.a = 1;
	}
}