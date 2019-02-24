package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Utils;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Item extends Entity {

  public static TextureRegion missing = Graphics.getTexture("item-missing");

  public boolean useOnPickup;
  public float a = 1;
  public boolean shop;
  protected String sprite;
  protected String name;
  protected String description;
  protected boolean stackable = false;
  protected int count = 1;
  protected boolean autoPickup = false;
  protected boolean useable = true;
  protected float delay = 0;
  protected float useTime = 0.5f;
  protected Creature owner;
  protected TextureRegion region;
  protected boolean auto = false;
  protected String useSpeedStr;
  public int price = 15;
  public boolean sale;

  public Item() {
		initStats();
  }

  public void disableAutoPickup() {
  	this.autoPickup = false;
  }

  public int getPrice() {
  	return 5;
  }

  protected Item(String name, String description, String sprite) {
    this.name = name;
    this.description = description;
    this.sprite = sprite;
    initStats();
  }

  private void initStats() {
	  String unlocalizedName = Utils.pascalCaseToSnakeCase(getClass().getSimpleName());

  	if (this.sprite == null) {
  		this.sprite = "item-" + unlocalizedName;
	  }

	  if (this.name == null) {
  		this.name = Locale.get(unlocalizedName);
	  }

	  if (this.description == null) {
		  this.description = Locale.get(unlocalizedName + "_desc");
	  }
  }

  public void updateInHands(float dt) {

  }

  public void generate() {

  }

  public void onPickup() {
  	if (Dungeon.depth != -1 && Dungeon.darkR == Dungeon.MAX_R && Dungeon.game.getState() instanceof InGameState) {
  		Audio.playSfx("pickup_item");
	  }

    if (useOnPickup) {
    	this.owner = Player.instance;
      this.use();
    }
  }

  public boolean isAuto() {
    return this.auto && !this.shop;
  }

  public int getValue() {
    return this.getCount();
  }

  public void render(float x, float y, float w, float h, boolean flipped, boolean back) {

  }

  @Override
  public void update(float dt) {
    this.delay = Math.max(0, this.delay - dt);
  }

  public Creature getOwner() {
    return this.owner;
  }

  public void setOwner(Creature owner) {
	  this.owner = owner;
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
    writer.writeBoolean(this.shop);
    writer.writeByte((byte) this.price);
    writer.writeBoolean(this.sale);
  }

  public void load(FileReader reader) throws IOException {
    this.count = reader.readInt32();
    this.shop = reader.readBoolean();
    this.price = reader.readByte();
    this.sale = reader.readBoolean();
  }

  public TextureRegion getSprite() {
  	if (this.region == null) {
  		this.region = Graphics.getTexture(this.sprite);

      if (this.region == null) {
        Log.error("Invalid item sprite " + this.getClass().getSimpleName());
        this.region = missing;
      }
    }

    return this.region;
  }

  public boolean isUseable() {
    return this.useable;
  }

  public boolean canBeUsed() {
  	return delay == 0;
  }

  public String getName() {
    return this.count == 0 || this.count >= 1 ? this.name : this.name + " (" + this.count + ")";
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

  public boolean hasAutoPickup() {
    return this.autoPickup;
  }


  public String getDescription() {
    return this.description;
  }

  public float getUseTime() {
    return this.useTime;
  }

  public StringBuilder buildInfo() {
    StringBuilder builder = new StringBuilder();

    builder.append(this.getName());
    builder.append("[gray]");

    if (!this.description.isEmpty()) {
      builder.append('\n');
      builder.append(this.getDescription());
    }

    return builder;
  }
}