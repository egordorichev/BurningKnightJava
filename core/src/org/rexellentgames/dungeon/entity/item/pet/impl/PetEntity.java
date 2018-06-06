package org.rexellentgames.dungeon.entity.item.pet.impl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class PetEntity extends SaveableEntity {
	public Player owner;
	public float z;
	protected String sprite = "";
	public TextureRegion region = Item.missing; // todo: replace with anim

	{
		alwaysActive = true;
	}

	protected boolean noTp;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!this.onScreen && !this.noTp) {
			this.x = this.owner.x + this.owner.w / 2;
			this.y = this.owner.y + this.owner.h / 2;
			this.tp();
		}
	}

	protected void tp() {

	}


	@Override
	public void init() {
		super.init();

		if (!this.sprite.isEmpty()) {
			this.region = Graphics.getTexture(this.sprite);
		}

		this.owner = Player.instance;
		double a = Random.newFloat() * Math.PI * 2;
		float d = 24f;

		this.x = this.owner.x + this.owner.w / 2 + (float) (Math.cos(a) * d);
		this.y = this.owner.y + this.owner.h / 2 + (float) (Math.sin(a) * d);
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y);
	}
}