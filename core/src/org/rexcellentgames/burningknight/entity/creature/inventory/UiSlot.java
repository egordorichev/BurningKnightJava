package org.rexcellentgames.burningknight.entity.creature.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.CurseFx;
import org.rexcellentgames.burningknight.entity.fx.UpgradeFx;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.Hat;
import org.rexcellentgames.burningknight.entity.item.consumable.scroll.ScrollOfUpgrade;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class UiSlot {
	private static TextureRegion slot = Graphics.getTexture("ui-inventory_slot");
	private static TextureRegion cursedSlot = Graphics.getTexture("ui-cursed_slot");
	private static TextureRegion selected = Graphics.getTexture("ui-selected_slot");

	private static TextureRegion armorBg = Graphics.getTexture("ui-hat_bg");
	private static TextureRegion coinBg = Graphics.getTexture("ui-coin_bg");
	private static TextureRegion equipBg = Graphics.getTexture("ui-ring_bg");

	public int x;
	public float y;
	protected int id;
	private boolean hovered = false;
	private UiInventory inventory;
	private float scale = 1f;
	private boolean active;
	protected float r = 1f;
	protected float g = 1f;
	protected float b = 1f;
	private float rr = 1f;
	private float rg = 1f;
	private float rb = 1f;

	public UiSlot(UiInventory inventory, int id, int x, int y) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.inventory = inventory;
	}

	private Tween.Task last;

	public void update(float dt) {
		Item item = this.inventory.getInventory().getSlot(this.id);

		this.r += (this.rr - this.r) * dt * 10;
		this.g += (this.rg - this.g) * dt * 10;
		this.b += (this.rb - this.b) * dt * 10;

		if (item != null) {
			item.update(dt);

			if (item.getCount() == 0) {
				this.inventory.getInventory().setSlot(this.id, null);
			}
		}

		if (!this.inventory.isOpen() && this.id >= 6) {
			return;
		}

		if (this.inventory.getActive() == this.id && !this.active) {
			this.active = true;

			Tween.remove(this.last);
			this.last = Tween.to(new Tween.Task(1.2f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});

			Audio.playSfx("menu/moving");
		} else if (this.inventory.getActive() != this.id && this.active) {
			this.active = false;


			Tween.remove(this.last);
			this.last = Tween.to(new Tween.Task(1f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});
		}

		if (this.id > 5 && !this.inventory.isOpen()) {
			return;
		}

		boolean h = this.hovered;
		this.hovered = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, this.x, (int) this.y, 24, 24);

		if (Ui.upgradeMosue) {
			if (this.hovered || h) {
				if (item != null && item.canBeUpgraded()) {
					if (this.hovered) {
						Ui.move = true;
					} else {
						Ui.move = false;
					}
				}
			}
		}

		if (this.hovered && !h) {
			Tween.to(new Tween.Task(this.inventory.getActive() == this.id ? 1.3f : 1.1f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});

			Audio.playSfx("menu/moving");
		} else if (!this.hovered && h) {
			Tween.to(new Tween.Task(this.inventory.getActive() == this.id ? 1.2f : 1f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});
		}

		if (this.hovered) {
			this.inventory.hoveredSlot = this.id;
			this.inventory.handled = true;

			if (Input.instance.wasPressed("use")) {
				leftClick();
			} else if (Input.instance.wasPressed("second_use")) {
				rightClick();
			}
		}
	}

	public void tweenClick() {
		Audio.playSfx("menu/select");

		Tween.to(new Tween.Task(this.inventory.getActive() == this.id ? 1.5f : 1.2f, 0.05f) {
			@Override
			public float getValue() {
				return scale;
			}

			@Override
			public void setValue(float value) {
				scale = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				Tween.to(new Tween.Task(inventory.getActive() == id ? 1.3f : 1.1f, 0.1f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return scale;
					}

					@Override
					public void setValue(float value) {
						scale = value;
					}
				});
			}
		});
	}

	public void leftClick() {
		Item self = this.inventory.getInventory().getSlot(this.id);

		if (self instanceof Accessory && this.id < 6 && Input.instance.isDown("shift")) {
			if (self.canBeUsed()) {
				self.use();
			}

			return;
		}

		Item current = this.inventory.getCurrentSlot();

		Item active = this.inventory.getInventory().getSlot(this.inventory.getActive());

		if (active instanceof ScrollOfUpgrade && self != null && (self.canBeUpgraded() && self.getLevel() < self.getMaxLevel())) {
			for (int i = 0; i < 10; i++) {
				UpgradeFx fx = new UpgradeFx();

				fx.x = Random.newFloat(16) - 8 + Input.instance.uiMouse.x;
				fx.y = Random.newFloat(16) - 8 + Input.instance.uiMouse.y;

				Dungeon.ui.add(fx);
			}

			if (active.isCursed()) {
				for (int i = 0; i < 10; i++) {
					CurseFx fx = new CurseFx();

					fx.x = Random.newFloat(16) - 8 + Input.instance.uiMouse.x;
					fx.y = Random.newFloat(16) - 8 + Input.instance.uiMouse.y;

					Dungeon.ui.add(fx);
				}

				((ScrollOfUpgrade) active).wasCursed = true;
			}

			active.use();
			self.upgrade();

			return;
		}

		if (current != null && self != null && current.getClass() == self.getClass() && self.isStackable()) {
			current.setCount(current.getCount() + self.getCount());
			this.inventory.getInventory().setSlot(this.id, current);
			this.inventory.setCurrentSlot(null);
		} else if (canAccept(this.id, current) || current == null) {
			if (this.id > 5 && this.id < 12 && self != null && self.isCursed()) {
				Audio.playSfx("item_nocash");
			} else {
				if (this.id > 5 && current != null) {
					for (int i = 6; i < 12; i++) {
						Item sl = this.inventory.getInventory().getSlot(i);

						if (sl != null && sl.getClass().isInstance(current)) {
							Audio.playSfx("item_nocash");
							return;
						}
					}
				}

				this.inventory.setCurrentSlot(self);
				this.inventory.getInventory().setSlot(this.id, current);

				if (this.id > 5 && this.id < 12) {
					if (current != null && current.isCursed()) {
						for (int i = 0; i < 10; i++) {
							CurseFx fx = new CurseFx();

							fx.x = Random.newFloat(16) - 8 + Input.instance.uiMouse.x;
							fx.y = Random.newFloat(16) - 8 + Input.instance.uiMouse.y;

							Dungeon.ui.add(fx);
						}

						Audio.playSfx("curse_lamp_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));
					}

					if (self instanceof Accessory) {
						((Accessory) self).equipped = false;
						((Accessory) self).onUnequip(false);
					}

					if (current instanceof Accessory) {
						current.setOwner(Player.instance);
						((Accessory) current).equipped = true;
						((Accessory) current).onEquip(false);
						Achievements.unlock(Achievements.EQUIP_ACCESSORY);
					}
				}
			}
		} else {
			this.r = 1f;
			this.g = 0;
			this.b = 0;
		}

		tweenClick();
	}

	public void rightClick() {
		Item self = this.inventory.getInventory().getSlot(this.id);
		Item current = this.inventory.getCurrentSlot();

		if (self == null || !self.isStackable()) {
			return;
		}

		if (current != null && self.getClass() != current.getClass()) {
			return;
		}

		if (current == null) {
			try {
				current = self.getClass().newInstance();
				current.setCount(0);
				current.setOwner(Player.instance);

				this.inventory.setCurrentSlot(current);
			} catch (Exception e) {
				Dungeon.reportException(e);

				return;
			}
		}

		if (self.getCount() == 1) {
			this.inventory.getInventory().setSlot(this.id, null);
		}

		current.setCount(current.getCount() + 1);
		current.setOwner(Player.instance);
		self.setCount(self.getCount() - 1);

		tweenClick();
	}

	public static boolean canAccept(int id, Item item) {
		if (id == 6) {
			return item instanceof Hat;
		} else if (id == 11) {
			return item instanceof Gold;
		} else if (id > 6 && id < 12) {
			return item instanceof Equippable;
		}

		return true;
	}

	private boolean acted;
	public float a = 0.7f;

	public void render(Item item) {
		boolean h = this.inventory.getActive() == this.id;
		boolean upgrade = this.inventory.getInventory().getSlot(this.inventory.getActive()) instanceof ScrollOfUpgrade;
		boolean cursed = item != null && item.isIdentified() && item.isCursed();

		if (h) {
			this.rr = 0.6f;
			this.rg = 0.6f;
			this.rb = 0.6f;
		} else if (this.hovered) {
			if (Input.instance.isDown("use") || Input.instance.isDown("second_use")) {
				this.rr = 0.3f;
				this.rg = 0.3f;
				this.rb = 0.3f;
			} else {
				this.rr = 0.5f;
				this.rg = 0.5f;
				this.rb = 0.5f;
			}
		} else {
			this.rr = 1f;
			this.rg = 1f;
			this.rb = 1f;
		}

		float an = 0;//(float) (Math.cos(Dungeon.time) * 10f);

		Graphics.batch.setColor(r, g, b, h ? 1 : a);

		TextureRegion reg = h ? selected : (cursed ? cursedSlot : slot);
		Graphics.render(reg, this.x + slot.getRegionWidth() / 2,
			this.y + slot.getRegionHeight() / 2, an, reg.getRegionWidth() / 2, reg.getRegionHeight() / 2, false, false, this.scale, this.scale);

		if (item == null) {
			if (this.id == 6) {
				Graphics.render(armorBg, this.x + 12 - armorBg.getRegionWidth() / 2,
					this.y + 12 - armorBg.getRegionHeight() / 2);
			} else if (this.id > 6 && this.id < 11) {
				Graphics.render(equipBg, this.x + 12 - equipBg.getRegionWidth() / 2,
					this.y + 12 - equipBg.getRegionHeight() / 2);
			} else if (this.id == 11) {
				Graphics.render(coinBg, this.x + 12 - coinBg.getRegionWidth() / 2,
					this.y + 12 - coinBg.getRegionHeight() / 2);
			}
		}

		if (item != null && item.getDelay() != 0) {
			float delay = item.getDelay();
			float maxDelay = item.getUseTime();

			int w = (int) (Math.min(1, (delay / maxDelay)) * 24);
			Graphics.batch.setColor(0.3f, 0.3f, 0.3f, 1);
			TextureRegion region = new TextureRegion(reg);
			region.setRegionWidth(w);
			Graphics.render(region, this.x + slot.getRegionWidth() / 2,
				this.y + slot.getRegionHeight() / 2, an, reg.getRegionWidth() / 2,
				region.getRegionHeight() / 2, false, false, this.scale, this.scale);
			Graphics.batch.setColor(1, 1, 1, a);

			if (!this.acted) {
				Tween.to(new Tween.Task(this.inventory.getActive() == this.id ? 1.5f : 1.2f, 0.05f) {
					@Override
					public float getValue() {
						return scale;
					}

					@Override
					public void setValue(float value) {
						scale = value;
					}

					@Override
					public void onEnd() {
						super.onEnd();

						Tween.to(new Tween.Task(inventory.getActive() == id ? 1.3f : 1.1f, 0.1f, Tween.Type.BACK_OUT) {
							@Override
							public float getValue() {
								return scale;
							}

							@Override
							public void setValue(float value) {
								scale = value;
							}
						});
					}
				});

				this.acted = true;
			}
		} else {
			this.acted = false;
		}

		Graphics.batch.setColor(1, 1, 1, a);

		if (item != null) {
			float gray = 1;

			if (upgrade && (!item.canBeUpgraded() || item.getLevel() >= item.getMaxLevel()) && !(item instanceof ScrollOfUpgrade)) {
				gray = 0.6f;
			}

			TextureRegion sprite = item.getSprite();
			int count = item.getValue();
			boolean enable = !item.disableBlink();

			if (item instanceof WeaponBase) {
				((WeaponBase) item).renderAt(x + slot.getRegionWidth() / 2,
					y + slot.getRegionHeight() / 2, 0,sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale, item.a, gray);
			} else {
				if (enable) {
					Graphics.batch.end();
					WeaponBase.shader.begin();
					WeaponBase.shader.setUniformf("a", item.a);
					WeaponBase.shader.setUniformf("gray", gray);
					WeaponBase.shader.setUniformf("time", Dungeon.time);
					WeaponBase.shader.end();
					Graphics.batch.setShader(WeaponBase.shader);
					Graphics.batch.begin();
				} else {
					Graphics.batch.setColor(gray, gray, gray, item.a);
				}

				Graphics.render(sprite, x + slot.getRegionWidth() / 2,
					y + slot.getRegionHeight() / 2, 0, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale);

				if (enable) {
					Graphics.batch.end();
					Graphics.batch.setShader(null);
					Graphics.batch.begin();
				} else {
					Graphics.batch.setColor(1, 1, 1, 1);
				}

			}

			float w = 24 * this.scale;
			float x = this.x + 12;
			float y = this.y + 12;

			if (count != 1 || item instanceof Gun) {
				Graphics.small.setColor(1, 1, 1, item.a);
				Graphics.print(String.valueOf(count), Graphics.small, x - w / 2 + 3, y - w / 2 + 3);
				Graphics.small.setColor(1, 1, 1, 1);
			}

			int level = item.getLevel();

			if (level != 1) {
				level -= 1;
				Graphics.small.setColor(level < 1 ? 1 : 0, level < 0 ? 0 : 1, 0, item.a);
				String text = level > 0 ? "+" + level : "" + level;
				Graphics.layout.setText(Graphics.small, text);
				Graphics.print(text, Graphics.small, x + w / 2 - 3 - Graphics.layout.width, y - w / 2 + 3);
				Graphics.small.setColor(1, 1, 1, 1);
			}
		}

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public void renderItem(Item item, float x, float y, float a) {
		if (item != null) {
			TextureRegion sprite = item.getSprite();
			int count = item.getValue();
			boolean enable = !item.disableBlink();

			if (item instanceof WeaponBase) {
				((WeaponBase) item).renderAt(x + slot.getRegionWidth() / 2,
					y + slot.getRegionHeight() / 2, 0,sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale, item.a, 1);
			} else {
				if (enable) {
					Graphics.batch.end();
					WeaponBase.shader.begin();
					WeaponBase.shader.setUniformf("a", a);
					WeaponBase.shader.setUniformf("time", Dungeon.time);
					WeaponBase.shader.end();
					Graphics.batch.setShader(WeaponBase.shader);
					Graphics.batch.begin();
				} else {
					Graphics.batch.setColor(1, 1, 1, a);
				}

				Graphics.render(sprite, x + slot.getRegionWidth() / 2,
					y + slot.getRegionHeight() / 2, 0, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale);

				if (enable) {
					Graphics.batch.end();
					Graphics.batch.setShader(null);
					Graphics.batch.begin();
				} else {
					Graphics.batch.setColor(1, 1, 1, 1);
				}
			}

			if (count != 1 || item instanceof Gun) {
				Graphics.small.setColor(1, 1, 1, a);
				Graphics.print(String.valueOf(count), Graphics.small, x + 3, y + 3);
				Graphics.small.setColor(1, 1, 1, 1);
			}
		}
	}
}