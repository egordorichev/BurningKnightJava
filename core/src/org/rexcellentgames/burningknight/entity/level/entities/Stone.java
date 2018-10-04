package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Stone extends SolidProp {
	private boolean flip;
	private String old;

	@Override
	public void init() {
		if (this.sprite == null) {
			return;
		}

		old = this.sprite;

		this.flip = Random.chance(50);
		this.createSprite(old);
	}

	private void createSprite(String sprite) {
		switch (sprite) {
			case "prop_stone":
				this.sprite = "props-rock_a";
				collider = new Rectangle(4, 6, 14 - 4 * 2, 12 - 6 - 2);
				w = 14;
				h = 12;
				break;
			case "prop_high_stone":
				this.sprite = "props-rock_b";
				collider = new Rectangle(4, 8, 14 - 4 * 2, 21 - 8 * 2);
				w = 14;
				h = 21;
				break;
			case "prop_big_stone":
				this.sprite = "props-rock_c";
				collider = new Rectangle(4, 8, 28 - 4 * 2, 23 - 8 * 2);
				w = 28;
				h = 23;
				break;
		}

		super.init();
		createCollider();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeString(this.old);
		writer.writeBoolean(flip);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.old = reader.readString();
		flip = reader.readBoolean();

		createSprite(old);
	}

	@Override
	public void render() {
		Graphics.render(region, this.x + region.getRegionWidth() / 2, this.y + region.getRegionHeight() / 2, 0, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, flip ? -1 : 1, 1);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y + 2, this.w, this.h);
	}
}