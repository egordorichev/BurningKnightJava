package org.rexcellentgames.burningknight.entity.level.entities.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class LadderFx extends UiEntity {
	private Entity ladder;
	private String text;
	private float a;

	{
		alwaysActive = true;
	}

	public LadderFx(Entity ladder, String text) {
		this.ladder = ladder;
		this.text = Locale.get(text);

		GlyphLayout layout = new GlyphLayout(Graphics.medium, this.text);

		this.x = ladder.x + 8 - layout.width / 2;
		this.y = ladder.y + ladder.h + 4;

		this.depth = 16;

		Tween.to(new Tween.Task(1, 0.1f, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}
		});
	}

	@Override
	public void render() {
		float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

		Graphics.medium.setColor(c, c, c, this.a);
		Graphics.print(this.text, Graphics.medium, this.x, this.y);
		Graphics.medium.setColor(1, 1, 1, 1);

		if (Input.instance.wasPressed("interact") && Dialog.active == null && Player.instance.pickupFx == null) {
			this.remove();
			this.end();
		}
	}

	public void end() {
		Dungeon.darkR = 0;
		Dungeon.darkR = Dungeon.MAX_R;
		Player.instance.setUnhittable(true);
		Camera.follow(null);

		Player.instance.playSfx("menu/select");
		InGameState.startTween = true;
		InGameState.id = ((Exit) ladder).getType();
	}

	public void remove() {
		Tween.to(new Tween.Task(0, 0.2f, Tween.Type.QUAD_IN) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();
				setDone(true);
			}
		});
	}
}