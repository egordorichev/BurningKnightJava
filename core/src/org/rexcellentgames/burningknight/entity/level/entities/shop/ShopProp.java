package org.rexcellentgames.burningknight.entity.level.entities.shop;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.entities.Prop;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class ShopProp extends Prop {
	private boolean shadow = false;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!parsed) {
			parsed = true;
			this.parseName();
		}
	}

	private boolean parsed;

	@Override
	public void renderShadow() {
		if (shadow) {
			super.renderShadow();
		}
	}

	private void parseName() {
		if (this.sprite.equals("shop-carpet") || this.sprite.equals("shop-blood")) {
			this.depth = -9;
			this.shadow = false;
		} else if (this.sprite.equals("shop-bat") || this.sprite.equals("shop-frog") || this.sprite.equals("shop-bone") || this.sprite.equals("shop-skull")) {
			this.depth = 1;
		} else if (sprite.equals("shop-frame_a") || sprite.equals("shop-frame_b") || sprite.equals("shop-target") || sprite.equals("shop-shields") || sprite.equals("shop-maniken")) {
			this.depth = 6;
		}

		if (sprite.startsWith("shop-frame")) {
			this.shadow = false;
		}

		this.w = this.region.getRegionWidth();
		this.h = this.region.getRegionHeight();
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.sprite = reader.readString();
		this.region = Graphics.getTexture(this.sprite);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeString(this.sprite);
	}
}