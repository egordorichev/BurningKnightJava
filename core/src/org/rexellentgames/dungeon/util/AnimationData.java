package org.rexellentgames.dungeon.util;

import org.rexellentgames.dungeon.assets.Graphics;

import java.util.ArrayList;

public class AnimationData {
	private final ArrayList<Animation.Frame> frames;
	private float t;
	private int index;
	private Animation.Frame current;

	public AnimationData(ArrayList<Animation.Frame> frames) {
		this.frames = frames;
		this.current = this.frames.get(0);
		this.t = Random.newFloat(0f, 100f);
	}

	public Animation.Frame getCurrent() {
		return this.current;
	}

	public void update(float dt) {
		this.t += dt;

		if (this.t >= this.current.delay) {
			this.index += 1;
			this.t = 0;

			if (this.index >= this.frames.size()) {
				this.index = 0;
			}

			this.current = this.frames.get(this.index);
		}
	}

	public void render(float x, float y, boolean flip) {
		Graphics.render(this.current.frame, x, y, 0, 0, 0, flip, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void render(float x, float y, boolean flip, int f) {
		Graphics.render(this.frames.get(f).frame, x, y, 0, 0, 0, flip, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}