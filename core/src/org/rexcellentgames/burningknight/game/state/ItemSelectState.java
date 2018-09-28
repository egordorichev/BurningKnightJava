package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.Bow;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.FastBook;
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.StartingItem;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class ItemSelectState extends State {
	private static Item[] melee = {
		new Sword(), new Dagger(), new Spear()
	};

	private static Item[] ranged = {
		new Gun(), new Bow()
	};

	private static final Item[] mage = {
		new MagicMissileWand(), new FastBook()
	};

	public static int depth;

	@Override
	public void init() {
		super.init();

		Dungeon.dark = 0;
		Tween.to(new Tween.Task(1, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.dark;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}
		});

		for (int i = 0; i < melee.length; i++) {
			StartingItem item = new StartingItem();
			item.item = melee[i];
			item.type = Player.Type.WARRIOR;
			item.y = Display.UI_HEIGHT / 2 + 48;
			item.x = (Display.UI_WIDTH - melee.length * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		for (int i = 0; i < ranged.length; i++) {
			StartingItem item = new StartingItem();
			item.item = ranged[i];
			item.type = Player.Type.RANGER;
			item.y = Display.UI_HEIGHT / 2;
			item.x = (Display.UI_WIDTH - ranged.length * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		for (int i = 0; i < mage.length; i++) {
			StartingItem item = new StartingItem();
			item.item = mage[i];
			item.type = Player.Type.WIZARD;
			item.y = Display.UI_HEIGHT / 2 - 48;
			item.x = (Display.UI_WIDTH - mage.length * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		Dungeon.ui.add(new UiButton("random", Display.UI_WIDTH / 2, Display.UI_HEIGHT / 2 - 48 * 2) {
			@Override
			public void onClick() {
				super.onClick();
				float r = Random.newFloat(1);

				if (r < 0.333f) {
					pick(melee[Random.newInt(melee.length - 1)], Player.Type.WARRIOR);
				} else if (r < 0.666f) {
					pick(ranged[Random.newInt(ranged.length - 1)], Player.Type.RANGER);
				} else {
					pick(mage[Random.newInt(mage.length - 1)], Player.Type.WIZARD);
				}
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Graphics.print("Pick starting item", Graphics.medium, Display.UI_HEIGHT / 2 + 48 * 2);

		Dungeon.ui.render();
		Ui.ui.renderCursor();
	}

	public static void pick(Item item, Player.Type tp) {
		Tween.to(new Tween.Task(0, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.dark;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}

			@Override
			public void onEnd() {
				Player.toSet = tp;
				GlobalSave.put("last_class", Player.toSet.id);

				Player.startingItem = item;
				Player.instance.generate();
				SaveManager.save(SaveManager.Type.PLAYER, false);
				Dungeon.goToLevel(depth);
			}
		});
	}
}