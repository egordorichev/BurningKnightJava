package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class NpcDialog extends Entity {
	private Npc npc;
	private String message;
	private String full;

	{
		depth = 14;
		alwaysActive = true;
		alwaysRender = true;
	}

	public NpcDialog(Npc npc, String message) {
		this.npc = npc;
		this.w = 4f;
		this.h = 0;
		this.setMessage(message);
	}

	public void setMessage(String message) {
		this.full = message;
		this.message = "";
		this.open();
	}

	// private static Color color = Color.valueOf("#0e071b");

	private TextureRegion top = Graphics.getTexture("bubble-top");
	private TextureRegion topLeft = Graphics.getTexture("bubble-top_left");
	private TextureRegion topRight = Graphics.getTexture("bubble-top_right");
	private TextureRegion center = Graphics.getTexture("bubble-center");
	private TextureRegion left = Graphics.getTexture("bubble-left");
	private TextureRegion right = Graphics.getTexture("bubble-right");
	private TextureRegion bottom = Graphics.getTexture("bubble-bottom");
	private TextureRegion bottomLeft = Graphics.getTexture("bubble-bottom_left");
	private TextureRegion bottomRight = Graphics.getTexture("bubble-bottom_right");
	private TextureRegion overlay = Graphics.getTexture("bubble-overlay");
	@Override
	public void render() {
		float x = Math.round(this.npc.x + this.npc.w / 2 + this.x);
		float y = Math.round(this.npc.y + this.npc.h + 8);

		float sx = (this.w - topLeft.getRegionWidth() * 2) / ((float) top.getRegionWidth());
		float sy = (this.h - left.getRegionHeight()) / ((float) left.getRegionHeight());

		Graphics.render(top, x - this.w / 2 + topLeft.getRegionWidth(), y + this.h / 2 - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topLeft, x - this.w / 2, y + this.h / 2 - topLeft.getRegionHeight());
		Graphics.render(topRight, x + this.w / 2 - topRight.getRegionWidth(), y + this.h / 2 - topRight.getRegionHeight());

		Graphics.render(left, x - this.w / 2, y - this.h / 2 + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(right, x + this.w / 2 - right.getRegionWidth(), y - this.h / 2 + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);

		Graphics.render(center, x - this.w / 2 + left.getRegionWidth(), y - this.h / 2 + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, sx, sy);

		Graphics.render(bottom, x - this.w / 2 + bottomLeft.getRegionWidth(), y - this.h / 2, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomLeft, x - this.w / 2, y - this.h / 2);
		Graphics.render(bottomRight, x + this.w / 2 - topRight.getRegionWidth(), y - this.h / 2);

		Graphics.render(overlay, x - this.w / 2 + bottomLeft.getRegionWidth(), y - this.h / 2 - 5);

		Graphics.small.setColor(1, 1, 1, 1);

		Graphics.write(this.full, Graphics.small, this.npc.x + this.npc.w / 2 + this.x - this.w / 2 + 6,
			this.npc.y + this.npc.h + this.h - 8);
	}

	private Tween.Task last;
	private float a;

	public void open() {
		toRemove = false;

		if (this.last != null) {
			Tween.remove(this.last);
			this.last = null;
		}

		this.h = Graphics.layout.height + 8;
		this.w = Graphics.layout.width + 8;
		this.message = this.full;
		this.a = 1;

		this.last = Tween.to(new Tween.Task(1, 0.3f) {
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

	private boolean toRemove;

	public void remove() {
		toRemove = true;

		if (this.last != null) {
			Tween.remove(this.last);
			this.last = null;
		}

		this.last = Tween.to(new Tween.Task(0, 0.3f) {
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
}