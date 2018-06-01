package org.rexellentgames.dungeon.entity.item.pet.impl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;

public class PetEntity extends SaveableEntity {
	public Player owner;
	public float z;
	public TextureRegion region = Item.missing; // todo: replace with anim

	@Override
	public void init() {
		super.init();

		this.owner = Player.instance;
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y);
	}
}