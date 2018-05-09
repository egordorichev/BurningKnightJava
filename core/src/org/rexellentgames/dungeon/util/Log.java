package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.debug.Console;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
	public static final boolean ENABLE_PHYSICS_MESSAGES = false;
	public static final boolean UI_DEBUG_WINDOW = true;
	private static FileWriter file;
	public static boolean UI_LOG = false;

	private static JTextArea area;
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
			frame.setVisible(true);

			JPanel panel = new JPanel();
			panel.setMinimumSize(new Dimension(Display.GAME_WIDTH, Display.GAME_HEIGHT * 2));
			panel.setLayout(new BorderLayout(0, 0));

			area = new JTextArea();
			area.setWrapStyleWord(true);
			area.setEditable(false);
			area.setLineWrap(true);
			panel.add(area, BorderLayout.PAGE_START);

			JTextField field = new JTextField();
			panel.add(field);

			field.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (Console.instance != null) {
						Console.instance.runCommand("/" + actionEvent.getActionCommand());
					} else {
						Log.info("Console is not here yet");
					}

					field.setText("");
				}
			});

			frame.add(panel, BorderLayout.PAGE_END);
		}
	}

	public static void close() {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (frame != null) {
			new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						frame.setVisible(false);
					}
				},
				5000
			);
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