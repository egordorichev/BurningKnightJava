package org.rexcellentgames.burningknight.game;

import com.codedisaster.steamworks.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.ui.UiAchievement;
import org.rexcellentgames.burningknight.util.Log;

public class Achievements {
	public static final String TEST = "TEST_ACHIEVEMENT";

	public static boolean unlocked(String id) {
		return false; // GlobalSave.isTrue(id);
	}

	public static void unlock(String id) {
		if (!unlocked(id)) {
			Log.info(id + " was unlocked!");
			GlobalSave.put(id, true);
			onUnlock(id);

			UiAchievement achievement = new UiAchievement();

			achievement.text = "Test achievement unlocked!";
			achievement.extra = "Idk how to unlock it";

			Dungeon.area.add(achievement);
		}
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