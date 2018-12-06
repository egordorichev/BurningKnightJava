package org.rexcellentgames.burningknight.entity.creature.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.key.BurningKey;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.Wand;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InventoryState;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.ui.UiMap;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class UiInventory extends UiEntity {
	private Inventory inventory;
	private Item currentSlot;
	public UiSlot[] slots;
	private int active = 0;
	public boolean handled;
	public int hoveredSlot = -1;
	public static UiInventory instance;

	{
		isSelectable = false;
	}

	private float mana;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;
		Player.instance.setUi(this);
	}

	@Override
	public void init() {
		if (instance != null) {
			instance.done = true;
		}

		instance = this;

		createSlots();
		hp = Player.instance.getHp();
		mana = Player.instance.getMana();
	}

	private float sx;

	public void createSlots() {
		this.slots = new UiSlot[this.inventory.getSize()];

		boolean full = Dungeon.game.getState() instanceof InventoryState;
		int my = full ? (Display.UI_HEIGHT) / 2 - 32 - 16 : 0;
		sx = full ? (Display.UI_WIDTH - 4 * 29 + 5) / 2 : 4;

		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = new UiSlot(this, i, (int) (i % 4 * 29 + sx), (int) (-29 * Math.floor(i / 4) + 4 + (i < 8 ? 16 : 0)) + my);

			if (full) {
				this.slots[i].a = 1;
			}

			if (i > 5 && i < 12) {
				Item current = this.inventory.getSlot(i);

				if (current instanceof Accessory) {
					current.setOwner(Player.instance);
					((Accessory) current).equipped = true;
					((Accessory) current).onEquip(true);

					Achievements.unlock(Achievements.EQUIP_ACCESSORY);
				}
			}
		}
	}

	public void resize(int newSize) {
		this.inventory.resize(newSize);
		Player.instance.inventorySize = newSize;
		createSlots();
	}

	@Override
	public void destroy() {
		super.destroy();
		this.done = true;

		for (int i = 6; i < this.slots.length; i++) {
			Item current = this.inventory.getSlot(i);

			if (current instanceof Accessory) {
				((Accessory) current).equipped = false;
				((Accessory) current).onUnequip(true);
			}
		}
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getActive() {
		return this.active;
	}

	private void validate(int a) {
		this.active = Math.abs(this.active % 2);
	}

	@Override
	public void update(float dt) {
		if (Dungeon.depth < 0 && Dungeon.depth != -3) {
			return;
		}

		boolean full = Dungeon.game.getState() instanceof InventoryState;

		depth = 1;

		if (Dungeon.game.getState().isPaused() || Dialog.active != null) {
			return;
		}

		this.handled = false;
		this.active = this.inventory.active;

		if (!UiMap.large) {
			if (Input.instance.wasPressed("switch")) {
				this.active = this.active == 1 ? 0 : 1;
				this.validate(Input.instance.getAmount());
				Player.instance.playSfx("menu/moving");
			} else if (Input.instance.wasPressed("scroll")) {
				this.active = this.active + Input.instance.getAmount();
				Player.instance.playSfx("menu/moving");
				this.validate(Input.instance.getAmount());
			}

			if (Input.instance.wasPressed("bomb")) {
				int count = Player.instance.getBombs();

				if (count == 0) {
					Player.instance.playSfx("item_nocash");
				} else {
					Player.instance.setBombs(count - 1);
					BombEntity e = new BombEntity(Player.instance.x + (Player.instance.w - 16) / 2, Player.instance.y + (Player.instance.h - 16) / 2).toMouseVel();
					e.owner = Player.instance;

					Player player = Player.instance;

					e.leaveSmall = player.leaveSmall;

					if (player.fireBombs) {
						e.toApply.add(new BurningBuff());
					}

					if (player.iceBombs) {
						e.toApply.add(new FreezeBuff());
					}

					if (player.poisonBombs) {
						e.toApply.add(new PoisonBuff());
					}

					if (player.manaBombs) {
						player.modifyMana(player.getManaMax());
					}

					Dungeon.area.add(e);
				}
			}

			if (Input.instance.wasPressed("active")) {
				Item item = Player.instance.getInventory().getSlot(2);

				if (item != null) {
					if (item.canBeUsed()) {
						item.use();

						if (item.getCount() == 0) {
							Player.instance.getInventory().setSlot(2, null);
						}
					} else {
						Player.instance.playSfx("item_nocash");
					}
				}
			}
		}

		if (Player.instance != null) {
			for (UiSlot slot : this.slots) {
				slot.update(dt);
			}
		}

		if (!full) {
			checkUse();
		}

		int hp = Player.instance.getHp();

		if (this.hp > hp) {
			this.hp = hp;
		} else if (this.hp < hp) {
			this.hp += dt * 10;
		}

		int mana = Player.instance.getMana();

		if (this.mana > mana) {
			this.mana = mana;
		} else if (this.mana < mana) {
			this.mana += dt * 20;
		}

		this.inventory.active = this.active;
	}

	private void checkUse() {
		if (Player.instance != null && !Player.instance.freezed && !this.handled && !Player.instance.isDead()) {
			if (this.currentSlot != null && (Input.instance.wasPressed("use"))) {

				Item slot = this.currentSlot;

				this.drop(slot);
				this.currentSlot = null;
			} else {
				Item slot = this.inventory.getSlot(this.active);

				if (slot != null) {
					if (Input.instance.wasPressed("use")) {
						if (slot.isUseable() && slot.canBeUsed() && slot.getDelay() == 0 && !Player.instance.isRolling()) {
							slot.setOwner(Player.instance);
							slot.use();
						}
					} else if (Input.instance.isDown("use") && slot.isAuto() && slot.getDelay() == 0 && !Player.instance.isRolling()) {
						slot.setOwner(Player.instance);
						slot.use();
					}
				}
			}
		}
	}

	private void drop(final Item slot) {
		slot.disableAutoPickup();

		if (slot instanceof BurningKey) {
			Player.instance.hasBkKey = false;
		}

		Tween.to(new Tween.Task(0, 0.1f) {
			@Override
			public float getValue() {
				return slot.a;
			}

			@Override
			public void setValue(float value) {
				slot.a = value;
			}

			@Override
			public void onEnd() {
				ItemHolder holder = new ItemHolder(slot);

				holder.x = (float) Math.floor(Player.instance.x) + (16 - slot.getSprite().getRegionWidth()) / 2;
				holder.y = (float) Math.floor(Player.instance.y) + (16 - slot.getSprite().getRegionHeight()) / 2;
				holder.velocityToMouse();

				for (int i = 0; i < inventory.getSize(); i++)  {
					Item it = inventory.getSlot(i);

					if (it == slot) {
						inventory.setSlot(i, null);
						break;
					}
				}

				Dungeon.area.add(holder);
				LevelSave.add(holder);
			}
		});
	}

	private static TextureRegion heart = Graphics.getTexture("ui-heart");
	private static TextureRegion half = Graphics.getTexture("ui-half_heart");
	private static TextureRegion heartIron = Graphics.getTexture("ui-heart_iron");
	private static TextureRegion halfIron = Graphics.getTexture("ui-half_heart_iron");
	private static TextureRegion heartGolden = Graphics.getTexture("ui-heart_golden");
	private static TextureRegion halfGolden = Graphics.getTexture("ui-half_heart_golden");

	private static TextureRegion heart_bg = Graphics.getTexture("ui-heart_bg");
	private static TextureRegion hurt = Graphics.getTexture("ui-hit_heart");

	private static TextureRegion star = Graphics.getTexture("ui-mana_star");
	private static TextureRegion star_bg = Graphics.getTexture("ui-star_bg");
	private static TextureRegion star_change = Graphics.getTexture("ui-mana_change");
	private static TextureRegion halfStar = Graphics.getTexture("ui-half_star");
	private static TextureRegion defense = Graphics.getTexture("ui-defense");

	private static TextureRegion ammo_texture = Graphics.getTexture("ui-ammo");
	private static TextureRegion ammo_bg = Graphics.getTexture("ui-ammo_bg");
	private static TextureRegion ammo_change = Graphics.getTexture("ui-ammo_hurt_bg");

	private static TextureRegion key = Graphics.getTexture("ui-key");
	private static TextureRegion bomb = Graphics.getTexture("ui-bomb");
	private static TextureRegion gold = Graphics.getTexture("ui-gold");

	private int lastMana;
	private float invm;

	private float inva;
	private float hp;

	private int lastAmmo;

	@Override
	public void render() {
		if ((Dungeon.depth != -3 && Dungeon.depth < 0) || Ui.hideUi) {
			return;
		}

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		boolean full = Dungeon.game.getState() instanceof InventoryState;

		if (full) {
			for (int i = this.inventory.getSize() - 1; i >= 0; i--) {
				Item item = this.inventory.getSlot(i);
				this.slots[i].render(item);
			}
		}

		boolean empty = false;

		for (int i = this.inventory.getSize() - 1; i >= 0; i--) {
			if (this.inventory.getSlot(i) == null) {
				empty = true;
				break;
			}
		}

		if (!empty) {
			Achievements.unlock(Achievements.FILL_UP_INVENTORY);
			Achievements.unlock(Achievements.UNLOCK_BACKPACK);
		}

		float y = full ? this.slots[0].y + 20 : -3;
		float x = sx;
		float xx = x;

		if (!full) {
			Item item = Player.instance.getInventory().getSlot(Player.instance.getInventory().active);
			float by = 18;

			if (item instanceof Wand) {
				by += 10;
				int mana = (int) this.mana;

				for (int i = 0; i < Player.instance.getManaMax() / 2; i++) {
					float s = 1f;
					float yy = y + 10;

					boolean change = (invm > 0.7f || (invm > 0.5f && invm % 0.2f > 0.1f));
					Graphics.render(change ? star_change : star_bg, xx + i * 11 + star.getRegionWidth() / 2 + (change ? -1 : 0),
						yy + 8 + star.getRegionHeight() / 2 + (change ? -1 : 0), 0,
						star.getRegionWidth() / 2, star.getRegionHeight() / 2, false, false, s, s);

					if (mana - 2 >= i * 2) {
						Graphics.render(star, xx + i * 11 + star.getRegionWidth() / 2, yy + 8
							+ star.getRegionHeight() / 2, 0, star.getRegionWidth() / 2, star.getRegionHeight() / 2, false, false, s, s);
					} else if (mana - 2 >= i * 2 - 1) {
						Graphics.render(halfStar, xx + i * 11 + star.getRegionWidth() / 2, yy + 8 + star.getRegionHeight() / 2, 0,
							star.getRegionWidth() / 2, star.getRegionHeight() / 2, false, false, s, s);
					}
				}
			} else if (item instanceof Gun) {
				by += 10;
				int ammo = ((Gun) item).getAmmoLeft();
				int max = ((Gun) item).ammoMax;

				if (lastAmmo < ammo) {
					inva = 1.0f;
				}

				lastAmmo = ammo;

				if (inva > 0) {
					inva -= Gdx.graphics.getDeltaTime();
				}

				for (int i = 0; i < max; i++) {
					float s = 1f;
					float yy = y + 10;

					boolean change = (inva > 0.7f || (inva > 0.5f && inva % 0.2f > 0.1f));
					Graphics.render(change ? ammo_change : ammo_bg, xx + i * 3 + ammo_texture.getRegionWidth() / 2,
						yy + 8 + ammo_texture.getRegionHeight() / 2, 0,
						ammo_texture.getRegionWidth() / 2, ammo_texture.getRegionHeight() / 2, false, false, s, s);

					if (i < ammo) {
						Graphics.render(ammo_texture, xx + i * 3 + ammo_texture.getRegionWidth() / 2 + 1, yy + 9
								+ ammo_texture.getRegionHeight() / 2, 0, ammo_texture.getRegionWidth() / 2,
							ammo_texture.getRegionHeight() / 2, false, false, s, s);
					}
				}
			}


			Graphics.render(bomb, 4, by);
			Graphics.render(key, 4, by + 12);
			Graphics.render(gold, 4, by + 24);

			Graphics.print(Player.instance.getBombs() + "", Graphics.small, 16, by);
			Graphics.print(Player.instance.getKeys() + "", Graphics.small, 16, by + 12);
			Graphics.print(Player.instance.getMoney() + "", Graphics.small, 16, by + 24);
		}

		if (lastMana > mana) {
			invm = 1.0f;
		}

		if (invm > 0) {
			invm -= Gdx.graphics.getDeltaTime();
		}

		lastMana = (int) mana;

		int hp = (int) Math.floor(this.hp);
		int iron = Player.instance.getIronHearts();
		int golden = Player.instance.getGoldenHearts();

		float invt = Player.instance.getInvt();
		int max = Player.instance.getHpMax();
		int i;

		for (i = 0; i < Math.ceil(((float) max) / 2); i++) {
			float s = 1f;
			float yy = (float) ((hp <= 2 && hp - 2 >= i * 2 - 1) ? Math.cos(((float)i) % 2 / 2 + Dungeon.time * 15) * 2.5f : 0) + y;

			if (hp - 2 == i * 2 || hp - 2 == i * 2 - 1) {
				s = (float) (1f + Math.abs(Math.cos(Dungeon.time * 3) / 2.5f));
			}

			Graphics.render((invt > 0.7f || (invt > 0.5f && invt % 0.2f > 0.1f)) ? hurt : heart_bg, xx + i * 11 + 1 + heart.getRegionWidth() / 2,
				yy + 9 + heart.getRegionHeight() / 2, 0,
				heart_bg.getRegionWidth() / 2, heart_bg.getRegionHeight() / 2, false, false, s, s);

			if (hp - 2 >= i * 2) {
				Graphics.render(heart, xx + i * 11 + 1 + heart.getRegionWidth() / 2, yy + 9
					+ heart.getRegionHeight() / 2, 0, heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, s, s);
			} else if (hp - 2 >= i * 2 - 1) {
				Graphics.render(half, xx + i * 11 + 1 + heart.getRegionWidth() / 2, yy + 9 + heart.getRegionHeight() / 2, 0,
					heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, s, s);
			}
		}

		for (; i < Math.ceil(((float) max) / 2) + Math.ceil(((float) iron) / 2); i++) {
			Graphics.render((invt > 0.7f || (invt > 0.5f && invt % 0.2f > 0.1f)) ? hurt : heart_bg, xx + i * 11 + 1 + heart.getRegionWidth() / 2,
				y + 9 + heart.getRegionHeight() / 2, 0,
				heart_bg.getRegionWidth() / 2, heart_bg.getRegionHeight() / 2, false, false, 1, 1);

			if (max + iron - 2 >= i * 2) {
				Graphics.render(heartIron, xx + i * 11 + 1 + heart.getRegionWidth() / 2, y + 9
					+ heart.getRegionHeight() / 2, 0, heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, 1, 1);
			} else if (max + iron - 2 >= i * 2 - 1) {
				Graphics.render(halfIron, xx + i * 11 + 1 + heart.getRegionWidth() / 2, y + 9 + heart.getRegionHeight() / 2, 0,
					heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, 1, 1);
			}
		}

		for (; i < Math.ceil(((float) max) / 2) + Math.ceil(((float) iron) / 2) + Math.ceil(((float) golden) / 2); i++) {
			Graphics.render((invt > 0.7f || (invt > 0.5f && invt % 0.2f > 0.1f)) ? hurt : heart_bg, xx + i * 11 + 1 + heart.getRegionWidth() / 2,
				y + 9 + heart.getRegionHeight() / 2, 0,
				heart_bg.getRegionWidth() / 2, heart_bg.getRegionHeight() / 2, false, false, 1, 1);

			if (max + iron + golden - 2 >= i * 2) {
				Graphics.render(heartGolden, xx + i * 11 + 1 + heart.getRegionWidth() / 2, y + 9
					+ heart.getRegionHeight() / 2, 0, heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, 1, 1);
			} else if (max + iron + golden - 2 >= i * 2 - 1) {
				Graphics.render(halfGolden, xx + i * 11 + 1 + heart.getRegionWidth() / 2, y + 9 + heart.getRegionHeight() / 2, 0,
					heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, 1, 1);
			}
		}

		Item item = Player.instance.getInventory().getSlot(2);

		if (item != null) {
			TextureRegion region = item.getSprite();
			if (item.getDelay() > 0) {
				int w = region.getRegionWidth();
				Graphics.batch.setColor(0.1f, 0.1f, 0.1f, 1);
				Graphics.render(region, 4, Display.UI_HEIGHT - region.getRegionHeight() - 4);
				Graphics.batch.setColor(1, 1, 1, 1);

				region.setRegionWidth((int) (w * (1f - item.getDelay() / item.getUseTime())));
				Graphics.render(region, 4, Display.UI_HEIGHT - region.getRegionHeight() - 4);
				region.setRegionWidth(w);
			} else {
				Graphics.render(region, 4, Display.UI_HEIGHT - region.getRegionHeight() - 4);
			}
		}

		this.renderCurrentSlot();

		if (full) {
			float dy = 8 + y;
			float dx = x + 4 * 29 - 5 - defense.getRegionWidth();

			Graphics.render(defense, dx, dy);
			int d = Player.instance.getDefense();
			String s = String.valueOf(d);

			Graphics.layout.setText(Graphics.small, s);
			Graphics.print(s, Graphics.small, dx + (defense.getRegionWidth() - Graphics.layout.width) / 2, dy + (defense.getRegionHeight() - Graphics.layout.height) / 2 - 1);
		}
	}

	public UiBuff hoveredBuff;

	public boolean hasEquipped(Class<? extends Equippable> type) {
		return getEquipped(type) != null;
	}

	public Equippable getEquipped(Class<? extends Equippable> type) {
		for (int i = 6; i < this.inventory.getSize(); i++) {
			Item it = this.inventory.getSlot(i);

			if (type.isInstance(it)) {
				return (Equippable) it;
			}
		}

		return null;
	}

	public void renderCurrentSlot() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (this.currentSlot != null) {
			float x = Input.instance.uiMouse.x;
			float y = Input.instance.uiMouse.y;

			if (Input.instance.activeController != null) {
				UiSlot slot = this.slots[this.active];

				x = slot.x + 24;
				y = slot.y + 24;
			}

			int count = this.currentSlot.getCount();

			if (count > 0) {
				TextureRegion sprite = this.currentSlot.getSprite();
				Graphics.render(sprite,
					 x + 12 - sprite.getRegionWidth() / 2,
					y - 8);


				if (count > 1) {
					Graphics.small.draw(Graphics.batch, String.valueOf(count), x + 16, y - 4);
				}
			}
		} else if (this.hoveredSlot != -1) {
			Item item = this.inventory.getSlot(this.hoveredSlot);

			if (item != null) {
				String info = item.buildInfo().toString();
				Graphics.layout.setText(Graphics.small, info);

				float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

				Graphics.small.setColor(c, c, c, 1);
				Graphics.print(info, Graphics.small, this.slots[0].x, this.slots[0].y + 20 + Graphics.layout.height + 14 + 8);
				Graphics.small.setColor(1, 1, 1, 1);

				this.hoveredSlot = -1;
			}
		}
	}

	public void renderOnPlayer(Player player, float of) {
		if (player == null || player.isDead()) {
			return;
		}

		Item slot = this.inventory.getSlot(this.active);

		if (slot != null) {
			slot.render(player.x, player.y + of, player.w, player.h, player.isFlipped());
		}
	}

	public void renderBeforePlayer(Player player, float of) {
		Item slot = this.inventory.getSlot(this.active);

		if (slot != null) {
			slot.beforeRender(player.x, player.y + of, player.w, player.h, player.isFlipped());
		}
	}

	public void setCurrentSlot(Item currentSlot) {
		this.currentSlot = currentSlot;
	}

	public Item getCurrentSlot() {
		return this.currentSlot;
	}
}