package org.rexcellentgames.burningknight.entity.level.entities;

import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class AnswerButton extends Button {
	public boolean shouldBeDown;
	public static ArrayList<AnswerButton> all = new ArrayList<>();

	@Override
	public void init() {
		super.init();
		all.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		shouldBeDown = reader.readBoolean();
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(shouldBeDown);
	}

	@Override
	public void onPress() {
		super.onPress();

		if (this.shouldBeDown) {
			Log.info(all.size() + " size");

			for (AnswerButton button : all) {
				if (button.shouldBeDown && !button.isDown()) {
					return;
				} else if (!button.shouldBeDown && button.isDown()) {
					return;
				}
			}

		}
	}
}