package org.rexcellentgames.burningknight.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import org.rexcellentgames.burningknight.*;
import org.rexcellentgames.burningknight.util.Random;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DesktopLauncher {
	private static final int SCALE = 2;

	public static void main(String[] arg) {
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
				return true;
			}

			@Override
			public void filesDropped(String[] files) {

			}

			@Override
			public void refreshRequested() {

			}
		});

		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String extra = titles[Random.newInt(titles.length - 1)];
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
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
		config.setWindowSizeLimits(Display.GAME_WIDTH, Display.GAME_HEIGHT, 1000000000, 10000000);
		config.setWindowIcon("icon.png", "icon32x32.png", "icon128x128.png");

		Dungeon.arg = arg;

		new Lwjgl3Application(new Client(), config);
	}

	private static String[] titles = new String[] {
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

		"This title will never appear, strange?"
	};

	private static String[] birthdayTitles = new String[] {
		"Happy burning!",
		"It's a good day to die hard",
		"Someone is not burning today",
		"Burning party",
		"Fire hard!"
	};

	private static String[] birthdays = new String[] {
		"06-29", // Egor
		"09-25", // Mate
		"06-10", // Nufflee
		"02-21" // Bibiki
	};
}