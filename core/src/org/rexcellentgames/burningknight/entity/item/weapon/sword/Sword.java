package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;
import java.util.ArrayList;

public class Sword extends SlashSword {
	protected float oy;
	protected float ox;

	protected int maxAngle;

	{
		moveXA = 0;
		moveXB = -8;
		moveYA = 0;
		moveYB = 0;
		timeA = 0f;
		delayA = 0.0f;
		timeB = 0.15f;
		delayB = 0.05f;
		timeC = 0.1f;
		backAngle = 0;
		maxAngle = 200;

		useTime = timeA + delayA + timeB + delayB + timeC;
	}

	public Sword() {
		setStats();
	}

	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		name = Locale.get("sword_" + letter);
		description = Locale.get("sword_desc");
		sprite = "item-sword_" + letter;
		damage = 10;
		useTime = 0.4f;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 7;
	}

	@Override
	public void upgrade() {
		super.upgrade();
		setStats();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		setStats();
	}

	{
		name = "Sword";
		sprite = "item-sword_b";
	}

	private float lastFrame;
	private ArrayList<Frame> frames = new ArrayList<>();

	private static class Frame {
		float added;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.lastFrame += dt;

		if (this.lastFrame >= 0.005f) {
			this.lastFrame = 0;

			if (this.added > 0) {
				Frame frame = new Frame();
				frame.added = (float) Math.toRadians(this.added);

				this.frames.add(frame);

				if (this.frames.size() > 10) {
					this.frames.remove(0);
				}
			} else if (this.frames.size() > 0) {
				this.frames.remove(0);

				if (this.frames.size() > 0) {
					this.frames.remove(0);
				}
			}
		}
	}

	protected float tr = 1f;
	protected float tg = 1f;
	protected float tb = 1f;
	protected float lastAngle;

	protected  boolean tail;

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		// Camera.shake(4);

		float a = this.owner.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2);
		this.owner.knockback.x += -Math.cos(a) * 120f;
		this.owner.knockback.y += -Math.sin(a) * 120f;
	}
}