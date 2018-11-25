package org.rexcellentgames.burningknight.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
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

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowListener(new Lwjgl3WindowListener() {
			@Override
			public void created(Lwjgl3Window window) {

			}

			@Override
			public void iconified(boolean isIconified) {

			}

			@Override
			public void maximized(boolean isMaximized) {

			}

			@Override
			public void focusLost() {
				Dungeon.instance.pause();
			}

			@Override
			public void focusGained() {
				Dungeon.instance.resume();
			}

			@Override
			public boolean closeRequested() {
				return Assets.finishedLoading;
			}

			@Override
			public void filesDropped(String[] files) {

			}

			@Override
			public void refreshRequested() {

			}
		});

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

		Dungeon.title = "Burning Knight " + Version.asString() + ": " + extra;

		config.setTitle(Dungeon.title);
		config.setWindowedMode(Display.GAME_WIDTH * SCALE, Display.GAME_HEIGHT * SCALE);
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 2);
		config.setWindowSizeLimits(Display.GAME_WIDTH, Display.GAME_HEIGHT, 1000000000, 10000000);
		config.setWindowIcon("icon.png", "icon32x32.png", "icon128x128.png");
		config.setIdleFPS(0);
		config.setInitialBackgroundColor(Color.BLACK);

		Dungeon.arg = arg;

		new Lwjgl3Application(new Client(), config);
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
		"Is this loss?"
	));

	private static String[] birthdayTitles = new String[]{
		"Happy burning!",
		"It's a good day to die hard",
		"Someone is not burning today",
		"Burning party",
		"Fire hard!"
	};

	private static String[] birthdays = new String[]{
		"06-29", // Egor
		"09-25", // Mate
		"06-10", // Nufflee
		"02-21" // Bibiki
	};
}