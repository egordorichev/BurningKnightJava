package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Gold extends Item {
	private static Animation gold = Animation.make("coin", "-gold");
	private static Animation iron = Animation.make("coin", "-iron");
	private static Animation bronze = Animation.make("coin", "-bronze");
	private AnimationData animation = bronze.get("idle");

	{
		name = Locale.get("gold");
		stackable = true;
		autoPickup = true;
		useable = false;
		description = Locale.get("gold_desc");
	}

	@Override
	public void onPickup() {
		Audio.playSfx("coin");
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.animation.update(dt);
	}

	@Override
	public TextureRegion getSprite() {
		return this.animation.getCurrent().frame;
	}

	@Override
	public Item setCount(int count) {
		if (count == 1) {
			this.animation = bronze.get("idle");
		} else if (count >= 5 && count <= 9) {
			this.animation = iron.get("idle");
		} else if (count >= 10) {
			this.animation = gold.get("idle");
		}

		return super.setCount(count);
	}

	@Override
	public void generate() {
		super.generate();

		switch (Random.newInt(10)) {
			case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: this.setCount(1); break;
			case 9: case 8: this.setCount(5); break;
			case 10: this.setCount(10); break;
		}

		if (Player.instance != null) {
			this.setCount((int) (Player.instance.goldModifier * this.getCount()));
		}
	}
}