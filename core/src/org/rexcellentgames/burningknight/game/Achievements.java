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

	// Item unlocks
	public static final String UNLOCK_BLACK_HEART = "UNLOCK_BLACK_HEART";
	// Secret
	// TODO

	private static ArrayList<UiAchievement> toShow = new ArrayList<>();
	private static Area top = new Area(true);
	private static UiAchievement lastActive;

	public static boolean unlocked(String id) {
		return false; // GlobalSave.isTrue(id);
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