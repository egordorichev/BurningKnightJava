package org.rexcellentgames.burningknight.entity.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Control extends SaveableEntity {
	public static TextureRegion top = Graphics.getTexture("ui-button_top");
	public static TextureRegion mid = Graphics.getTexture("ui-button_mid");
	public static TextureRegion bottom = Graphics.getTexture("ui-button_bottom");

	private String key;
	private float sc = 4;
	public String id;
	private float mod;

	{
		depth = -1;
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeString(this.id);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.id = reader.readString();
		this.parseId();
	}

	public void parseId() {
		String name = Input.instance.getBinding(this.id);
		key = name; //  name.substring(0, 0).toUpperCase() + name.substring(1, name.length()).toLowerCase();

		Graphics.layout.setText(Graphics.mediumSimple, this.key);
		this.mod = (16 - Graphics.layout.width) / 2;
	}

	@Override
	public void init() {
		super.init();

		if (this.id != null) {
			this.parseId();
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		boolean down = Input.instance.isDown(id);

		this.sc += ((down ? 1 : 4) - this.sc) * dt * 24;
	}

	@Override
	public void render() {
		Graphics.render(bottom, this.x, this.y);
		Graphics.render(mid, this.x, this.y + 1, 0, 0, 0, false, false, 1, this.sc);
		Graphics.render(top, this.x, this.y + 1 + this.sc);

		Graphics.mediumSimple.setColor(0, 0, 0, 1);
		Graphics.print(this.key, Graphics.mediumSimple, this.x + this.mod, this.y - 2 + this.sc);
		Graphics.mediumSimple.setColor(1, 1, 1, 1);
	}
}