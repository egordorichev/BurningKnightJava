package org.rexellentgames.dungeon.entity.level.entities.fx;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.ui.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.ui.UiEntity;
import org.rexellentgames.dungeon.util.Dialog;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

public class LadderFx extends UiEntity {
	private Entity ladder;
	private String text;
	private float a;

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

		if (Input.instance.wasPressed("action") && Dialog.active == null) {
			this.remove();

			if (this.ladder instanceof Entrance) {
				if (Dungeon.depth <= 0) {
					UiLog.instance.print("[orange]You cant leave just yet!");
				} else {
					this.end();
				}
			} else if (this.ladder instanceof Exit) {
				if (Dungeon.depth == 4) {
					UiLog.instance.print("[red]Not implemented just yet!");
				} else {
					this.end();
				}
			}
		}
	}

	public void end() {
		Dungeon.darkR = Dungeon.MAX_R;
		Player.instance.setUnhittable(true);
		Camera.instance.follow(null);

		Dungeon.darkX = this.fromWorldX(this.ladder.x + this.ladder.w / 2);
		Dungeon.darkY = this.fromWorldY(this.ladder.y + this.ladder.h / 2);

		Tween.to(new Tween.Task(0, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.darkR;
			}

			@Override
			public void setValue(float value) {
				Dungeon.darkR = value;
			}

			@Override
			public void onEnd() {
				if (ladder instanceof Entrance) {
					Dungeon.loadType = Entrance.LoadType.GO_UP;
					Dungeon.ladderId = ((Entrance) ladder).getType();

					Dungeon.goToLevel(Dungeon.depth - 1);
				} else {
					if (Dungeon.type == Dungeon.Type.INTRO) {
						Log.info("Tutorial finished");

						Dungeon.type = Dungeon.Type.REGULAR;
						Dungeon.newGame();
					} else {
						Dungeon.loadType = Entrance.LoadType.GO_DOWN;
						Dungeon.ladderId = ((Exit) ladder).getType();

						if (((Exit) ladder).getType() == Entrance.ENTRANCE_TUTORIAL) {
							Log.info("Entering tutorial");

							Dungeon.type = Dungeon.Type.INTRO;
						}

						Dungeon.goToLevel(Dungeon.depth + 1);
					}
				}
			}
		});
	}

	public void remove() {
		LadderFx self = this;
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

				self.done = true;
			}
		});
	}
}