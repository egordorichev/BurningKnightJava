package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Utils;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.HashMap;

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
  protected boolean identified = true;
  protected boolean cursed;
  protected Creature owner;
  protected TextureRegion region;
  protected boolean auto = false;
  protected boolean fly = false;
  protected byte level = 1;
  protected String useSpeedStr;
  public byte price = 15;
  public boolean sale;

  public Item() {
		initStats();
  }

  public void upgrade() {
  	this.level ++;
  }

	public byte getLevel() {
		return level;
	}

	public boolean canBeUpgraded() {
		return false;
	}

	// todo: depend on quality
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

  }

  public boolean isFlying() {
    return this.fly;
  }

  public void onPickup() {
    Audio.playSfx("pickup_item");

    if (useOnPickup) {
    	this.owner = Player.instance;
      this.use();
    }
  }

  public boolean isAuto() {
    return this.auto;
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
    triggerEvent("use");
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
    writer.writeByte(this.price);
    writer.writeBoolean(this.sale);
    writer.writeByte(this.level);
  }

  public void load(FileReader reader) throws IOException {
    this.count = reader.readInt32();
    this.shop = reader.readBoolean();
    this.price = reader.readByte();
    this.sale = reader.readBoolean();
    this.level = reader.readByte();
  }

  public boolean disableBlink() {
  	return false;
  }

  public TextureRegion getSprite() {
  	if (this.region == null) {
      if (this.modId != null) {
      	this.region = Graphics.getModTexture(this.modId, this.sprite);

        if (this.region == null) {
	        this.region = Graphics.getTexture(this.sprite);
        }
      } else {
      	this.region = Graphics.getTexture(this.sprite);
		  }

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
  	if (this.canBeUsedCallback != null) {
  		try {
			  LuaValue can = this.canBeUsedCallback.call(self, this.owner.self);

			  if (can.isboolean()) {
			  	return can.toboolean();
			  }
		  } catch (LuaError error) {
  			Log.error("Internal mod error");
  			error.printStackTrace();
		  }
	  }

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
    return this.identified ? this.description : "???";
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

    if (this.cursed) {
      builder.append("\n[red]Cursed[gray]");
    }

    return builder;
  }

  /*
   * Lua logic
   */

  private HashMap<String, LuaFunction> events = new HashMap<>();

  public void registerEvent(String name, LuaValue value) {
  	LuaValue val = value.get(name);

  	if (val != LuaValue.NIL && val.isfunction()) {
  		events.put(name, (LuaFunction) val);
	  }
  }

  public void triggerEvent(String name) {
  	LuaFunction fun = events.get(name);

  	if (fun != null) {
  		try {
			  fun.call(self, this.owner.self);
		  } catch (LuaError error) {
			  Log.error("Internal mod error!");
  			error.printStackTrace();
		  }
	  }
  }

  private String modId;
  protected LuaValue self;
	protected LuaFunction canBeUsedCallback;

  public void initFromMod(String modId, String name, LuaTable args) {
	  this.modId = modId;

	  this.name = Locale.get(name);
	  this.description = Locale.get(name + "_desc");

	  LuaValue val = args.get("sprite");

	  if (val == LuaValue.NIL) {
		  this.sprite = "item-" + name;
	  } else {
		  this.sprite = val.toString();
	  }

	  this.getSprite();
	  this.self = CoerceJavaToLua.coerce(this);

	  val = args.get("can_use");

	  if (val != LuaValue.NIL && val.isfunction()) {
		  this.canBeUsedCallback = (LuaFunction) val;
    }

	  registerEvents(args);
  }

  protected void registerEvents(LuaTable args) {
	  registerEvent("use", args);
  }

  private HashMap<String, Object> fields = new HashMap<>();

  public void set(String key, Object value) {
  	fields.put(key, value);
  }

  public Object get(String key) {
  	return fields.get(key);
  }
}