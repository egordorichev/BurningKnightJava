package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.graphics.Color;
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

	private Color color = Color.valueOf("0e071b");

	@Override
	public void render() {
		float x = Math.round(this.npc.x + this.npc.w / 2 + this.x - topLeft.getRegionWidth());
		float y = Math.round(this.npc.y + this.npc.h + 10 - this.h / 2);

		float sx = (this.w - topLeft.getRegionWidth() * 2) / ((float) top.getRegionWidth());
		float sy = (this.h - left.getRegionHeight()) / ((float) left.getRegionHeight());

		Graphics.batch.setColor(1, 1, 1, this.a);

		Graphics.render(top, x + topLeft.getRegionWidth(), y + this.h - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topLeft, x, y + this.h - topLeft.getRegionHeight());
		Graphics.render(topRight, x + this.w - topRight.getRegionWidth(), y + this.h - topRight.getRegionHeight());

		Graphics.render(left, x, y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(right, x + this.w - right.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, 1, sy);
		Graphics.render(center, x + left.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0, false, false, sx, sy);

		Graphics.render(bottom, x+ bottomLeft.getRegionWidth(),
			y, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomLeft, x, y);
		Graphics.render(bottomRight, x + this.w - topRight.getRegionWidth(), y);

		Graphics.render(overlay, x + bottomLeft.getRegionWidth(), y - 4);

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.smallSimple.setColor(color.r, color.g, color.b, this.a);

		Graphics.print(this.message, Graphics.smallSimple, x + 4,
			y + 4);

		Graphics.smallSimple.setColor(1, 1, 1, 1);
	}

	private Tween.Task last;
	private float a;

	public void open() {
		toRemove = false;

		if (this.last != null) {
			Tween.remove(this.last);
			this.last = null;
		}

		Graphics.layout.setText(Graphics.smallSimple, this.full);
		this.h = Graphics.layout.height + 12;
		this.w = Graphics.layout.width + 8;
		this.message = "";
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

	private float lastLt;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.message.length() != this.full.length()) {
			this.lastLt = Math.max(0, lastLt - dt);

			if (this.lastLt == 0) {
				lastLt = 0.05f;
				this.message = this.full.substring(0, this.message.length() + 1);
			}
		}
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