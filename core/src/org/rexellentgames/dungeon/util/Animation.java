package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.net.Network;

import java.util.ArrayList;

public class Animation {
	private ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
	private float delay;
	private int size;

	public static Animation make(Texture texture, float delay, int size, int ... frames) {
		if (Network.SERVER) {
			return null;
		} else {
			return new Animation(texture, delay, size, frames);
		}
	}

	public Animation(Texture texture, float delay, int size, int ... frames) {
		for (int frame : frames) {
			int xx = frame % 32 * 16;
			int yy = (int) (Math.floor(frame / 32) * 16);

			this.frames.add(new TextureRegion(texture, xx, yy, size, size));
		}

		this.size = size;
		this.delay = delay;
	}

	public void render(float x, float y, float t, boolean flip) {
		int id = (int) (Math.floor(t / this.delay) % this.frames.size());
		TextureRegion frame = this.frames.get(id);

		Graphics.batch.draw(frame, flip ? x + this.size : x, y, 0, 0, this.size, this.size, flip ? -1 : 1, 1, 0);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}