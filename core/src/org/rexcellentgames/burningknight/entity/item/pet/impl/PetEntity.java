package org.rexcellentgames.burningknight.entity.item.pet.impl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Contact;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.util.Random;

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

	@Override
	public boolean shouldCollide(Entity entity, Contact contact) {
		if  (entity instanceof Creature || entity instanceof Door || entity instanceof SolidProp || (entity instanceof ItemHolder && !(((ItemHolder) entity).getItem() instanceof Gold))) {
			return false;
		}

		return super.shouldCollide(entity, contact);
	}
}