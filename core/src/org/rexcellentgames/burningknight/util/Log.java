package org.rexcellentgames.burningknight.util;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.debug.Console;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Log {
	public static final boolean ENABLE_PHYSICS_MESSAGES = false;
	public static final boolean UI_DEBUG_WINDOW = false;

	private static JTextArea area;
	private static JFrame frame;

	public static void report(Throwable t) {
		if (UI_DEBUG_WINDOW) {
			area.append("Exception: " + t.getMessage() + "\n" + t.getCause() + "\n");
			frame.getContentPane().validate();
		}

		t.printStackTrace();
		force = true;
		close();
		init();
	}

	public static void printStackTrace() {
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			Log.error(s.toString());
		}
	}

	public static void init() {
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

			final JTextField field = new JTextField();
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

	private static boolean force;

	public static void close() {
		if (frame != null) {
			if (force) {
				force = false;
			} else {
				new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							frame.setVisible(false);
							System.exit(0);
						}
					},
					5000
				);
			}
		}
	}

	public static void error(Object string) {
		if (UI_DEBUG_WINDOW) {
			area.append("ERROR: " + string + "\n");
			frame.getContentPane().validate();
		}

		if (!Version.debug) {
			return;
		}

		try {
			System.out.println("\u001B[31m" + string + "\u001B[0m");
		} catch (Exception e) {

		}
	}

	public static void info(Object string) {
		if (UI_DEBUG_WINDOW) {
			area.append(string + "\n");
			frame.getContentPane().validate();
		}

		try {
			System.out.println("\u001B[32m" + string + "\u001B[0m");
		} catch (Exception e) {

		}
	}

	public static void physics(Object string) {
		if (!ENABLE_PHYSICS_MESSAGES || !Version.debug) {
			return;
		}

		if (UI_DEBUG_WINDOW) {
			area.append("Physics: " + string + "\n");
			frame.getContentPane().validate();
		}

		try {
			System.out.println("\u001B[34m" + string + "\u001B[0m");
		} catch (Exception e) {

		}
	}
}