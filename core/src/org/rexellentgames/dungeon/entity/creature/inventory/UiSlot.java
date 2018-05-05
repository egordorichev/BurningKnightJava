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
import org.rexellentgames.dungeon.entity.item.accessory.equipable.Equipable;
import org.rexellentgames.dungeon.entity.item.accessory.hat.Hat;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;

public class UiSlot {
	private static TextureRegion slot;
	private static TextureRegion armorBg = Graphics.getTexture("ui (armor_bg)");
	private static TextureRegion coinBg = Graphics.getTexture("ui (gold_bg)");
	private static TextureRegion equipBg = Graphics.getTexture("ui (equip_bg)");

	private int x;
	private int y;
	private int id;
	private boolean hovered = false;
	private UiInventory inventory;

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

		if (item != null) {
			item.update(dt);

			if (item.getCount() == 0) {
				this.inventory.getInventory().setSlot(this.id, null);
			}
		}

		if (!this.inventory.isOpen() && this.id >= 6) {
			return;
		}

		this.hovered = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, this.x, this.y, 24, 24);

		if (this.hovered) {
			this.inventory.hoveredSlot = this.id;
			this.inventory.handled = true;

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

	public void render(Item item) {
		if (this.inventory.getActive() == this.id) {
			Graphics.batch.setColor(0.6f, 0.6f, 0.6f, 1);
		} else if (this.hovered) {
			Graphics.batch.setColor(0.8f, 0.8f, 0.8f, 1);
		}

		Graphics.render(slot, this.x, this.y);

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
			Graphics.batch.setColor(0.5f, 0.5f, 0.5f, 1f);
			TextureRegion region = new TextureRegion(slot);
			region.setRegionWidth(w);
			Graphics.render(region, this.x, this.y);
			Graphics.batch.setColor(1, 1, 1, 1);
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

			Graphics.render(sprite, this.x + 12 - sprite.getRegionWidth() / 2,
				this.y + 12 - sprite.getRegionHeight() / 2);

			if (count != 1) {
				Graphics.print(String.valueOf(count), Graphics.small, this.x + 3, this.y + 3);
			}
		}
	}
}