package org.rexcellentgames.burningknight.entity.creature.player.fx;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.entities.ClassSelector;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Tween;

public class ItemPickupFx extends Entity {
	private String text;
	public ItemHolder item;
	private Player player;
	private float a;
	private boolean go;

	{
		depth = 15;
		alwaysActive = true;
	}

	public ItemPickupFx(ItemHolder item, Player player) {
		if (item instanceof ClassSelector) {
			this.text = Locale.get(((ClassSelector) item).id);
		} else {
			this.text = item.getItem().getName();

			if (this.text == null) {
				this.text = "Missing item name";
			}
		}

		this.item = item;
		this.player = player;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + item.w / 2 - Graphics.layout.width / 2;
		this.y = item.y + item.h + 4;
		Tween.to(new Tween.Task(1, 0.1f, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.go) {
			return;
		}

		if (Input.instance.wasPressed("pickup") && Dialog.active == null) {
			if (this.item instanceof ClassSelector) {
				ClassSelector s = (ClassSelector) this.item;

				// Item item = Player.instance.getInventory().getSlot(0);
				Item old = this.item.getItem();

				old.setOwner(Player.instance);
				Player.instance.getInventory().setSlot(0, old);

				switch (s.id) {
					case "warrior": Player.instance.setType(Player.Type.WARRIOR); break;
					case "wizard": Player.instance.setType(Player.Type.WIZARD); break;
					case "ranger": Player.instance.setType(Player.Type.RANGER); break;
				}

				this.erase();
			} else {
				if (this.item.getItem().shop) {
					int g = this.player.getInventory().getGold();

					if (g < this.item.getItem().price) {
						this.remove();
						Player.instance.playSfx("item_nocash");
						return;
					} else {
						this.player.getInventory().removeGold(this.item.getItem().price);
						this.item.getItem().shop = false;
						this.item.price.remove();
						this.erase();
						Player.instance.playSfx("item_purchase");

						if (Shopkeeper.instance != null && !Shopkeeper.instance.enranged) {
							Shopkeeper.instance.become("thanks");
						}
					}
				}

				if (this.player.tryToPickup(this.item)) {
					this.erase();
					LevelSave.remove(item);
				}
			}
		} else if (this.item.done) {
			LevelSave.remove(item);
		}
	}

	public void remove() {
		if (go) {
			return;
		}

		this.go = true;

		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public void onEnd() {
				setDone(true);
			}
		});
	}

	public void erase() {
		for (int i = 0; i < 3; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.item.x + this.item.w / 2;
			fx.y = this.item.y + this.item.h / 2;

			Dungeon.area.add(fx);
		}

		this.go = true;

		if (!(this.item instanceof ClassSelector) && !text.startsWith("+")) {
			this.text = "+" + this.text;

			Graphics.layout.setText(Graphics.medium, this.text);
			this.x = item.x + item.w / 2 - Graphics.layout.width / 2;
			this.y = item.y + item.h + 4;
		}


		if (!(this.item instanceof ClassSelector)) {
			Tween.to(new Tween.Task(this.y + 10, 2f, Tween.Type.QUAD_OUT) {
				@Override
				public float getValue() {
					return y;
				}

				@Override
				public void setValue(float value) {
					y = value;
				}
			});
		}

		Tween.to(new Tween.Task(0, (this.item instanceof ClassSelector) ? 0.4f : 2f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public void onEnd() {
				setDone(true);
			}
		});
	}

	@Override
	public void render() {
		float c = (float) (0.8f + Math.cos(Dungeon.time * 10) / 5f);

		Graphics.medium.setColor(c, c, c, this.a);
		Graphics.print(this.text, Graphics.medium, this.x, this.y);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}