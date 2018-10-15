package org.rexcellentgames.burningknight.entity.level.entities.shop;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class SolidShopProp extends SolidProp {
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
		if (this.sprite.equals("shop-table")) {
			this.collider = new Rectangle(0, 10, 60, 25 - 15);
			this.createCollider();
		} else if (this.sprite.equals("shop-table_2")) {
			this.collider = new Rectangle(0, 10, 42, 28 - 15);
			this.createCollider();
		} else if (this.sprite.equals("shop-cauldron")) {
			this.collider = new Rectangle(6, 10, 37 - 12, 35 - 20);
			this.createCollider();
		} else if (this.sprite.equals("shop-shelf")) {
			this.collider = new Rectangle(12, 10, 42 - 24, 10);
			this.createCollider();
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