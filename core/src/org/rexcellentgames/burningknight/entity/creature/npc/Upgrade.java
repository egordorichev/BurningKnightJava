package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.Confetti;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Upgrade extends ItemHolder {
	public static ArrayList<Upgrade> all = new ArrayList<>();
	public static boolean updateEvent;
	public static Upgrade activeUpgrade;

	public Type type = Type.PERMANENT;

	private Item item;
	private String str;
	protected float z;
	private Body body;
	private int price = 0;
	private String costStr = "";
	private int costW = 0;
	private int nameW = 0;
	public String idd = "";

	private boolean hidden;

	public enum Type {
		CONSUMABLE,
		WEAPON,
		ACCESSORY,
		PET,
		PERMANENT,
		NONE,
		DECOR
	}

	public static int getTypeId(Type type) {
		switch (type) {
			case CONSUMABLE: return 0;
			case WEAPON: return 1;
			case ACCESSORY: return 2;
			case PET: return 3;
			case PERMANENT: return 4;
			case NONE: default: return 5;
			case DECOR: return 6;
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		type = Type.values()[reader.readByte()];

		try {
			this.idd = reader.readString();
			this.str = reader.readString();

			if (str != null) {
				ItemRegistry.Pair pair = ItemRegistry.items.get(this.str);
				pair.busy = true;

				this.item = (Item) pair.type.newInstance();
				this.price = pair.cost;

				this.setupInfo();
				this.createBody();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		super.init();
		this.t = Random.newFloat(10f);
		all.add(this);
	}

	protected Item generateItem() {
		for (ItemRegistry.Pair pair : ItemRegistry.items.values()) {
			if (!pair.busy && pair.pool == this.type && !pair.unlocked(pair.id)) {
				this.str = pair.id;
				pair.busy = true;

				try {
					this.price = pair.cost;
					this.setupInfo();

					return (Item) pair.type.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private void setupInfo() {
		if (this.item == null) {
			this.costStr = "";
			return;
		}

		this.costStr = "" + this.price;

		Graphics.layout.setText(Graphics.small, this.costStr);
		this.costW = (int) Graphics.layout.width;

		Graphics.layout.setText(Graphics.medium, this.item.getName());
		this.nameW = (int) Graphics.layout.width;
	}

	private boolean checked;

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		this.checkItem();

		writer.writeByte((byte) getTypeId(type));
		writer.writeString(this.idd);
		writer.writeString(this.str);
	}

	@Override
	public void update(float dt) {
		if (!checked) {
			checked = true;

			for (Trader trader : Trader.all) {
				if (trader.id.equals(this.idd) && trader.saved) {
					this.hidden = false;
					return;
				}
			}

			this.hidden = true;
		}

		if (this.hidden) {
			return;
		}

		if (colliding && activeUpgrade == null && Npc.active == null) {
			activeUpgrade = this;
		} else if (!colliding && activeUpgrade == this) {
			activeUpgrade = null;
		}

		this.checkItem();

		this.al = MathUtils.clamp(0f, 1f, this.al + ((activeUpgrade == this ? 1 : 0) - this.al) * dt * 10f);
		this.z += (Math.cos((this.t * 2.4f)) / 10f * dt * 60.0);
		this.z = MathUtils.clamp(0f, 5f, this.z);

		if (this.body != null) {
			this.body.setTransform(this.x + 8 - this.w / 2, this.y + this.z + 8 - this.h / 2, 0f);
		}

		if (activeUpgrade == this && Input.instance.wasPressed("interact")) {
			int count = GlobalSave.getInt("num_coins");

			if (count < this.price) {
				Audio.playSfx("item_nocash");
				Camera.shake(3f);
			} else {
				count -= this.price;
				GlobalSave.put("num_coins", count);
				this.playSfx("item_purchase");

				updateEvent = true;

				Achievements.unlock("SHOP_" + this.str.toUpperCase());

				for (int i = 0; i < 9; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x + this.w / 2;
					fx.y = this.y + this.h / 2;

					Dungeon.area.add(fx);
				}

				for (int i = 0; i < 14; i++) {
					Confetti c = new Confetti();

					c.x = this.x + Random.newFloat(this.w);
					c.y = this.y + Random.newFloat(this.h);
					c.vel.x = Random.newFloat(-30f, 30f);
					c.vel.y = Random.newFloat(30f, 40f);

					Dungeon.area.add(c);
				}

				this.item = null;
				this.str = null;
				this.body = World.removeBody(this.body);
			}
		}

		super.update(dt);
	}

	private void checkItem() {
		if (this.item == null) {
			this.item = this.generateItem();

			if (this.item == null) {
				this.hidden = true;
			} else {
				this.setupInfo();
				this.createBody();
			}
		}
	}

	private void createBody() {
		this.w = this.item.getSprite().getRegionWidth();
		this.h = this.item.getSprite().getRegionHeight();

		this.body = World.removeBody(this.body);
		this.body = World.createSimpleBody(this, 0f, 0f,
			this.w, this.h, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0f);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
		all.remove(this);
	}

	private float al;

	@Override
	public void renderShadow() {
		if (!this.hidden) {
			super.renderShadow();
		}
	}

	public void renderSigns() {
		if (hidden || item == null) {
			return;
		}

		if (this.al > 0.05f && !Ui.hideUi) {
			Graphics.medium.setColor(1f, 1f, 1f, this.al);
			Graphics.print(this.item.getName(), Graphics.medium, this.x + 8 - nameW / 2, this.y + this.h + 8);
			Graphics.medium.setColor(1f, 1f, 1f, 1f);
		}
	}

	@Override
	public void render() {
		if (hidden || item == null) {
			return;
		}

		TextureRegion sprite = this.item.getSprite();

		float a = (float) (Math.cos((this.t * 3f)) * 8f);
		float sy = (float) (1f + Math.sin((this.t * 2f)) / 10f);

		Graphics.batch.end();

		if (this.al > 0.05f && !Ui.hideUi) {
			Mob.shader.begin();
			Mob.shader.setUniformf("u_color", new Vector3(1f, 1f, 1f));
			Mob.shader.setUniformf("u_a", this.al);
			Mob.shader.end();
			Graphics.batch.setShader(Mob.shader);
			Graphics.batch.begin();

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -1; yy < 2; yy++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(sprite, this.x + 8 + xx,
							this.y + z + 8 + yy, a, w / 2, h / 2, false, false, 1f, sy);
					}
				}
			}

			Graphics.batch.end();
			Graphics.batch.setShader(null);
		}

		Graphics.batch.begin();
		Graphics.print(this.costStr, Graphics.small,
			this.x + 8 - this.costW / 2, this.y - 8);
		Graphics.batch.end();

		WeaponBase.shader.begin();
		WeaponBase.shader.setUniformf("a", 1f);
		WeaponBase.shader.setUniformf("gray", 1f);
		WeaponBase.shader.setUniformf("time", Dungeon.time + this.t);
		WeaponBase.shader.end();
		Graphics.batch.setShader(WeaponBase.shader);
		Graphics.batch.begin();

		Graphics.render(sprite, this.x + 8,
			this.y + z + 8, a, w / 2, h / 2, false, false, 1f, sy);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	private boolean colliding;

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			colliding = true;
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			colliding = false;
		}
	}
}