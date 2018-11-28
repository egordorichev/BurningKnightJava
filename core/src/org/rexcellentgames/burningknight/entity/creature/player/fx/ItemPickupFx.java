package org.rexcellentgames.burningknight.entity.creature.player.fx;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.Confetti;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.NullItem;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.*;
import org.rexcellentgames.burningknight.entity.item.key.BurningKey;
import org.rexcellentgames.burningknight.entity.level.entities.ClassSelector;
import org.rexcellentgames.burningknight.entity.level.entities.HatSelector;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Random;
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
			this.text = Locale.get(((ClassSelector) item).getClass());
		} else {
			this.text = item.getItem().getName();

			if (this.text == null) {
				this.text = "Missing item name";
			}
		}

		if (this.text.equals("null_item")) {
			this.text = "";
		}

		this.item = item;
		this.player = player;

		Graphics.layout.setText(Graphics.medium, this.text);
		this.x = item.x + item.w / 2 - Graphics.layout.width / 2;
		this.y = item.y + item.h + 4;
		Tween.to(new Tween.Task(1, 0.2f, Tween.Type.QUAD_OUT) {
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

	public static Item setSkin(String skin) {
		if (skin == null || skin.equals("gobbo_head")) {
			return new NullItem();
		} else if (skin.equals("knight")) {
			return new KnightHat();
		} else if (skin.equals("stone")) {
			return new MoaiHat();
		} else if (skin.equals("viking")) {
			return new VikingHat();
		} else if (skin.equals("dunce")) {
			return new DunceHat();
		} else if (skin.equals("ravi")) {
			return new RaveHat();
		} else if (skin.equals("ushanka")) {
			return new UshankaHat();
		} else if (skin.equals("ruby")) {
			return new RubyHat();
		} else if (skin.equals("gold")) {
			return new GoldHat();
		} else if (skin.equals("wings")) {
			return new ValkyreHat();
		} else if (skin.equals("skull")) {
			return new SkullHat();
		} else if (skin.equals("cowboy")) {
			return new CoboiHat();
		} else if (skin.equals("red_mushroom")) {
			return new ShroomHat();
		} else if (skin.equals("brown_mushroom")) {
			return new FungiHat();
		}
		
		return null;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.go) {
			return;
		}

		if (Input.instance.wasPressed("interact") && Dialog.active == null) {
			if (this.item.getItem() instanceof NullItem) {
				String skin = Player.hatId;
				Player.instance.setHat("gobbo_head");
				Player.instance.getInventory().setSlot(6, null);
				HatSelector.nullGot = false;
				this.item.setItem(setSkin(skin));
				Player.instance.playSfx("menu/select");
			} else if (this.item.getItem() instanceof Hat && Dungeon.depth == -2) {
				String skin = Player.hatId;
				Player.instance.setHat(((Hat) this.item.getItem()).skin);
				Player.instance.getInventory().setSlot(6, this.item.getItem());
				this.item.getItem().setOwner(Player.instance);
				this.item.setItem(setSkin(skin));
				Player.instance.playSfx("menu/select");
			} else if (this.item instanceof ClassSelector) {
				ClassSelector s = (ClassSelector) this.item;

				// Item item = Player.instance.getInventory().getSlot(0);
				Item old = this.item.getItem();

				old.setOwner(Player.instance);
				Player.instance.getInventory().setSlot(0, old);

				if ("warrior".equals(s.getClass())) {
					Player.instance.setType(Player.Type.WARRIOR);
				} else if ("wizard".equals(s.getClass())) {
					Player.instance.setType(Player.Type.WIZARD);
				} else if ("ranger".equals(s.getClass())) {
					Player.instance.setType(Player.Type.RANGER);
				}

				this.erase();
			} else {
				if (this.item.getItem().shop) {
					int g = this.player.getMoney();

					if (g < this.item.getItem().price) {
						this.remove();
						Player.instance.playSfx("item_nocash");

						Camera.shake(6);
						return;
					} else {
						this.player.setMoney(g - this.item.getItem().price);
						this.item.getItem().shop = false;
						this.item.getPrice().remove();
						this.erase();
						Player.instance.playSfx("item_purchase");

						if (Shopkeeper.instance != null && !Shopkeeper.instance.enranged) {
							Shopkeeper.instance.become("thanks");
						}

						for (int i = 0; i < 15; i++) {
							Confetti c = new Confetti();

							c.x = this.item.x + Random.newFloat(this.item.w);
							c.y = this.item.y + Random.newFloat(this.item.h);
							c.vel.x = Random.newFloat(-30f, 30f);
							c.vel.y = Random.newFloat(30f, 40f);

							Dungeon.area.add(c);
						}
					}
				}

				if (this.player.tryToPickup(this.item)) {
					this.erase();
					LevelSave.remove(item);
					if (item.getItem() instanceof BurningKey) {
						Player.instance.hasBkKey = true;
					}
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

		Tween.to(new Tween.Task(0, 0.3f) {
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