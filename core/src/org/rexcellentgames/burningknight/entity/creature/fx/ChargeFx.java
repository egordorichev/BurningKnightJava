package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Lamp;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

public class ChargeFx extends Entity {
	private static Animation animations = Animation.make("fx_charge");
	private TextureRegion region;
	private float val;
	public Entity owner;

	{
		depth = 15;
		alwaysActive = true;
	}

	public ChargeFx(float x, float y, float val) {
		this.x = x;
		this.y = y;
		this.val = val;
		this.region = animations.getFrames("idle").get(Random.newInt(animations.getFrames("idle").size())).frame;
	}

	@Override
	public void update(float dt) {
		float dx = this.x - (this.owner.w / 2 + this.owner.x - 2);
		float dy = this.y - (this.owner.h / 2 + this.owner.y - 2);
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		this.x -= dx / d * 3;
		this.y -= dy / d * 3;

		if (d < 3) {
			this.done = true;
			Lamp lamp = (Lamp) Player.instance.getInventory().findItem(Lamp.class);

			if (lamp != null) {
				float v = lamp.val;

				lamp.val = Math.min(100f, lamp.val + this.val);

				if (v < 100f && lamp.val == 100f) {
					Dungeon.area.add(new TextFx("Lamp Charged", this.owner).setColor(Dungeon.YELLOW));
				}
			}
		}
	}

	@Override
	public void render() {
		Graphics.render(this.region, this.x - 2, this.y - 2);
	}
}