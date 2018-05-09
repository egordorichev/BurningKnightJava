package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.UiLog;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	public static final boolean ENABLE_PHYSICS_MESSAGES = false;
	public static final boolean UI_DEBUG_WINDOW = true;
	private static FileWriter file;
	public static boolean UI_LOG = false;

	private static JTextArea area;
	private static JScrollPane pane;
	private static JFrame frame;

	public static void report(Throwable t) {
		try {
			file.write("Exception:");
			file.write(t.getMessage() + "\n" + t.getCause());
		} catch (IOException e) {
			e.printStackTrace();
		}

		t.printStackTrace();
	}

	public static void init() {
		try {
			file = new FileWriter(Gdx.files.external("bk.log").file());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (UI_DEBUG_WINDOW) {
			frame = new JFrame();
			frame.setSize(Display.GAME_WIDTH, Display.GAME_HEIGHT * 2);
			area = new JTextArea();
			pane = new JScrollPane(area);

			frame.getContentPane().add(pane);
			frame.setVisible(true);
		}
	}

	public static void close() {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void error(String string) {
		try {
			file.write("[ERROR]: " + string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (UiLog.instance != null && UI_LOG) {
			UiLog.instance.print(string);
		}

		if (UI_DEBUG_WINDOW) {
			area.append("ERROR: " + string + "\n");
			frame.getContentPane().validate();
		}

		System.out.println("\u001B[31m" + string + "\u001B[0m");
	}

	public static void info(String string) {
		try {
			file.write(string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (UiLog.instance != null && UI_LOG) {
			UiLog.instance.print(string);
		}

		if (UI_DEBUG_WINDOW) {
			area.append(string + "\n");
			frame.getContentPane().validate();
		}

		System.out.println("\u001B[32m" + string + "\u001B[0m");
	}

	public static void physics(String string) {
		if (!ENABLE_PHYSICS_MESSAGES) {
			return;
		}

		try {
			file.write("Physics: " + string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (UI_DEBUG_WINDOW) {
			area.append("Physics: " + string + "\n");
			frame.getContentPane().validate();
		}

		System.out.println("\u001B[34m" + string + "\u001B[0m");
	}
}