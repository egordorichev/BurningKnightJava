package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class ItemPrice extends Entity {
	public byte price;
	private String text;
	public boolean sale;

	{
		depth = -1;
	}

	private boolean did;

	public void remove() {
		if (did) {
			return;
		}

		did = true;

		Tween.to(new Tween.Task(0, 0.2f) {
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

	private float a;

	@Override
	public void init() {
		super.init();

		this.text = String.valueOf(this.price);
		Graphics.layout.setText(Graphics.medium, text);

		this.w = Graphics.layout.width;
		this.h = Graphics.layout.height * 2;
		this.x -= this.w / 2;
		this.y -= 16;

		Tween.to(new Tween.Task(1, 0.2f) {
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
		super.render();

		if (sale) {
			Graphics.medium.setColor(1, 0.6f, 0.2f, this.a);
		} else {
			Graphics.medium.setColor(1, 1, 1, this.a);
		}

		Graphics.medium.draw(Graphics.batch, text, this.x, this.y + 16);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}