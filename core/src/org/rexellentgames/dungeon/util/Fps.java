package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class Fps {
	long lastTimeCounted;
	private float sinceChange;
	private float frameRate;

	public Fps() {
		lastTimeCounted = TimeUtils.millis();
		sinceChange = 0;
		frameRate = Gdx.graphics.getFramesPerSecond();
	}

	public void update() {
		long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
		lastTimeCounted = TimeUtils.millis();

		sinceChange += delta;
		if(sinceChange >= 1000) {
			sinceChange = 0;
			frameRate = Gdx.graphics.getFramesPerSecond();
		}
	}

	public float getFrameRate() {
		return frameRate;
	}
}