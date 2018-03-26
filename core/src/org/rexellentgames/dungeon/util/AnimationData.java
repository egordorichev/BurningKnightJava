package org.rexellentgames.dungeon.util;

import org.rexellentgames.dungeon.assets.Graphics;

import java.util.ArrayList;

public class AnimationData {
	private ArrayList<Animation.Frame> frames = new ArrayList<>();
	private float t;
	private int index;
	private Animation.Frame current;

	public AnimationData(ArrayList<Animation.Frame> frames) {
		this.frames.addAll(frames);
		this.current = this.frames.get(0);
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
}