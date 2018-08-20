package org.rexcellentgames.burningknight.game;

import com.codedisaster.steamworks.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.ui.UiAchievement;
import org.rexcellentgames.burningknight.util.Log;

import java.util.ArrayList;

public class Achievements {
	public static final String TEST = "TEST_ACHIEVEMENT";
	public static final String REACH_DESERT = "REACH_DESERT_ACHIEVEMENT";
	public static final String REACH_LIBRARY = "REACH_LIBRARY_ACHIEVEMENT";
	public static final String KILL_BK = "KILL_BK_ACHIEVEMENT";
	public static final String DIE = "DIE_ACHIEVEMENT";

	private static ArrayList<UiAchievement> toShow = new ArrayList<UiAchievement>();
	private static Area top = new Area();
	private static UiAchievement lastActive;

	public static boolean unlocked(String id) {
		return GlobalSave.isTrue(id);
	}

	public static void unlock(String id) {
		if (!unlocked(id)) {
			Log.info(id + " was unlocked!");
			GlobalSave.put(id, true);
			onUnlock(id);

			UiAchievement achievement = new UiAchievement();

			achievement.text = Locale.get(id.toLowerCase());
			achievement.extra = Locale.get(id.toLowerCase() + "_desc");
			achievement.icon = Graphics.getTexture("achievements-" + (id.toLowerCase().replace("_achievement", "")));

			toShow.add(achievement);
		}
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