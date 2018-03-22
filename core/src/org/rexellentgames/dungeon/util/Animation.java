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

		JsonValue meta = root.get("meta");
		JsonValue frameTags = meta.get("frameTags");
		JsonValue frames = root.get("frames");

		JsonValue needed = null;

		// todo: check!

		for (JsonValue tag : frameTags) {
			String name = tag.get("name").asString();

			if (name.equals(state)) {
				needed = tag;
				break;
			}
		}

		if (needed == null) {
			Log.error("Failed to find " + state + " in " + file);
			return;
		}

		int from = needed.getInt("from");
		int to = needed.getInt("to");

		for (int i = from; i <= to; i++) {
			JsonValue frame = frames.get(i);
			String name = frame.name;
			int delay = frame.getInt("duration");

			// this: actor_towelknight 0.ase -- and state dead
			// into this: actor-towelknight-dead-00

			name = name.replace(".ase", "");
			name = name.replace('_', '-');
			name = name.replace(' ', '-');

			int num = 0;
			int j = 0;

			Log.info("the name was: " + name);

			while (Character.isDigit(name.charAt(name.length() - 1))) {
				j += 1;

				num += Integer.valueOf(name.charAt(name.length() - 1)) * (10 ^ j);
				name = name.substring(0, name.length() - 1);
			}

			name += String.format("%02d", num);
			Log.info("the num is: " + num);

			this.frames.add(new Frame(Graphics.getTexture(name), 0.001f * delay));
		}

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

		public Frame(TextureRegion frame, float delay) {
			this.frame = frame;
			this.delay = delay;
		}
	}
}