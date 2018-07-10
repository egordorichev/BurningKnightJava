package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class Bloodsplat extends Entity {
	private static ArrayList<Animation.Frame> blood = Animation.make("fx-bloodsplat").getFrames("idle");

	{
		alwaysActive = true;
		alwaysRender = true;
	}

	private float a;
	private float c;
	private TextureRegion texture;
	private float al;
	private float alm;

	@Override
	public void init() {
		super.init();
		this.c = Random.newFloat(0.1f, 0.5f);
		texture = blood.get(Random.newInt(blood.size())).frame;
		a = Random.newFloat(360);
		alm = Random.newFloat(0.5f, 0.8f);

		Tween.to(new Tween.Task(this.alm, 0.05f) {
			@Override
			public float getValue() {
				return al;
			}

			@Override
			public void setValue(float value) {
				al = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.5f) {
					@Override
					public float getValue() {
						return al;
					}

					@Override
					public void setValue(float value) {
						al = value;
					}

					@Override
					public void onEnd() {
						setDone(true);
					}
				}).delay(0.5f);
			}
		});
	}

	@Override
	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.setColor(1, this.c, this.c, this.al);
		Graphics.render(texture, this.x, this.y, this.a, texture.getRegionWidth() / 2, texture.getRegionHeight() / 2, false, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}