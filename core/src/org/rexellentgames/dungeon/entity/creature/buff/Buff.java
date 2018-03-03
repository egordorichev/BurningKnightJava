package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.Log;

public class Buff {
	protected String name;
	protected String description;
	protected int sprite;
	protected float duration = 10f;
	protected float time;
	protected boolean bad = false;
	protected Creature owner;
	protected boolean ended;

	public Buff(float duration) {
		this.duration = duration;
	}

	public Buff() {
		this.duration = this.time;
	}

	public void onStart() {

	}

	protected void onEnd() {

	}

	protected void onUpdate(float dt) {

	}

	public void render(Creature creature) {

	}

	public void update(float dt) {
		this.time += dt;
		this.onUpdate(dt);

		if (this.time >= this.duration) {
			this.ended = true;
			this.onEnd();
			this.owner.removeBuff(this);
		}
	}

	public void setOwner(Creature owner) {
		this.owner = owner;
	}

	public int getSprite() {
		return this.sprite;
	}

	public String getName() {
		return this.name;
	}

	public float getDuration() {
		return this.duration;
	}

	public float getTime() {
		return this.time;
	}
}