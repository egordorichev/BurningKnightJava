package org.rexcellentgames.burningknight.entity.creature.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.creature.Creature;

public class Buff {
	protected String name;
	protected String id;
	protected float duration;
	protected float time;
	public boolean infinite = false;
	protected Creature owner;
	protected boolean ended;
	private TextureRegion region;

	public Buff(float duration) {
		this.duration = duration;
	}

	public Buff() {
		this.duration = this.time;
	}

	public void onStart() {

	}

	public void onEnd() {

	}

	public Buff setDuration(float duration) {
		this.duration = duration;
		return this;
	}

	protected void onUpdate(float dt) {

	}

	public void render(Creature creature) {

	}

	public void update(float dt) {
		this.time += dt;
		this.onUpdate(dt);

		if (this.time >= this.duration) {
			if (this.infinite) {
				this.time = 0;
			} else {
				this.ended = true;
				this.onEnd();
				this.owner.removeBuff(this.getId());
			}
		}
	}

	public void setOwner(Creature owner) {
		this.owner = owner;
	}

	public String getName() {
		return this.name;
	}

	public String getId() {
		return this.id;
	}

	public float getDuration() {
		return this.duration;
	}

	public float getTime() {
		return this.time;
	}
}