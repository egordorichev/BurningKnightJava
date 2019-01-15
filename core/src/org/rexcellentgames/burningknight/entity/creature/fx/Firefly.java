package org.rexcellentgames.burningknight.entity.creature.fx;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.levels.ice.IceLevel;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;

public class Firefly extends SaveableEntity {
	{
		depth = 14;
		w = 12;
		h = 12;
	}

	private PointLight light;
	private float t;
	private boolean ice;

	@Override
	public void init() {
		super.init();
		ice = Dungeon.level instanceof IceLevel;
		this.t = Random.newFloat(1024);
		light = World.newLight(32, ice ? new Color(0, 0.3f, 1, 1f) : new Color(0, 1, 0.3f, 1f), 0, x, y);
		light.setXray(true);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.t += dt * 2;
	}

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
	}

	private float rd = 6;

	@Override
	public void render() {
		int r = 6;
		boolean on = this.t % 20 <= 16f;

		rd += ((on ? 6 : 0) - rd) * Gdx.graphics.getDeltaTime() * 3;
		rd = MathUtils.clamp(0, 6, rd);

		float x = (float) (this.x + Math.cos(this.t / 8) * Math.sin(this.t / 9) * 32);
		float y = (float) (this.y + Math.sin(this.t / 7) * Math.cos(this.t / 10) * 32);

		light.setPosition(x + r, y + r);
		light.setDistance(rd * 10);

		if (ice) {
			light.setColor(0, 0.3f, 1, 1);
		} else {
			light.setColor(0, 1, 0.3f, 1);
		}

		Graphics.startAlphaShape();

		Color color = ice ? colorIce : Firefly.color;
		Color off = ice ? offIce : Firefly.off;

		if (rd > 2) {
			Graphics.shape.setColor(color.r, color.g, color.b, 0.3f);
			Graphics.shape.circle(x + r, y + r, rd);
		}

		Graphics.shape.setColor(on ? color.r : off.r, on ? color.g : off.g, on ? color.b : off.b, 1);
		Graphics.shape.circle(x + r, y + r, 2);
		Graphics.endAlphaShape();
	}

	private static Color color = Color.valueOf("#99e65f");
	private static Color off = Color.valueOf("#134c4c");

	public static Color colorIce = Color.valueOf("#0cf1ff");
	private static Color offIce = Color.valueOf("#0069aa");
}