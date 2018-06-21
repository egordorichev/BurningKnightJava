package org.rexcellentgames.burningknight.entity.creature.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.Equipable;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class UiInventory extends UiEntity {
	private Inventory inventory;
	private Item currentSlot;
	private UiSlot[] slots;
	private int active = 0;
	private boolean open = false;
	public boolean handled;
	public int hoveredSlot = -1;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;

		Player.instance.setUi(this);
	}

	@Override
	public void init() {
		createSlots();
	}

	private void createSlots() {
		this.slots = new UiSlot[this.inventory.getSize()];

		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = new UiSlot(this, i, i % 6 * 29 + 4, 4);

			if (i > 5 && i < 12) {
				Item current = this.inventory.getSlot(i);

				if (current instanceof Accessory) {
					current.setOwner(Player.instance);
					((Accessory) current).onEquip();
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
				((Accessory) current).onUnequip();
			}
		}
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getActive() {
		return this.active;
	}

	public boolean isOpen() {
		return this.open;
	}

	protected boolean dn = true;
	private Tween.Task lastA;
	public float forceT;

	@Override
	public void update(float dt) {
		if (Dungeon.game.getState().isPaused() || Dialog.active != null) {
			return;
		}

		this.handled = false;
		this.active = this.inventory.active;
		// this.forceT = 1f; // Math.max(this.forceT - dt, 0);

		if (this.dn) {
			float dx = Math.abs(Input.instance.uiMouse.x - 88);
			float dy = Math.abs(Input.instance.uiMouse.y - 18);
			// float d = (float) Math.sqrt(dx * dx + dy * dy);

			float h = this.inventory.getSize() / 3 * 29;
			boolean nd = Dialog.active != null;

			if (!nd && !this.open && dx < 75f && dy < h) {
				this.open = true;
				this.dn = false;

				if (this.lastA != null) {
					Tween.remove(this.lastA);
					this.lastA = null;
				}

				this.lastA = Tween.to(new Tween.Task(1, 0.1f) {
					@Override
					public float getValue() {
						return slots[0].a;
					}

					@Override
					public void setValue(float value) {
						for (int i = 0; i < inventory.getSize(); i++) {
							UiSlot slot = slots[i];
							slot.a = value;
						}
					}
				});

				Tween.to(new Tween.Task(29 + 4, 0.3f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return slots[6].y;
					}

					@Override
					public void setValue(float value) {
						for (int i = 6; i < 12; i++) {
							UiSlot slot = slots[i];
							slot.y = value;
						}
					}

					@Override
					public void onEnd() {
						super.onEnd();
						dn = true;
					}
				});

				if (this.inventory.getSize() > 12) {
					Tween.to(new Tween.Task(29 + 29 + 4, 0.3f, Tween.Type.BACK_OUT) {
						@Override
						public float getValue() {
							return slots[12].y;
						}

						@Override
						public void setValue(float value) {
							for (int i = 12; i < inventory.getSize(); i++) {
								UiSlot slot = slots[i];
								slot.y = value;
							}
						}

						@Override
						public void onEnd() {
							super.onEnd();
							dn = true;
						}
					});
				}
			} else if (this.open && (nd || dx > 80f || dy > h + 5)) {
				if (this.lastA != null) {
					Tween.remove(this.lastA);
					this.lastA = null;
				}
				this.dn = false;

				this.lastA = Tween.to(new Tween.Task(0.3f, 0.5f, Tween.Type.QUAD_IN) {
					@Override
					public float getValue() {
						return slots[0].a;
					}

					@Override
					public void setValue(float value) {
						for (int i = 0; i < inventory.getSize(); i++) {
							UiSlot slot = slots[i];
							slot.a = value;
						}
					}

					@Override
					public void onEnd() {
						super.onEnd();
						dn = true;
					}
				});

				Tween.to(new Tween.Task(4, 0.2f, Tween.Type.QUAD_OUT) {
					@Override
					public float getValue() {
						return slots[6].y;
					}

					@Override
					public void setValue(float value) {
						for (int i = 6; i < 12; i++) {
							UiSlot slot = slots[i];
							slot.y = value;
						}
					}

					@Override
					public void onEnd() {
						super.onEnd();

						open = false;
					}
				});

				if (this.inventory.getSize() > 12) {
					Tween.to(new Tween.Task(4, 0.2f, Tween.Type.QUAD_OUT) {
						@Override
						public float getValue() {
							return slots[12].y;
						}

						@Override
						public void setValue(float value) {
							for (int i = 12; i < inventory.getSize(); i++) {
								UiSlot slot = slots[i];
								slot.y = value;
							}
						}

						@Override
						public void onEnd() {
							super.onEnd();

							open = false;
						}
					});
				}
			}
		}

		Item item = this.inventory.getSlot(this.active);
		if (!(item instanceof WeaponBase && item.getDelay() > 0)) {

			if (Input.instance.wasPressed("scroll") && Dialog.active == null) {
				this.active = (this.active + Input.instance.getAmount()) % 6;

				if (this.active == -1) {
					this.active = 5;
				}

				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("prev") && Dialog.active == null) {
				this.active -= 1;
				this.forceT = 1f;

				if (this.active == -1) {
					this.active = 5;
				}
			}

			if (Input.instance.wasPressed("next") && Dialog.active == null) {
				this.active = (this.active + 1) % 6;
				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("drop_item") && Dialog.active == null) {
				Item slot = this.inventory.getSlot(this.active);

				if (slot == null) {
					return;
				}

				this.drop(slot);
			}


			if (Input.instance.wasPressed("1")) {
				this.active = 0;
				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("2")) {
				this.active = 1;
				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("3")) {
				this.active = 2;
				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("4")) {
				this.active = 3;
				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("5")) {
				this.active = 4;
				this.forceT = 1f;
			}

			if (Input.instance.wasPressed("6")) {
				this.active = 5;
				this.forceT = 1f;
			}
		}

		if (Player.instance != null) {
			for (UiSlot slot : this.slots) {
				slot.update(dt);
			}
		}

		if (Player.instance != null && !Player.instance.freezed && !this.handled && !Player.instance.isDead()) {
			if (this.currentSlot != null && (Input.instance.wasPressed("mouse0") || Input.instance.wasPressed("mouse1"))) {
				Item slot = this.currentSlot;

				if (!slot.isCursed()) {
					this.drop(slot);
					this.currentSlot = null;
				}
			} else {
				Item slot = this.inventory.getSlot(this.active);

				if (slot != null) {
					if (Input.instance.wasPressed("mouse0")) {
						if (slot.isUseable() && slot.getDelay() == 0) {
							slot.setOwner(Player.instance);
							slot.use();
						}
					} else if (Input.instance.wasPressed("mouse1")) {
						if (slot.isUseable() && slot.getDelay() == 0) {
							slot.setOwner(Player.instance);
							slot.secondUse();
						}
					} else {
						if (Input.instance.isDown("mouse0") && slot.isAuto() && slot.getDelay() == 0) {
							slot.setOwner(Player.instance);
							slot.use();
						} else if (Input.instance.isDown("mouse1") && slot.isAuto() && slot.getDelay() == 0) {
							slot.setOwner(Player.instance);
							slot.secondUse();
						}
					}
				}
			}
		}

		this.inventory.active = this.active;
	}

	private void drop(Item slot) {
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
				ItemHolder holder = new ItemHolder();

				holder.x = (float) Math.floor(Player.instance.x) + (16 - slot.getSprite().getRegionWidth()) / 2;
				holder.y = (float) Math.floor(Player.instance.y) + (16 - slot.getSprite().getRegionHeight()) / 2;
				holder.setItem(slot);
				holder.velToMouse();

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
	private static TextureRegion heart_bg = Graphics.getTexture("ui-heart_bg");
	private static TextureRegion hurt = Graphics.getTexture("ui-hit_heart");
	private static TextureRegion half = Graphics.getTexture("ui-half_heart");

	private static TextureRegion star = Graphics.getTexture("ui-mana_star");
	private static TextureRegion star_bg = Graphics.getTexture("ui-star_bg");
	private static TextureRegion halfStar = Graphics.getTexture("ui-half_star");

	@Override
	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		for (int i = (this.open ? this.inventory.getSize() : 6) - 1; i >= 0; i--) {
			Item item = this.inventory.getSlot(i);
			this.slots[i].render(item);
		}

		float y = this.slots[this.inventory.getSize() - 1].y + 20;
		float x = 4;

		int hp = Player.instance.getHp();
		float invt = Player.instance.getInvt();

		for (int i = 0; i < Player.instance.getHpMax() / 2; i++) {
			float s = 1f;
			float yy = (float) ((hp <= 4 && hp - 2 >= i * 2 - 1) ? Math.cos(((float)i) % 2 / 2 + Dungeon.time * 20) * 2.5f : 0) + y;

			if (hp - 2 == i * 2 || hp - 2 == i * 2 - 1) {
				s = (float) (1f + Math.abs(Math.cos(Dungeon.time * 3) / 3.5f));
			}

			Graphics.render((invt > 0.7f || (invt > 0.5f && invt % 0.2f > 0.1f)) ? hurt : heart_bg, x + i * 11 + heart.getRegionWidth() / 2,
				yy + 8 + heart.getRegionHeight() / 2, 0,
				heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, s, s);

			if (hp - 2 >= i * 2) {
				Graphics.render(heart, x + i * 11 + 1 + heart.getRegionWidth() / 2, yy + 9
					+ heart.getRegionHeight() / 2, 0, heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, s, s);
			} else if (hp - 2 >= i * 2 - 1) {
				Graphics.render(half, x + i * 11 + 1 + heart.getRegionWidth() / 2, yy + 9 + heart.getRegionHeight() / 2, 0,
					heart.getRegionWidth() / 2, heart.getRegionHeight() / 2, false, false, s, s);
			}
		}

		int mana = Player.instance.getHp();

		for (int i = 0; i < Player.instance.getManaMax() / 2; i++) {
			float s = 1f;
			float yy = y + 10;

			Graphics.render(star_bg, x + i * 11 + star.getRegionWidth() / 2,
				yy + 8 + star.getRegionHeight() / 2, 0,
				star.getRegionWidth() / 2, star.getRegionHeight() / 2, false, false, s, s);

			if (mana - 2 >= i * 2) {
				Graphics.render(star, x + i * 11 + 1 + star.getRegionWidth() / 2, yy + 9
					+ star.getRegionHeight() / 2, 0, star.getRegionWidth() / 2, star.getRegionHeight() / 2, false, false, s, s);
			} else if (mana - 2 >= i * 2 - 1) {
				Graphics.render(halfStar, x + i * 11 + 1 + star.getRegionWidth() / 2, yy + 9 + star.getRegionHeight() / 2, 0,
					star.getRegionWidth() / 2, star.getRegionHeight() / 2, false, false, s, s);
			}
		}

		UiBuff[] buffs = Player.instance.uiBuffs.toArray(new UiBuff[]{});

		for (int i = 0; i < buffs.length; i++) {
			UiBuff buff = buffs[i];
			buff.render(i, y);
		}

		this.renderCurrentSlot();
	}

	public UiBuff hoveredBuff;

	public boolean hasEquiped(Class<? extends Equipable> type) {
		for (int i = 6; i < this.inventory.getSize(); i++) {
			Item it = this.inventory.getSlot(i);

			if (type.isInstance(it)) {
				return true;
			}
		}

		return false;
	}

	public void renderCurrentSlot() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (this.currentSlot != null) {
			int count = this.currentSlot.getCount();

			if (count > 0) {
				TextureRegion sprite = this.currentSlot.getSprite();
				Graphics.render(sprite,
					Input.instance.uiMouse.x + 12 - sprite.getRegionWidth() / 2,
					Input.instance.uiMouse.y - 8);


				if (count > 1) {
					Graphics.small.draw(Graphics.batch, String.valueOf(count), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 4);
				}
			}
		} else if (this.hoveredSlot != -1) {
			Item item = this.inventory.getSlot(this.hoveredSlot);

			if (item != null) {
				String info = item.buildInfo().toString();
				Graphics.layout.setText(Graphics.small, info);

				float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

				Graphics.small.setColor(c, c, c, 1);
				Graphics.print(info, Graphics.small, 4, this.slots[this.inventory.getSize() - 1].y + 20 + Graphics.layout.height + 14);
				Graphics.small.setColor(1, 1, 1, 1);

				this.hoveredSlot = -1;
			}
		}

		if (hoveredBuff != null) {
			String info = hoveredBuff.getInfo();
			Graphics.layout.setText(Graphics.small, info);

			float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

			Graphics.small.setColor(c, c, c, 1);
			Graphics.print(info, Graphics.small,4, this.slots[this.inventory.getSize() - 1].y + 20 + Graphics.layout.height + 14 + 15);
			Graphics.small.setColor(1, 1, 1, 1);

			hoveredBuff = null;
		}
	}

	public void renderOnPlayer(Player player) {
		if (player == null || player.isDead()) {
			return;
		}

		Item slot = this.inventory.getSlot(this.active);

		if (slot != null) {
			slot.render(player.x, player.y, player.w, player.h, player.isFlipped());
		}
	}

	public void renderBeforePlayer(Player player) {
		Item slot = this.inventory.getSlot(this.active);

		if (slot != null) {
			slot.beforeRender(player.x, player.y, player.w, player.h, player.isFlipped());
		}
	}

	public void setCurrentSlot(Item currentSlot) {
		this.currentSlot = currentSlot;
	}

	public Item getCurrentSlot() {
		return this.currentSlot;
	}
}