package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Button;
import org.rexcellentgames.burningknight.util.Random;

public class ButtonPuzzleRoom extends SpecialRoom {
	private boolean done;

	public ButtonPuzzleRoom() {
		this.w = (int) Random.newFloat(this.getMinWidth(), this.getMaxWidth());
		this.w = w % 2 == 0 ? w : w + 1;

		this.h = (int) Random.newFloat(this.getMinHeight(), this.getMaxHeight());
		this.h = h % 2 == 0 ? h : h + 1;
	}

	public void initData() {
		if (!done) {
			done = true;
		} else {
			return;
		}

		switch (Random.newInt(3)) {
			case 0: paintVertical(); break;
			case 1: paintHorizontal(); break;
			case 2: paintVertical(); paintHorizontal(); break;
		}
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		initData();

		int w = this.getWidth() - 2;

		for (int y = 0; y < this.getHeight() - 2; y++) {
			for (int x = 0; x < w; x++) {
				if (this.data[x + y * (w)] > 0) {
					Button button = new Button();

					button.x = (x + this.left) * 16;
					button.y = (y + this.top) * 16;

					Dungeon.area.add(button.add());
				}
			}
		}
	}

	private void paintVertical() {
		int w = this.getWidth() - 2;
		int h = this.getHeight() - 2;

		if (this.data == null) {
			this.data = new byte[w * h];
		}

		for (int y = 2; y < h; y += 2) {
			if (Random.chance(50)) {
				this.data[2 + y * w] = 1;
			} else {
				this.data[2 + y * w] = 2;
			}

			if (Random.chance(50)) {
				this.data[w - 1 + y * w] = 1;
			} else {
				this.data[w - 1 + y * w] = 2;
			}
		}
	}

	private void paintHorizontal() {
		int w = this.getWidth() - 2;
		int h = this.getHeight() - 2;

		if (this.data == null) {
			this.data = new byte[w * h];
		}

		for (int x = 2; x < w; x += 2) {
			if (Random.chance(50)) {
				this.data[x + w * 2] = 1;
			} else {
				this.data[x + w * 2] = 2;
			}

			if (Random.chance(50)) {
				this.data[x + (h - 1) * w] = 1;
			} else {
				this.data[x + (h - 1) * w] = 2;
			}
		}
	}

	private int w;
	private int h;

	@Override
	protected int validateWidth(int w) {
		return this.w;
	}

	@Override
	protected int validateHeight(int h) {
		return this.h;
	}

	public byte[] data;
}