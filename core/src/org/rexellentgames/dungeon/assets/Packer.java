package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Packer {
	public final static void main(String[] args) {
		TexturePacker.process("sprites_split", "atlas", "atlas");
	}
}