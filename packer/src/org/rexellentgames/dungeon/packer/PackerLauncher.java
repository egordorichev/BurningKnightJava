package org.rexellentgames.dungeon.packer;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class PackerLauncher {
	public final static void main(String[] args) {
		final File folder = new File("sprites_split");

		final File[] files = folder.listFiles((dir, name) -> name.indexOf('@') != -1);

		for (final File file : files) {
			if (!file.delete()) {
				System.err.println("Can't remove " + file.getAbsolutePath());
			}
		}

		TexturePacker.process("sprites_split", "atlas", "atlas");
	}
}