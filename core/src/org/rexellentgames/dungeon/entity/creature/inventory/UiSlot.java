package org.rexellentgames.dungeon.entity.creature.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.item.accessory.Accessory;
import org.rexellentgames.dungeon.entity.item.accessory.equipable.Equipable;
import org.rexellentgames.dungeon.entity.item.accessory.hat.Hat;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.Tween;

public class UiSlot {
	private static TextureRegion slot;
	private static TextureRegion armorBg = Graphics.getTexture("ui (armor_bg)");
	private static TextureRegion coinBg = Graphics.getTexture("ui (gold_bg)");
	private static TextureRegion equipBg = Graphics.getTexture("ui (equip_bg)");

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

		if (slot == null) {
			slot = Graphics.getTexture("ui (inventory slot)");
		}
	}

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

			Tween.to(new Tween.Task(1.2f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}
			});
		} else if (this.inventory.getActive() != this.id && this.active) {
			this.active = false;

			Tween.to(new Tween.Task(1f, 0.1f) {
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
				} else if (this.canAccept(current) || current == null) {
					this.inventory.setCurrentSlot(self);
					this.inventory.getInventory().setSlot(this.id, current);

					if (this.id > 5) {
						if (current != null && current instanceof Accessory) {
							((Accessory) current).onEquip();
						}

						if (self != null && self instanceof Accessory) {
							((Accessory) self).onUnequip();
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

	public boolean canAccept(Item item) {
		if (this.id == 6) {
			return item instanceof Hat;
		} else if (this.id == 11) {
			return item instanceof Gold;
		} else if (this.id > 6) {
			return item instanceof Equipable;
		}

		return true;
	}

	private boolean acted;

	public void render(Item item) {
		if (this.inventory.getActive() == this.id) {
			this.rr = 0.6f;
			this.rg = 0.6f;
			this.rb = 0.6f;
		} else if (this.hovered) {
			if (Input.instance.isDown("mouse0") || Input.instance.isDown("mouse1")) {
				this.rr = 0.4f;
				this.rg = 0.4f;
				this.rb = 0.4f;
			} else {
				this.rr = 0.8f;
				this.rg = 0.8f;
				this.rb = 0.8f;
			}
		} else {
			this.rr = 1f;
			this.rg = 1f;
			this.rb = 1f;
		}

		float a = 0;//(float) (Math.cos(Dungeon.time) * 10f);

		Graphics.batch.setColor(this.r, this.g, this.b, 1);

		Graphics.render(slot, this.x + slot.getRegionWidth() / 2, this.y + slot.getRegionHeight() / 2, a, slot.getRegionWidth() / 2, slot.getRegionHeight() / 2, false, false, this.scale, this.scale);

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

			int w = (int) ((delay / maxDelay) * 24);
			Graphics.batch.setColor(0.3f, 0.3f, 0.3f, 1f);
			TextureRegion region = new TextureRegion(slot);
			region.setRegionWidth(w);
			Graphics.render(region, this.x + slot.getRegionWidth() / 2, this.y + region.getRegionHeight() / 2, a, slot.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, this.scale, this.scale);
			Graphics.batch.setColor(1, 1, 1, 1);

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

		Graphics.batch.setColor(1, 1, 1, 1);

		if (item != null) {
			TextureRegion sprite = item.getSprite();
			int count = item.getValue();

			if (item instanceof Lamp && ((Lamp) item).getRadius() > 0) {
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
			}

			Graphics.render(sprite, this.x + slot.getRegionWidth() / 2,
				this.y + slot.getRegionHeight() / 2, a, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, this.scale, this.scale);

			if (count != 1) {
				Graphics.print(String.valueOf(count), Graphics.small, this.x + 3, this.y + 3);
			}
		}
	}
}