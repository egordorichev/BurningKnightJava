package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Noise;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.Bow;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.BurstGun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.NoppyGun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.FireWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.FastBook;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.HomingBook;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.TripleShotBook;
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Butcher;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.MorningStar;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.StartingItem;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class ItemSelectState extends State {
	private static ArrayList<Item> melee = new ArrayList<>();
	private static  ArrayList<Item> ranged = new ArrayList<>();
	private static  ArrayList<Item> mage = new ArrayList<>();

	public static int depth;

	@Override
	public void init() {
		super.init();

		Audio.stop();

		Dungeon.white = 0;
		Dungeon.dark = 0;
		Dungeon.darkR = Dungeon.MAX_R;

		melee.clear();
		ranged.clear();
		mage.clear();

		melee.add(new Sword());
		ranged.add(new Revolver());
		mage.add(new MagicMissileWand());

		if (Player.instance != null) {
			Dungeon.area.remove(Player.instance);
			PlayerSave.remove(Player.instance);
			Player.instance = null;
		}

		if (Achievements.unlocked("UNLOCK_DAGGER")) {
			melee.add(new Dagger());
		}

		if (Achievements.unlocked("UNLOCK_SPEAR")) {
			melee.add(new Spear());
		}

		if (Achievements.unlocked("UNLOCK_BUTCHER")) {
			melee.add(new Butcher());
		}

		if (Achievements.unlocked("UNLOCK_MORNING")) {
			melee.add(new MorningStar());
		}


		if (Achievements.unlocked("UNLOCK_BOW")) {
			ranged.add(new Bow());
		}

		if (Achievements.unlocked("UNLOCK_AXE")) {
			ranged.add(new Axe());
		}

		if (Achievements.unlocked("UNLOCK_NOPPY")) {
			ranged.add(new NoppyGun());
		}

		if (Achievements.unlocked("UNLOCK_BURST")) {
			ranged.add(new BurstGun());
		}

		if (Achievements.unlocked("UNLOCK_FAST_BOOK")) {
			mage.add(new FastBook());
		}

		if (Achievements.unlocked("UNLOCK_AIM_BOOK")) {
			mage.add(new HomingBook());
		}

		if (Achievements.unlocked("UNLOCK_FIRE_WAND")) {
			mage.add(new FireWand());
		}

		if (Achievements.unlocked("UNLOCK_TRIPLE_BOOK")) {
			mage.add(new TripleShotBook());
		}

		picked = false;

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

		for (int i = 0; i < melee.size(); i++) {
			StartingItem item = new StartingItem();
			item.item = melee.get(i);
			item.type = Player.Type.WARRIOR;
			item.y = Display.UI_HEIGHT / 2 + 48;
			item.x = (Display.UI_WIDTH - melee.size() * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		for (int i = 0; i < ranged.size(); i++) {
			StartingItem item = new StartingItem();
			item.item = ranged.get(i);
			item.type = Player.Type.RANGER;
			item.y = Display.UI_HEIGHT / 2;
			item.x = (Display.UI_WIDTH - ranged.size() * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		for (int i = 0; i < mage.size(); i++) {
			StartingItem item = new StartingItem();
			item.item = mage.get(i);
			item.type = Player.Type.WIZARD;
			item.y = Display.UI_HEIGHT / 2 - 48;
			item.x = (Display.UI_WIDTH - mage.size() * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		Dungeon.ui.add(new UiButton("random", Display.UI_WIDTH / 2, Display.UI_HEIGHT / 2 - 48 * 2) {
			@Override
			public void onClick() {
				super.onClick();
				float r = Random.newFloat(1);

				if (r < 0.333f) {
					pick(melee.get(Random.newInt(melee.size() - 1)), Player.Type.WARRIOR);
				} else if (r < 0.666f) {
					pick(ranged.get(Random.newInt(ranged.size() - 1)), Player.Type.RANGER);
				} else {
					pick(mage.get(Random.newInt(mage.size() - 1)), Player.Type.WIZARD);
				}
			}
		});

		Ui.saveAlpha = 0;
	}

	@Override
	public void destroy() {
		super.destroy();
		Ui.saveAlpha = 0;
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Graphics.print("Pick starting item", Graphics.medium, Display.UI_HEIGHT / 2 + 48 * 2);

		Dungeon.ui.render();
		Ui.ui.renderCursor();
	}

	private static boolean picked;

	public static void pick(Item item, Player.Type tp) {
		if (picked) {
			return;
		}

		picked = true;

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

				PlayerSave.generate();
				Dungeon.area.remove(Player.instance);

				SaveManager.save(SaveManager.Type.PLAYER, false);
				Dungeon.goToLevel(depth);
			}
		});
	}

	@Override
	public void render() {
		super.render();


		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.nil.combined);
		Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360, 360, 360);
		Dungeon.setBackground2(new Color(cl.r * 0.4f, cl.g * 0.4f, cl.b * 0.4f, 1f));

		for (int i = 0; i < 65; i++) {
			float s = i * 0.015f;
			float mx = (Noise.instance.noise(Dungeon.time * 0.25f + s) * 128f);
			float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + s) * 128f);
			float v = ((float) i) / 80f + 0.3f;

			Color color = ColorUtils.HSV_to_RGB((Dungeon.time * 20 - i * 1.4f) % 360, 360, 360);
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.5f);

			float a = (float) (Math.PI * i * 0.2f) + Dungeon.time * 2f;
			float w = i * 2 + 64;
			float d = i * 4f;
			float x = (float) (Math.cos(a) * d) + Display.GAME_WIDTH / 2 + mx * (((float) 56-i) / 56);
			float y = (float) (Math.sin(a) * d) + Display.GAME_HEIGHT / 2 + my * (((float) 56-i) / 56);

			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 1f, 1f, (float) Math.toDegrees(a + 0.1f));
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.9f);
			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 0.9f, 0.9f, (float) Math.toDegrees(a + 0.1f));
		}

		float i = 32;
		float mx = (Noise.instance.noise(Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);
		float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);

		Graphics.endAlphaShape();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Graphics.render(InventoryState.player, Display.GAME_WIDTH / 2 + mx, Display.GAME_HEIGHT / 2 + my, Dungeon.time * 650, 8, 8,false, false);
	}
}