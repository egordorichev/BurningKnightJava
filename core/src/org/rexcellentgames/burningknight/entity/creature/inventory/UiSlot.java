package org.rexcellentgames.burningknight.entity.creature.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.Equipable;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.Hat;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Tween;

public class UiSlot {
	private static TextureRegion slot = Graphics.getTexture("ui-inventory_slot");
	private static TextureRegion slotBig = Graphics.getTexture("ui-inventory_slot_large");

	private static TextureRegion armorBg = Graphics.getTexture("ui-hat_bg");
	private static TextureRegion coinBg = Graphics.getTexture("ui-coin_bg");
	private static TextureRegion equipBg = Graphics.getTexture("ui-ring_bg");

	public int x;
	public float y;
	private int id;
	private boolean hovered = false;
	private UiInventory inventory;
	private float scale = 1f;
	private boolean active;
	private float r = 1f;
	private float g = 1f;
	private float b = 1f;
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

		this.r += (this.rr - this.r) * dt * 20;
		this.g += (this.rg - this.g) * dt * 20;
		this.b += (this.rb - this.b) * dt * 20;

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

		boolean h = this.hovered;
		this.hovered = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, this.x, (int) this.y, 24, 24);

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

			if (Input.instance.wasPressed("mouse0") || Input.instance.wasPressed("mouse1")) {
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

			if (Input.instance.wasPressed("mouse0")) {
				Item current = this.inventory.getCurrentSlot();
				Item self = this.inventory.getInventory().getSlot(this.id);

				if (current != null && self != null && current.getClass() == self.getClass() && self.isStackable()) {
					current.setCount(current.getCount() + self.getCount());
					this.inventory.getInventory().setSlot(this.id, current);
					this.inventory.setCurrentSlot(null);
				} else if (canAccept(this.id, current) || current == null) {
					if (this.id > 5 && current != null) {
						for (int i = 5; i < this.inventory.getInventory().getSize(); i++) {
							Item sl = this.inventory.getInventory().getSlot(i);

							if (sl != null && sl.getClass().isInstance(current)) {
								return;
							}
						}
					}

					this.inventory.setCurrentSlot(self);
					this.inventory.getInventory().setSlot(this.id, current);

					if (this.id > 5) {
						if (self instanceof Accessory) {
							((Accessory) self).onUnequip();
						}

						if (current instanceof Accessory) {
							current.setOwner(Player.instance);
							((Accessory) current).onEquip();
						}
					}
				}
			} else if (Input.instance.wasPressed("mouse1")) {
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
			}
		}
	}

	public static boolean canAccept(int id, Item item) {
		if (id == 6) {
			return item instanceof Hat;
		} else if (id == 11) {
			return item instanceof Gold;
		} else if (id > 6 && id < 12) {
			return item instanceof Equipable;
		}

		return true;
	}

	private boolean acted;
	public float a = 0.7f;

	public void render(Item item) {
		TextureRegion reg = this.slot;
		boolean h = this.inventory.getActive() == this.id;

		if (h) {
			/*if (Input.instance.isDown("mouse0") || Input.instance.isDown("mouse1")) {
				this.rr = 1f;
				this.rg = 1f;
				this.rb = 1f;
			} else {*/
				this.rr = 0.6f;
				this.rg = 0.6f;
				this.rb = 0.6f;
			// }
		} else if (this.hovered) {
			if (Input.instance.isDown("mouse0") || Input.instance.isDown("mouse1")) {
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

		Graphics.batch.setColor(this.r, this.g, this.b, a);

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
			Graphics.batch.setColor(0.3f, 0.3f, 0.3f, a);
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
			TextureRegion sprite = item.getSprite();
			int count = item.getValue();

			/*if (item instanceof Lamp && ((Lamp) item).getRadius() > 0) {
				Graphics.batch.end();

				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Graphics.shape.setProjectionMatrix(Camera.ui.combined);
				Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

				Graphics.shape.setColor(1, 0.5f, 0, 0.6f);
				Graphics.shape.circle(this.x + 12, this.y + 12, (float) (10f + Math.cos(Dungeon.time * 3) * Math.sin(Dungeon.time * 4)) * ((Lamp) item).getRadius());

				Graphics.shape.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				Graphics.batch.begin();
			}*/

			if (item instanceof WeaponBase) {
				((WeaponBase) item).renderAt(this.x + slot.getRegionWidth() / 2,
					this.y + slot.getRegionHeight() / 2, 0,sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale, item.a);
			} else {
				Graphics.batch.end();
				WeaponBase.shader.begin();
				WeaponBase.shader.setUniformf("a", item.a);
				WeaponBase.shader.setUniformf("time", Dungeon.time);
				WeaponBase.shader.end();
				Graphics.batch.setShader(WeaponBase.shader);
				Graphics.batch.begin();
				Graphics.render(sprite, this.x + slot.getRegionWidth() / 2,
					this.y + slot.getRegionHeight() / 2, an, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale);

				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			if (count != 1 || item instanceof Gun) {
				Graphics.small.setColor(1, 1, 1, item.a);
				Graphics.print(String.valueOf(count), Graphics.small, this.x + 3, this.y + 3);
				Graphics.small.setColor(1, 1, 1, 1);
			}
		}

		Graphics.batch.setColor(1, 1, 1, 1);
	}
}