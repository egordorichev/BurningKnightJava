package org.rexcellentgames.burningknight.util;

import org.rexcellentgames.burningknight.assets.Graphics;

import java.util.ArrayList;

public class AnimationData {
	private final ArrayList<Animation.Frame> frames;
	private float t;
	private int index;
	private boolean pause;
	private boolean back;
	private Animation.Frame current;
	private boolean auto;
	private Listener listener;

	public void setSpeedModifier(float mod) {
		for (Animation.Frame frame : frames) {
			frame.delay = frame.initial * mod;
		}
	}

	public boolean isGoingBack() {
		return back;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public ArrayList<Animation.Frame> getFrames() {
		return this.frames;
	}

	public void setAutoPause(boolean auto) {
		this.auto = auto;
	}

	public AnimationData(ArrayList<Animation.Frame> frames) {
		this.frames = frames;
		this.current = this.frames.get(0);
	}

	public AnimationData randomize() {
		this.setFrame(Random.newInt(this.frames.size()));
		this.t = Random.newFloat(0f, 100f);
		return this;
	}

	public Animation.Frame getCurrent() {
		return this.current;
	}

	public boolean isPaused() {
		return this.pause;
	}

	public boolean update(float dt) {
		boolean val = false;

		if (!this.pause) {
			this.t += dt;

			if (this.t >= this.current.delay) {
				this.index += (this.back ? -1 : 1);
				this.t = 0;

				if ((!this.back && this.index >= this.frames.size()) || (this.back && this.index < 0)) {
					if (this.listener != null) {
						this.listener.onEnd();
					}

					this.index = (this.back ? this.frames.size() - 1 : 0);
					val = true;

					if (this.auto) {
						this.index = (this.back ? 0 : this.frames.size() - 1);
						this.pause = true;
					}
				}

				if (this.listener != null) {
					this.listener.onFrame(this.index);
				}

				this.current = this.frames.get(this.index);
			}
		}

		return val;
	}

	public void setPaused(boolean pause) {
		this.pause = pause;
	}

	public void setBack(boolean back) {
		this.back = back;
	}

	public void setFrame(int i) {
		this.current = this.frames.get(i);
	}

	public int getFrame() {
		return this.index;
	}

	public void render(float x, float y, boolean flip) {
		Graphics.render(this.current.frame, x, y, 0, 0, 0, flip, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void render(float x, float y, boolean flip, boolean flipY, float ox, float oy, float a) {
		Graphics.render(this.current.frame, x + ox, y + oy, a, ox, oy, flip, flipY);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void render(float x, float y, boolean flip, boolean flipY, float ox, float oy, float a, float sx, float sy) {
		Graphics.render(this.current.frame, x + ox, y + oy, a, ox, oy, flip, flipY, sx, sy);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void render(float x, float y, boolean flip, boolean flipY, int f) {
		Graphics.render(this.frames.get(f).frame, x, y, 0, 0, 0, flip, flipY);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public static class Listener {
		public void onFrame(int frame) {

		}

		public void onEnd() {

		}
	}
}