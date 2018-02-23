package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;

import java.util.ArrayList;

public class Animation {
	private ArrayList<TextureRegion> frames = new ArrayList<TextureRegion>();
	private float delay;

	public Animation(Texture texture, float delay, int ... frames) {
		for (int frame : frames) {
			int xx = frame % 32 * 16;
			int yy = (int) (Math.floor(frame / 32) * 16);

			this.frames.add(new TextureRegion(texture, xx, yy, 16, 16));
		}

		this.delay = delay;
	}

	public void render(float x, float y, float t, boolean flip) {
		int id = (int) (Math.floor(t / this.delay) % this.frames.size());
		TextureRegion frame = this.frames.get(id);

		Graphics.batch.draw(frame, flip ? x + 16 : x, y, 0, 0, 16, 16, flip ? -1 : 1, 1, 0);
	}
}