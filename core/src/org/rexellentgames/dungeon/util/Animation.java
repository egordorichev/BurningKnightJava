package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.net.Network;

import java.util.ArrayList;

public class Animation {
	private ArrayList<Frame> frames = new ArrayList<Frame>();
	private Frame current;
	private int index;
	private float t;

	public Animation(String file, String state) {
		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("sprites_split/" + file + ".json"));

		// todo: load

		this.current = this.frames.get(0);
	}

	public static Animation make(String file, String state) {
		if (Network.SERVER) {
			return null;
		} else {
			return new Animation(file, state);
		}
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

	private class Frame {
		public TextureRegion frame;
		public float delay;
	}
}