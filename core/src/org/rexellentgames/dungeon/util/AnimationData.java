package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import org.rexellentgames.dungeon.assets.Graphics;

import java.util.ArrayList;

public class AnimationData {
	private final ArrayList<Animation.Frame> frames;
	private float t;
	private int index;
	private boolean pause;
	private boolean back;
	private Animation.Frame current;
	private boolean auto;

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
					this.index = (this.back ? this.frames.size() - 1 : 0);
					val = true;

					if (this.auto) {
						this.index = (this.back ? 0 : this.frames.size() - 1);
						this.pause = true;
					}
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
		render(x, y, flip, true);
	}

	public void render(float x, float y, boolean flip, boolean s) {
		if (s) {
			Color color = Graphics.batch.getColor();
			Graphics.batch.end();
			Graphics.shadows.begin();

			Graphics.batch.begin();

			Graphics.batch.setColor(0, 0, 0, 1f);
			Graphics.render(this.current.frame, x, y - this.current.frame.getRegionHeight(), 0, 0, 0, flip, true,
				flip ? -1f : 1f, -0.5f);
			Graphics.batch.setColor(color);

			Graphics.batch.end();

			Graphics.shadows.end();
			Graphics.batch.begin();
		}

		Graphics.render(this.current.frame, x, y, 0, 0, 0, flip, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void render(float x, float y, boolean flip, boolean flipY, float ox, float oy, float a) {
		render(x, y, flip, flipY, ox, oy, a, true);
	}

	public void render(float x, float y, boolean flip, boolean flipY, float ox, float oy, float a, boolean s) {

		if (s) {
			Color color = Graphics.batch.getColor();
			Graphics.batch.end();
			Graphics.shadows.begin();

			Graphics.batch.begin();

			Graphics.batch.setColor(0, 0, 0, 1f);
			Graphics.render(this.current.frame, x + ox, y + oy - this.current.frame.getRegionHeight(), a, ox, oy, flip, flipY, flip ? -1f : 1f, flipY ? 0.5f : -0.5f);
			Graphics.batch.setColor(color);

			Graphics.batch.end();

			Graphics.shadows.end();
			Graphics.batch.begin();
		}

		Graphics.render(this.current.frame, x + ox, y + oy, a, ox, oy, flip, flipY);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void render(float x, float y, boolean flip, boolean flipY, int f) {
		render(x, y, flip, flipY, f, true);
	}

	public void render(float x, float y, boolean flip, boolean flipY, int f, boolean s) {
		if (s) {
			Color color = Graphics.batch.getColor();
			Graphics.batch.end();
			Graphics.shadows.begin();

			Graphics.batch.begin();

			Graphics.batch.setColor(0, 0, 0, 1f);
			Graphics.render(this.frames.get(f).frame, x, y - this.current.frame.getRegionHeight(), 0, 0, 0, flip, flipY, flip ? -1f : 1f, flipY ? 0.5f : -0.5f);
			Graphics.batch.setColor(color);

			Graphics.batch.end();

			Graphics.shadows.end();
			Graphics.batch.begin();
		}

		Graphics.render(this.frames.get(f).frame, x, y, 0, 0, 0, flip, flipY);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}