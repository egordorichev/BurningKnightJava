package org.rexellentgames.dungeon.entity.creature.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.accessory.Accessory;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.ui.UiEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.path.Graph;

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
		this.slots = new UiSlot[Player.INVENTORY_SIZE];

		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = new UiSlot(this, i, i % 6 * 29 + /*108*/4, 4);

			if (i > 5) {
				Item current = this.inventory.getSlot(i);

				if (current != null && current instanceof Accessory) {
					((Accessory) current).onEquip();
				}
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
	private boolean hidden;
	private float forceT;

	@Override
	public void update(float dt) {
		this.handled = false;
		this.active = this.inventory.active;
		this.forceT = Math.max(this.forceT - dt, 0);

		if (this.dn) {
			float dx = Math.abs(Input.instance.uiMouse.x - 88);
			float dy = Math.abs(Input.instance.uiMouse.y - 18);
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (this.hidden && dx < 90f && dy < 2f || this.forceT > 0) {
				this.dn = false;

				Tween.to(new Tween.Task(4, 0.3f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return slots[0].y;
					}

					@Override
					public void setValue(float value) {
						for (int i = 0; i < 12; i++) {
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

				this.hidden = false;
			} else {
				if (!this.hidden && !this.open && dx < 90f && dy < 15f) {
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
							for (int i = 0; i < 12; i++) {
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
				} else if (!this.hidden && this.open && (dx > 100f || dy > 60f) && this.forceT == 0) {
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
							for (int i = 0; i < 12; i++) {
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
				} else if (!this.open && !this.hidden && d > 94f) {
					this.dn = false;

					Tween.to(new Tween.Task(-25, 0.3f) {
						@Override
						public float getValue() {
							return slots[0].y;
						}

						@Override
						public void setValue(float value) {
							for (int i = 0; i < 12; i++) {
								UiSlot slot = slots[i];
								slot.y = value;
							}
						}

						@Override
						public void onEnd() {
							super.onEnd();
							dn = true;
							hidden = true;
						}
					});
				}
			}
		}

		if (Input.instance.wasPressed("scroll")) {
			this.active = (this.active + Input.instance.getAmount()) % 6;

			if (this.active == -1) {
				this.active = 5;
			}
		}

		if (Input.instance.wasPressed("prev")) {
			this.active -= 1;
			this.forceT = 1f;

			if (this.active == -1) {
				this.active = 5;
			}
		}

		if (Input.instance.wasPressed("next")) {
			this.active = (this.active + 1) % 6;
			this.forceT = 1f;
		}

		if (Input.instance.wasPressed("drop_item") && !this.open) {
			Item slot = this.inventory.getSlot(this.active);

			if (slot == null) {
				return;
			}

			if (slot.isCursed()) {
				UiLog.instance.print("[red]The item is cursed!");
			} else {
				ItemHolder holder = new ItemHolder();

				holder.x = (float) Math.floor(Player.instance.x) + (16 - slot.getSprite().getRegionWidth()) / 2;
				holder.y = (float) Math.floor(Player.instance.y) + (16 - slot.getSprite().getRegionHeight()) / 2;
				holder.setItem(slot);
				holder.velToMouse();

				this.inventory.setSlot(this.active, null);
				Dungeon.area.add(holder);
				Dungeon.level.addSaveable(holder);
			}
		}

		if (Input.instance.wasPressed("1")) {
			this.active = 0;
		}

		if (Input.instance.wasPressed("2")) {
			this.active = 1;
		}

		if (Input.instance.wasPressed("3")) {
			this.active = 2;
		}

		if (Input.instance.wasPressed("4")) {
			this.active = 3;
		}

		if (Input.instance.wasPressed("5")) {
			this.active = 4;
		}

		if (Input.instance.wasPressed("6")) {
			this.active = 5;
		}

		for (UiSlot slot : this.slots) {
			slot.update(dt);
		}

		if (!this.handled && !Player.instance.isDead()) {
			if (this.currentSlot != null && (Input.instance.wasPressed("mouse0") || Input.instance.wasPressed("mouse1"))) {
				Item slot = this.currentSlot;

				if (slot.isCursed()) {
					UiLog.instance.print("[red]The item is cursed!");
				} else {
					ItemHolder holder = new ItemHolder();

					holder.x = (float) Math.floor(Player.instance.x) + (16 - slot.getSprite().getRegionWidth()) / 2;
					holder.y = (float) Math.floor(Player.instance.y) + (16 - slot.getSprite().getRegionHeight()) / 2;
					holder.setItem(slot);
					holder.velToMouse();

					this.inventory.setSlot(this.active, null);
					Dungeon.area.add(holder);
					Dungeon.level.addSaveable(holder);

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

	private static TextureRegion frame = Graphics.getTexture("ui (exp_bar_frame)");
	private static TextureRegion bar = Graphics.getTexture("ui (exp_in_bar)");
	private static TextureRegion heart = Graphics.getTexture("ui (heart)");
	private static TextureRegion heart_bg = Graphics.getTexture("ui (heart_bg)");
	private static TextureRegion hurt = Graphics.getTexture("ui (hurt_heart)");
	private static TextureRegion half = Graphics.getTexture("ui (half_heart)");

	private float last = 0f;

	@Override
	public void render() {
		this.last += (Player.instance.getExperienceForLevel() - this.last) / 10f;

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		if (!this.hidden) {
			for (int i = (this.open ? this.inventory.getSize() : 6) - 1; i >= 0; i--) {
				Item item = this.inventory.getSlot(i);
				this.slots[i].render(item);
			}
		}

		float y = this.slots[6].y + 29;
		float w = 168f;

		int l = Player.instance.getLevel();
		Graphics.layout.setText(Graphics.small, l + "");

		float x = 4; // Display.GAME_WIDTH / 2 - w / 2;

		TextureRegion region = new TextureRegion();
		region.setRegion(bar);

		Graphics.batch.setColor(0.2f, 0.2f, 0.2f, 1);

		Graphics.render(region, x, y + 1);

		Graphics.batch.setColor(1, 1, 1, 1);

		region.setRegionWidth((int) (this.last / Player.instance.getExperienceMaxForLevel() * region.getRegionWidth()));

		Graphics.render(region, x, y + 1);
		Graphics.render(frame, x, y);

		Graphics.print(l + "", Graphics.small, x + w / 2 - Graphics.layout.width / 2, y - 1);

		int hp = Player.instance.getHp();
		float invt = Player.instance.getInvt();

		for (int i = 0; i < 10; i++) {
			float s = 1f;
			float yy = (float) ((hp <= 8 && hp - 2 >= i * 2 - 1) ? Math.cos(((float)i) % 2 / 2 + Dungeon.time * 20) * 2.5f : 0) + y;

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

		Buff[] buffs = Player.instance.getBuffs().toArray(new Buff[]{});

		for (int i = 0; i < buffs.length; i++) {
			Buff buff = buffs[i];

			TextureRegion sprite = buff.getSprite();
			Graphics.batch.draw(sprite, 4 + i * 12, y + 9 + 11);
		}
	}

	public void renderCurrentSlot() {
		if (this.currentSlot != null) {
			TextureRegion sprite = this.currentSlot.getSprite();
			Graphics.render(sprite,
				Input.instance.uiMouse.x + 12 - sprite.getRegionWidth() / 2,
				Input.instance.uiMouse.y - 8);

			int count = this.currentSlot.getCount();

			if (count > 1) {
				Graphics.small.draw(Graphics.batch, String.valueOf(count), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 4);
			}
		} else if (this.hoveredSlot != -1) {
			Item item = this.inventory.getSlot(this.hoveredSlot);

			if (item != null) {
				String info = item.buildInfo().toString();
				Graphics.layout.setText(Graphics.small, info);

				float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

				Graphics.small.setColor(c, c, c, 1);
				Graphics.print(info, Graphics.small,
					MathUtils.clamp(1, Display.GAME_WIDTH - 1, (int) Input.instance.uiMouse.x + 12),
					MathUtils.clamp((int) Graphics.layout.height + 1, Display.GAME_HEIGHT - 1, (int) Input.instance.uiMouse.y + 2));
				Graphics.small.setColor(1, 1, 1, 1);

				this.hoveredSlot = -1;
			}
		}
	}

	public void renderOnPlayer(Player player) {
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