package org.rexellentgames.dungeon.assets;

//import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.rexellentgames.dungeon.util.Log;

import java.io.File;
import java.io.FilenameFilter;

public class Packer {
	public final static void main(String[] args) {
		final File folder = new File("sprites_split");

		final File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.indexOf('@') != -1;
			}
		});

		for (final File file : files) {
			if (!file.delete()) {
				System.err.println("Can't remove " + file.getAbsolutePath());
			}
		}

		// TexturePacker.process("sprites_split", "atlas", "atlas");
	}
}