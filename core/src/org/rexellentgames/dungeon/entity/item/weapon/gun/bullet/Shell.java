package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class Shell extends Entity {
	private static Animation animations = Animation.make("fx-shell");
	private TextureRegion sprite;
	public Point vel;
	private float z = 10f;

	@Override
	public void init() {
		super.init();

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		this.sprite = frames.get(Random.newInt(frames.size())).frame;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.x *= (this.z == 0 ? 0.5f : 0.98f);

		this.x += this.vel.x;
		this.z = Math.max(0, this.z + this.vel.y);
		this.vel.y -= 0.1f;
	}

	@Override
	public void render() {
		Graphics.render(this.sprite, this.x, this.y + this.z);
	}
}