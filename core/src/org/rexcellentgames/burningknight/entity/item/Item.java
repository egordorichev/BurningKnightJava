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
import org.rexcellentgames.burningknight.util.Random;
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
  protected boolean identified;
  protected boolean cursed;
  protected Creature owner;
  protected TextureRegion region;
  protected boolean auto = false;
  protected int level = 1;
  protected String useSpeedStr;
  public int price = 15;
  public boolean sale;

  public Item() {
		initStats();
  }

  public int getMaxLevel() {
  	return 11;
  }

  public int getMinLevel() {
  	return -1;
  }

  public boolean canBeDegraded() {
  	return false;
  }

  public void upgrade() {
  	if (!canBeUpgraded()) {
  		return;
	  }

	  this.cursed = false;
  	this.level = (byte) Math.min(this.getMaxLevel(), this.level + 1);
  }

	public void degrade() {
  	if (!canBeDegraded()) {
  		return;
	  }

		this.level = (byte) Math.max(this.getMinLevel(), this.level - 1);
	}

  public void disableAutoPickup() {
  	this.autoPickup = false;
  }

	public int getLevel() {
		return level;
	}

	public boolean canBeUpgraded() {
		return false;
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
	  String unlocalizedName = Utils.INSTANCE.pascalCaseToSnakeCase(getClass().getSimpleName());

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
  
  public String getUseSpeedAsString() {
    String str = "super_slow";

    if (this.useTime <= 0.1f) {
      str = "insane_fast";
    } else if (this.useTime <= 0.2f) {
      str = "fast";
    } else if (this.useTime <= 0.4f) {
      str = "normal_spd";
    } else if (this.useTime <= 0.5f) {
      str = "slow";
    }

    return Locale.get(str);
  }

  public void generate() {
  	float r = Random.newFloat();

	  if (r <= 0.2f) {
			if (this.canBeDegraded()) {
				this.degrade();

				/*if (Random.chance(50)) {
					this.cursed = true;
				}*/

				if (Random.chance(30)) {
					this.degrade();
				}
			}
		} else if (r <= 0.4f) {
			if (this.canBeUpgraded()) {
				this.upgrade();

				if (Random.chance(30)) {
					this.upgrade();
				}
			}
		}
  }

	public void setLevel(byte level) {
		this.level = level;
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

  public void render(float x, float y, float w, float h, boolean flipped) {
    /*getSprite();
    
    Graphics.render(this.region, x + (w - this.region.getRegionWidth()) / 2 + (flipped ? -w / 2 : w / 2),
      y + (h - this.region.getRegionHeight()) / 2, 0, this.region.getRegionWidth() / 2, this.region.getRegionHeight() / 2, flipped, false);*/
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
    writer.writeByte((byte) this.level);
	  writer.writeBoolean(this.identified);
	  writer.writeBoolean(this.cursed);
  }

  public void load(FileReader reader) throws IOException {
    this.count = reader.readInt32();
    this.shop = reader.readBoolean();
    this.price = reader.readByte();
    this.sale = reader.readBoolean();
    this.level = reader.readByte();
    this.identified = reader.readBoolean();
	  this.cursed = reader.readBoolean();
  }

  public boolean disableBlink() {
  	return false;
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
  	return true;
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

    if (this.cursed && this.identified) {
      builder.append("\n[red]");
      builder.append(cursedLocale);
      builder.append("[gray]");
    }

    return builder;
  }

  private static String cursedLocale = Locale.get("cursed");
}