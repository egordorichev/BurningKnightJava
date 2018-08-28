package org.rexcellentgames.burningknight.game;

import com.codedisaster.steamworks.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemRegistry;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.ui.UiAchievement;
import org.rexcellentgames.burningknight.util.Log;

import java.util.ArrayList;

public class Achievements {
	// Regular achievements
	public static final String TEST = "TEST_ACHIEVEMENT";

	public static final String REACH_DESERT = "REACH_DESERT_ACHIEVEMENT";
	public static final String REACH_LIBRARY = "REACH_LIBRARY_ACHIEVEMENT";
	public static final String KILL_BK = "KILL_BK_ACHIEVEMENT";
	public static final String DIE = "DIE_ACHIEVEMENT";
	public static final String BURN_TO_DEATH = "BURN_TO_DEATH_ACHIEVEMENT";
	public static final String FIND_MIMIC = "FIND_MIMIC_ACHIEVEMENT";
	public static final String KILL_DM = "KILL_DM_ACHIEVEMENT";
	public static final String SELL_10_ITEMS = "SELL_10_ITEMS_ACHIEVEMENT";
	public static final String EQUIP_ACCESSORY = "EQUIP_ACCESSORY_ACHIEVEMENT";
	public static final String COLLECT_300_GOLD = "COLLECT_300_GOLD_ACHIEVEMENT";
	public static final String GET_8_HEART_CONTAINERS = "GET_8_HEART_CONTAINERS_ACHIEVEMENT";
	public static final String DONT_GET_HIT_IN_BOSS_FIGHT = "DONT_GET_HIT_IN_BOSS_FIGHT_ACHIEVEMENT";
	public static final String FIND_CRASH_BOOK = "FIND_CRASH_BOOK_ACHIEVEMENT";
	public static final String FILL_UP_INVENTORY = "FILL_UP_INVENTORY_ACHIEVEMENT";
	public static final String FIND_SECRET_ROOM = "FIND_SECRET_ROOM_ACHIEVEMENT";
	public static final String UPGRADE = "UPGRADE_ACHIEVEMENT";
	// Item unlocks
	public static final String UNLOCK_BLACK_HEART = "UNLOCK_BLACK_HEART";
	public static final String UNLOCK_MIMIC_TOTEM = "UNLOCK_MIMIC_TOTEM";
	public static final String UNLOCK_MIMIC_SUMMONER = "UNLOCK_MIMIC_SUMMONER";
	public static final String UNLOCK_SALE = "UNLOCK_SALE";
	public static final String UNLOCK_MONEY_PRINTER = "UNLOCK_MONEY_PRINTER";
	public static final String UNLOCK_CRASH_BOOK = "UNLOCK_CRASH_BOOK";
	public static final String UNLOCK_BLOOD_CROWN = "UNLOCK_BLOOD_CROWN";
	public static final String UNLOCK_DIAMOND_SWORD = "UNLOCK_DIAMOND_SWORD";
	public static final String UNLOCK_DIAMOND = "UNLOCK_DIAMOND";
	public static final String UNLOCK_HALO = "UNLOCK_HALO";
	public static final String UNLOCK_WATER_BOLT = "UNLOCK_WATER_BOLT";
	public static final String UNLOCK_WINGS = "UNLOCK_WINGS";
	public static final String UNLOCK_DEW_VIAL = "UNLOCK_DEW_VIAL";
	public static final String UNLOCK_LOOTPICK = "UNLOCK_LOOTPICK";
	public static final String UNLOCK_SWORD_ORBITAL = "UNLOCK_SWORD_ORBITAL";
	public static final String UNLOCK_AMMO_ORBITAL = "UNLOCK_AMMO_ORBITAL";
	public static final String UNLOCK_SPECTACLES = "UNLOCK_SPECTACLES";
	public static final String UNLOCK_MEATBOY = "UNLOCK_MEETBOY";
	public static final String UNLOCK_MEATBOY_AXE = "UNLOCK_MEETBOY_AXE";
	public static final String UNLOCK_GOLD_RING = "UNLOCK_GOLD_RING";
	public static final String UNLOCK_BACKPACK = "UNLOCK_BACKPACK";
	public static final String UNLOCK_ISAAC_HEAD = "UNLOCK_ISAAC_HEAD";
	public static final String UNLOCK_DENDY = "UNLOCK_KOTLING_GUN";
	// Special room unlocks, TODO
	public static final String UNLOCK_CLOCK_HEART = "UNLOCK_CLOCK_HEART";
	public static final String UNLOCK_VVVVV = "UNLOCK_VVVVV";
	public static final String UNLOCK_STOP_WATCH = "UNLOCK_STOP_WATCH";
	public static final String UNLOCK_STAR = "UNLOCK_STAR";
	public static final String UNLOCK_KOTLING_GUN = "UNLOCK_KOTLING_GUN";
	// todo: unlock for resizing win to min
	// Secret
	// TODO

	private static ArrayList<UiAchievement> toShow = new ArrayList<>();
	private static Area top = new Area(true);
	private static UiAchievement lastActive;

	public static boolean unlocked(String id) {
		return id == null || GlobalSave.isTrue(id);
	}

	public static void unlock(String id) {
		if (!unlocked(id)) {
			Log.info(id + " was unlocked!");
			GlobalSave.put(id, true);
			onUnlock(id);

			UiAchievement achievement = new UiAchievement();

			if (id.contains("ACHIEVEMENT")) {
				achievement.text = Locale.get(id.toLowerCase());
				achievement.extra = Locale.get(id.toLowerCase() + "_desc");
				achievement.icon = Graphics.getTexture("achievements-" + (id.toLowerCase().replace("_achievement", "")));
			} else {
				String reg = id.replace("UNLOCK_", "").toLowerCase();

				try {
					ItemRegistry.Pair pair = ItemRegistry.INSTANCE.getItems().get(reg);

					if (pair == null) {
						Log.error("Failed to unlock item " + reg);
						return;
					}

					Item item = pair.getType().newInstance();

					achievement.text = item.getName() + " " + Locale.get("was_unlocked");
					achievement.icon = item.getSprite();
					achievement.unlock = true;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				// todo
			}

			toShow.add(achievement);
		}
	}

	public static void clear() {
		top.destroy();
	}

	public static void update(float dt) {
		if ((lastActive == null || lastActive.done) && toShow.size() > 0) {
			lastActive = toShow.get(0);
			toShow.remove(0);

			top.add(lastActive);
		}

		top.update(dt);
	}

	public static void render() {
		top.render();
	}

	private static void onUnlock(String id) {
		if (stats != null) {
			stats.setAchievement(id);
		}
	}

	private static SteamUserStats stats;

	public static void init() {
		if (Dungeon.steam) {
			stats = new SteamUserStats(new SteamUserStatsCallback() {
				@Override
				public void onUserStatsReceived(long gameId, SteamID steamIDUser, SteamResult result) {

				}

				@Override
				public void onUserStatsStored(long gameId, SteamResult result) {

				}

				@Override
				public void onUserStatsUnloaded(SteamID steamIDUser) {

				}

				@Override
				public void onUserAchievementStored(long gameId, boolean isGroupAchievement, String achievementName, int curProgress, int maxProgress) {

				}

				@Override
				public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard, boolean found) {

				}

				@Override
				public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle leaderboard, SteamLeaderboardEntriesHandle entries, int numEntries) {

				}

				@Override
				public void onLeaderboardScoreUploaded(boolean success, SteamLeaderboardHandle leaderboard, int score, boolean scoreChanged, int globalRankNew, int globalRankPrevious) {

				}

				@Override
				public void onGlobalStatsReceived(long gameId, SteamResult result) {

				}
			});
		}
	}

	public static void dispose() {

	}
}