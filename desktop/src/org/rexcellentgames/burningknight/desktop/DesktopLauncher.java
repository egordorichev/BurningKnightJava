package org.rexcellentgames.burningknight.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.*;
import org.rexcellentgames.burningknight.assets.Assets;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DesktopLauncher {
	private static final int SCALE = 3;

	public static void main(String[] arg) {
		if (!lockInstance(System.getProperty("user.home") + File.separator + ".burningknight_lock")) {
			Log.error("Another instance of Burning Knight is already running, exiting");
			return;
		}

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				throwable.printStackTrace();
				Crash.report(thread, throwable);
				Gdx.app.exit();
			}
		});

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		Dungeon.title = "Burning Knight " + Version.asString() + ": " + generateTitle();

		config.title = Dungeon.title;
		config.width = Display.GAME_WIDTH * SCALE;
		config.height = Display.GAME_HEIGHT * SCALE;
		config.addIcon("icon.png", Files.FileType.Internal);
		config.addIcon("icon32x32.png", Files.FileType.Internal);
		config.addIcon("icon128x128.png", Files.FileType.Internal);
		config.resizable = true;
		config.samples = 2;
		config.backgroundFPS = 0;
		config.initialBackgroundColor = Color.BLACK;
		config.vSyncEnabled = true;

		// min size not avaible :(

		Dungeon.arg = arg;

		new LwjglApplication(new Client(), config) {
			@Override
			public void exit() {
				if (Assets.finishedLoading) {
					super.exit();
				}
			}
		};
	}

	private static String generateTitle() {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");

		if (Random.chance(0.001f)) {
			titles.add("This title will never appear, strange?");
		}

		if (Random.chance(0.01f)) {
			titles.add("You feel lucky");
		}

		String extra = titles.get(Random.newInt(titles.size()));

		Date now = new Date();
		Calendar current = Calendar.getInstance();

		try {
			for (String birthday : birthdays) {
				Date b = format.parse(birthday);

				Calendar birthdayCal = Calendar.getInstance();

				birthdayCal.setTime(b);
				current.setTime(now);

				boolean sameDay = birthdayCal.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR);

				if (sameDay) {
					extra = birthdayTitles[Random.newInt(birthdayTitles.length)];
					break;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			extra = "Houston, we have a problem!";
		}

		return extra;
	}

	private static boolean lockInstance(final String lockFile) {
		try {
			final File file = new File(lockFile);
			final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();

			if (fileLock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							fileLock.release();
							randomAccessFile.close();
							file.delete();
						} catch (Exception e) {
							e.printStackTrace();
							Log.error("Unable to remove lock file: " + lockFile);
						}
					}
				});

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Unable to create and/or lock file: " + lockFile);
		}

		return false;
	}

	private static ArrayList<String> titles = new ArrayList<>(Arrays.asList(
		"Fireproof",
		"Might burn",
		"'Friendly' fire",
		"Get ready to burn",
		"Do you need some heat?",
		"BBQ is ready!",
		"Hot sales!",
		"AAAAAA",
		"It burns burns burns",
		"Not for children under -1",
		"Unhandled fire",
		"Chili music",
		"Fire trap",
		"On-fire",
		"Hot potatoo",
		"Is this loss?",
		"Also try Enter the Gungeon - @ArutyunovPaul",
		"Also try Nuclear Throne",
		"undefined",
		"If only you could talk to the enemies...",
		"It slit. Thank you, good knight - @_kaassouffle",
		"Why aren't you in fullscreen? - @Brastin",
		"Burning Knight. It's what's for dinner - @MysticJemLP",
		"so fire, so good - @somepx",
		"Please don't feed the goblins - @BigastOon",
		"Burn, baby, burn - @Hairic_Lilred",
		"Only you can prevent the absence of forest fire - @Hairic_Lilred",
		"If at first you don't success, just burn everything - @Hairic_Lilred",
		"Attention: may cause sick burns - @Hairic_Lilred",
		"What doesn't make you stronger kills you - @Hairic_Lilred",
		"Is that a plane? Is that a bird? It is a flying fire! - @Hairic_Lilred",
		"Of course we still love you",
		"Just read the instructions",
		"Your CPU is close to overheating",
		"This is fine",
		"Ice cool",
		"Overheat alarm was triggered",
		"Burn",
		"Being a Knight does not guarantee burning - @wombatstuff",
		"Burning does not guarantee becoming a knight - @wombatstuff",
		"The thing of fire - @wombatstuff",
		"Sick burn, knight - @wombatstuff",
		"Wombats are dangerous",
		"Stuff happens",
		"1000% screen shake",
		"Water, fire, air, and... chocolate!",
		"What can be cooler than fire?",
		"???",
		"!!!",
		"Ya feel lucky",
		"Today is the day",
		"Run, gobbo, run!",
		"Oszor is watching you",
		"Always wear dry socks",
		"The walls are shifting, don't get squeezed! - @Hairic_Lilred",
		"Procgen? More like PRO-cgen! - @Hairic_Lilred",
		"Turn up your firewall! - @Hairic_Lilred",
		"Generating new generation - @Hairic_Lilred",
		"But can you procedurally generate this?! - @Hairic_Lilred",
		"But can you procedurally generate titles?",
		"Now with more Math.random() than ever! - @Hairic_Lilred",
		"Who needs static level design when you have math? - @Hairic_Lilred",
		"Too hot to handle - @viza",
		"Hot as hell - @viza",
		"Go for the burn! - @viza",
		"Never gets the cold feet - @viza",
		"If you play with fire... - @viza",
		"Now with more pathos than ever!",
		"Now with less bugs than ever!",
		"Now with more code than ever!",
		"There is a throne, however it's made of metal and not uranium... - @Xist3nce_Dev",
		"No, we're not associated with the flaming bear - @Xist3nce_Dev",
		"Eeeeeeeenttteeeeerrr the game... wait what did you think I was gonna say? - @Xist3nce_Dev",
		"There are no shovels in this one, I swear - @Xist3nce_Dev",
		"Не ожидали?",
		"Flameberrry",
		"No relation to Evolver Analog - @Xist3nce_Dev",
		"You are not allowed to die",
		"This is a title mimic",
		"Penguins will take over the world!",
		"Don't crash, don't crash, don't crash!!!",
		"Is this a batterfly?",
		"Lets play",
		"That's lit! - @JackerDeluxe",
		"Remains functional up to 4000 degrees Kelvin - @s_nnnikki",
		"Caution: might contain knights - @Eiyeron",
		"/!\\ Do not throw oil at the knight! - @TRASEVOL_DOG",
		"/!\\ /!\\ /!\\",
		"xD",
		"All your knights are belong to us - @Telecoda",
		"It's dangerous to go alone, take this!",
		"The early bird gets two in the bush - @Hybrid_Games_",
		"Please keep hands and feet inside the chaos at all times - @bwalter_indie",
		"You just got fired! - @brephenson",
		"Note: This game contains flammables - @brephenson",
		"We didn't start the fire - @brephenson",
		"I'm a slow burner - @dollarone",
		"I'm on fire! - @dollarone",
		"The Knight is lava  - @dollarone",
		"Hello fire, my old friend - @dollarone",
		"I'm here to talk with you again",
		"Hot n' spicy! - @dollarone",
		"GET OUT OF HERE!!! Or buy these DELICIOUS stones for 9.99 each - @DS100",
		"This it lit",
		"Вообще огонь",
		"0b11111100011",
		"0x7E3",
		"Take it easy",
		"Slow up, please",
		"Thy hath the sickest of burns! - @avivbeer",
		"Is this a game?",
		"Roses are red, violets are blue, water boils at 100°C and freezes at 0°C - @DSF100",
		"Burn CPU, BURN",
		"No one can defeat BK, why? READ THE LORE - @DSF100",
		"Please, read the instructions",
		":bok: - @DSF100",
		"BK stands for Bonkey Kong - @DSF100",
		"BK is a legendary enemy, I hope that you will defeat him in a legendary way! - @DSF100",
		"discord.gg/xhNbrtx", // FIXME: replace with proper link (rexcellent)
		"twitter.com/egordorichev",
		"twitter.com/rexcellentgames",
		"rexcellentgames.com",
		"Rex",
		"¯\\_(ツ)_/¯",
		"☉_☉",
		"⌐■-■"
	));

	private static String[] birthdayTitles = new String[] {
		"Happy burning!",
		"It's a good day to die hard",
		"Someone is not burning today",
		"Burning party",
		"Fire hard!",
		"Today is a special day",
		""
	};

	private static String[] birthdays = new String[]{
		"06-29", // Egor
		"09-25", // Mate
		"02-21" // Bibiki
	};
}