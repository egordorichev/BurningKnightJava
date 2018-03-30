package org.rexellentgames.dungeon.entity.creature.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.Creature;

public class Buff {
	protected String name;
	protected String description;
	protected String sprite;
	protected float duration = 10f;
	protected float time;
	protected boolean bad = false;
	protected boolean infinite = false;
	protected Creature owner;
	protected boolean ended;
	private TextureRegion region;

	public TextureRegion getSprite() {
		if (this.region == null) {
			this.region = Graphics.getTexture(this.sprite);
		}

		return this.region;
	}

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

	public void setDuration(float duration) {
		this.duration = duration;
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
				this.owner.removeBuff(this.getClass());
			}
		}
	}

	public void setOwner(Creature owner) {
		this.owner = owner;
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