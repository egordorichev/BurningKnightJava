package org.rexcellentgames.burningknight.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
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

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		/*config.setWindowListener(new Lwjgl3WindowListener() {
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
		});*/

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

		config.title = Dungeon.title;
		config.samples = 4;
		config.width = Display.GAME_WIDTH * SCALE;
		config.height = Display.GAME_HEIGHT * SCALE;
		config.addIcon("icon.png", Files.FileType.Internal);
		config.addIcon("icon32x32.png", Files.FileType.Internal);
		config.addIcon("icon128x128.png", Files.FileType.Internal);

		Dungeon.arg = arg;

		new LwjglApplication(new Client(), config);
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