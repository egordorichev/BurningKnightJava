package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.StartingItem;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiTextInput;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class ItemSelectState extends State {
	private static ArrayList<Item> melee = new ArrayList<>();
	private static ArrayList<Item> ranged = new ArrayList<>();
	private static ArrayList<Item> mage = new ArrayList<>();

	public static int depth;


	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		return builder.toString();
	}

	public static int stringToSeed(String str) {
		int seed = 0;

		for (int i = 0; i < str.length(); i++) {
			seed += str.charAt(i);
		}

		return seed;
	}

	@Override
	public void init() {
		super.init();
		StartingItem.hovered = null;

		seed = randomAlphaNumeric(8);

		SaveManager.delete();
		Audio.play("Void");

		melee.clear();
		ranged.clear();
		mage.clear();

		mage.add(new MagicMissileWand());

		boolean ur = GlobalSave.isTrue("unlocked_ranged");

		if (ur) {
			ranged.add(new Revolver());
		}

		boolean um = GlobalSave.isTrue("unlocked_melee");

		if (um) {
			melee.add(new Sword());
		}

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

		/*if (Achievements.unlocked("UNLOCK_BUTCHER")) {
			melee.add(new Butcher());
		}

		if (Achievements.unlocked("UNLOCK_MORNING")) {
			melee.add(new MorningStar());
		}


		if (Achievements.unlocked("UNLOCK_BOW")) {
			ranged.add(new Bow());
		}*/

		if (Achievements.unlocked("UNLOCK_AXE")) {
			ranged.add(new Axe());
		}

		/*if (Achievements.unlocked("UNLOCK_NOPPY")) {
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
		}*/

		picked = false;

		for (int i = 0; i < melee.size(); i++) {
			StartingItem item = new StartingItem();
			item.item = melee.get(i);
			item.name = melee.get(i).getName();
			item.type = Player.Type.WARRIOR;
			item.y = Display.UI_HEIGHT / 2 - 48;
			item.x = (Display.UI_WIDTH - melee.size() * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		for (int i = 0; i < ranged.size(); i++) {
			StartingItem item = new StartingItem();
			item.item = ranged.get(i);
			item.name = ranged.get(i).getName();
			item.type = Player.Type.RANGER;
			item.y = Display.UI_HEIGHT / 2;
			item.x = (Display.UI_WIDTH - ranged.size() * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		for (int i = 0; i < mage.size(); i++) {
			StartingItem item = new StartingItem();
			item.item = mage.get(i);
			item.name = mage.get(i).getName();
			item.type = Player.Type.WIZARD;
			item.y = Display.UI_HEIGHT / 2 + 48;
			item.x = (Display.UI_WIDTH - mage.size() * 48) / 2 + i * 48 + 24;
			Dungeon.ui.add(item);
		}

		Dungeon.ui.add(new UiButton("random", Display.UI_WIDTH / 2, Display.UI_HEIGHT / 2 - 48 * 2) {
			@Override
			public void onClick() {
				super.onClick();
				float r = Random.newFloat(1);

				if (r < 0.333f || (melee.size() == 0 && ranged.size() == 0)) {
					pick(mage.get(Random.newInt(mage.size() - 1)), Player.Type.WIZARD);
				} else if (r < 0.666f || ranged.size() == 0) {
					pick(melee.get(Random.newInt(melee.size() - 1)), Player.Type.WARRIOR);
				} else {
					pick(ranged.get(Random.newInt(ranged.size() - 1)), Player.Type.RANGER);
				}
			}
		});

		input = (UiTextInput) Dungeon.ui.add(new UiTextInput("seed", Display.UI_WIDTH / 2, (int) (Display.UI_HEIGHT / 2 - 48 * 2.5f)) {
			@Override
			public int getMaxLength() {
				return 8;
			}

			@Override
			public char validate(char ch) {
				if (!Character.isLetterOrDigit(ch)) {
					return '\0';
				}

				return Character.toUpperCase(ch);
			}
		});

		Ui.saveAlpha = 0;

		Dungeon.white = 0;
		Dungeon.dark = 1;
		Dungeon.darkR = Dungeon.MAX_R;

		uiY = Display.UI_HEIGHT;
		Tween.to(new Tween.Task(0, 0.5f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return uiY;
			}

			@Override
			public void setValue(float value) {
				uiY = value;
			}
		});

		if (!ur && !um && mage.size() == 1) {
			fastPick(mage.get(0), Player.Type.WIZARD);
		}
	}

	private static UiTextInput input;
	private static String seed;

	@Override
	public void destroy() {
		super.destroy();
		Ui.saveAlpha = 0;
	}

	private static float uiY;

	@Override
	public void renderUi() {
		renderPortal();
		super.renderUi();

		Camera.ui.position.y -= uiY;
		Camera.ui.update();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Dungeon.ui.render();
		Graphics.print("Pick starting item", Graphics.medium, Display.UI_HEIGHT / 2 + 48 * 2);

		Camera.ui.position.y += uiY;
		Camera.ui.update();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Ui.ui.renderCursor();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("F")) {
			InGameState.horn();
		}

		if (Input.instance.wasPressed("pause")) {
			InGameState.triggerPause = true;
		}
	}

	private static boolean picked;

	public static void pick(Item item, Player.Type tp) {
		if (picked) {
			return;
		}

		picked = true;

		Tween.to(new Tween.Task(Display.UI_HEIGHT, 0.15f, Tween.Type.QUAD_IN) {
			@Override
			public float getValue() {
				return uiY;
			}

			@Override
			public void setValue(float value) {
				uiY = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				fastPick(item, tp);
			}
		});
	}

	public static void fastPick(Item item, Player.Type tp) {
		Player.toSet = tp;
		GlobalSave.put("last_class", Player.toSet.id);
		Player.startingItem = item;

		LoadState.fromSelect = true;
		Dungeon.game.setState(new LoadState());

		if (input != null) {
			String string = input.input;

			if (!string.isEmpty()) {
				seed = string;
			}

			input = null;
		}

		Random.setSeed(seed);
	}
}