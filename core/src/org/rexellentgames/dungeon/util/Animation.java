package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.net.Network;

import java.util.ArrayList;
import java.util.HashMap;

public class Animation {
	private HashMap<String, ArrayList<Frame>> frames = new HashMap<>();

	public Animation(String file) {
		this(file, "");
	}

	public Animation(String file, String add) {
		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("sprites_split/" + file + ".json"));

		JsonValue meta = root.get("meta");
		JsonValue frameTags = meta.get("frameTags");
		JsonValue frames = root.get("frames");

		for (JsonValue tag : frameTags) {
			int from = tag.getInt("from");
			int to = tag.getInt("to");
			String state = tag.getString("name");

			ArrayList<Frame> framesList = new ArrayList<>();

			for (int i = from; i <= to; i++) {
				JsonValue frame = frames.get(i);
				String name = frame.name;
				int delay = frame.getInt("duration");

				// this: actor_towelknight 0.ase -- and state dead
				// into this: actor-towelknight-dead-00

				name = name.replace(".ase", "");
				name = name.replace('_', '-');
				name = name.replace(' ', '-');

				name = name.substring(0, name.length() - (Character.isDigit(name.charAt(name.length() - 2)) ? 3 : 2));
				name += add + "-" + state + "-" + String.format("%02d", i - from);

				framesList.add(new Frame(Graphics.getTexture(name), delay * 0.001f));
			}

			this.frames.put(state, framesList);
		}
	}

	public static Animation make(String file) {
		if (Network.SERVER) {
			return null;
		} else {
			return new Animation(file);
		}
	}

	public static Animation make(String file, String add) {
		if (Network.SERVER) {
			return null;
		} else {
			return new Animation(file, add);
		}
	}

	public AnimationData get(String state) {
		ArrayList<Frame> data = this.frames.get(state);

		if (data == null) {
			return null;
		}

		return new AnimationData(data);
	}

	public class Frame {
		public TextureRegion frame;
		public float delay;

		public Frame(TextureRegion frame, float delay) {
			this.frame = frame;
			this.delay = delay;
		}
	}
}