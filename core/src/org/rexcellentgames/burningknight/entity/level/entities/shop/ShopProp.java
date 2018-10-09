package org.rexcellentgames.burningknight.entity.level.entities.shop;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.entities.Prop;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class ShopProp extends Prop {
	private boolean shadow = false;

	@Override
	public void renderShadow() {
		if (shadow) {
			super.renderShadow();
		}
	}

	@Override
	public void init() {
		super.init();

		if (this.sprite != null) {
			this.parseName();
		}
	}

	private void parseName() {
		if (this.sprite.equals("shop-carpet")) {
			this.depth = -1;
			this.shadow = false;
		} else if (this.sprite.equals("shop-bat") || this.sprite.equals("shop-frog") || this.sprite.equals("shop-bone") || this.sprite.equals("shop-skull")) {
			this.depth = 1;
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.sprite = reader.readString();
		this.region = Graphics.getTexture(this.sprite);

		parseName();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeString(this.sprite);
	}
}