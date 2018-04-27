package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Item extends Entity {
	protected String sprite = "missing texture";
	protected String name = "Missing Item Name";
	protected String description = "";
	protected boolean stackable = false;
	protected int count = 1;
	protected boolean autoPickup = false;
	protected boolean useable = true;
	protected float delay = 0;
	protected float useTime = 0.5f;
	protected boolean identified;
	protected boolean cursed;
	protected Creature owner;
	protected TextureRegion region;
	protected boolean auto = false;
	protected boolean fly = false;

	public void generate() {

	}

	public boolean isFlying() {
		return this.fly;
	}

	public void onPickup() {

	}

	public boolean isAuto() {
		return this.auto;
	}

	public int getValue() {
		return this.getCount();
	}

	public void setOwner(Creature owner) {
		this.owner = owner;
	}

	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion s = this.getSprite();

		Graphics.startShadows();
		Graphics.render(s, x + (w - s.getRegionWidth()) / 2, y - h / 2, 0, s.getRegionWidth() / 2, s.getRegionHeight() / 2, flipped, false, flipped ? 1f : -1f, -1f);
		Graphics.endShadows();
		Graphics.render(s, x + (w - s.getRegionWidth()) / 2, y + h / 2, 0, s.getRegionWidth() / 2, s.getRegionHeight() / 2, flipped, false);
	}

	public void beforeRender(float x, float y, float w, float h, boolean flipped) {

	}

	@Override
	public void update(float dt) {
		this.delay = Math.max(0, this.delay - dt);
	}

	public Creature getOwner() {
		return this.owner;
	}

	public void use() {
		this.delay = this.useTime;
	}

	public void secondUse() {
		this.delay = this.useTime;
	}

	public void endUse() {

	}

	public float getDelay() {
		return this.delay;
	}

	public void save(FileWriter writer) throws IOException {
		writer.writeInt32(this.count);
	}

	public void load(FileReader reader) throws IOException {
		this.count = reader.readInt32();
	}

	public TextureRegion getSprite() {
		if (this.region == null) {
			this.region = Graphics.getTexture(this.sprite);
		}

		return this.region;
	}

	public boolean isUseable() {
		return this.useable;
	}

	public String getName() {
		return this.count == 1 ? this.name : this.name + " (" + this.count + ")";
	}

	public boolean isStackable() {
		return this.stackable;
	}

	public int getCount() {
		return this.count;
	}

	public Item setCount(int count) {
		this.count = count;
		return this;
	}

	public Item randomize() {
		return this;
	}

	public boolean hasAutoPickup() {
		return this.autoPickup;
	}

	public boolean isCursed() {
		return this.cursed;
	}

	public boolean isIdentified() {
		return this.identified;
	}

	public void identify() {
		this.identified = true;
	}

	public String getDescription() {
		return this.identified ? this.description : "???";
	}

	public float getUseTime() {
		return this.useTime;
	}

	public StringBuilder buildInfo() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.getName());
		builder.append('\n');
		builder.append(this.getDescription());

		if (this.cursed) {
			builder.append("\n[red]Cursed[white]");
		}

		return builder;
	}
}