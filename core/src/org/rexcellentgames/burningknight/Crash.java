package org.rexcellentgames.burningknight;

import com.badlogic.gdx.Gdx;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.TList;
import com.julienvey.trello.impl.TrelloImpl;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.List;

public class Crash {
	private static final String ERROR_MESSAGE = "BurningKnight had stopped running, because it encountered a problem.\n"
		+ "\nThis crash was automatically reported to rexcellent games team.\n\n";

	public static void report(Thread thread, Throwable throwable) {
		StringBuilder builder = new StringBuilder();

		builder.append("BurningKnight had crashed!\n-------------------\n");
		builder.append(ERROR_MESSAGE);

		builder.append("--- BEGIN CRASH REPORT ---");
		builder.append("\nBurningKnight version: ").append(Version.asString());
		builder.append("\nDate: ").append(new Date().toString());
		builder.append("\nOS: ").append(System.getProperty("os.name")).append(" (").append(System.getProperty("os.arch")).append(") version ").append(System.getProperty("os.version"));
		builder.append("\nJava version: ").append(System.getProperty("java.version")).append(", ").append(System.getProperty("java.vendor"));
		builder.append("\nJava VM version: ").append(System.getProperty("java.vm.name")).append(" (").append(System.getProperty("java.vm.info")).append("), ").append(System.getProperty("java.vm.vendor"));
		builder.append("\nJava VM flags: ").append(getJavaVMFlags());
		builder.append("\nMemory: ").append(getMemoryUsage());
		builder.append("\nRandom seed: ").append(Random.getSeed());
		builder.append("\n--- Exception cause: ---\n");

		StringWriter writer = new StringWriter();
		throwable.printStackTrace(new PrintWriter(writer));
		throwable.printStackTrace();

		builder.append(writer.toString());
		builder.append("\n--- END CRASH REPORT ---\n");

		if (!Version.debug) {
			Trello trelloApi = new TrelloImpl("7e84b78076780d10a2c6a1905c69c6e9", "2695b451bd169f26fc16319500d3bf08eb479ae76794954aff4d90a204814419");

			Board board = trelloApi.getBoard("ve32nwEC");
			List<TList> lists = board.fetchLists();
			TList reports = null;

			for (TList list : lists) {
				if (list.getName().equals("Crash reports")) {
					reports = list;
					break;
				}
			}

			if (reports == null) {
				Log.error("Reports is null");
			} else {
				Card card = new Card();

				card.setName("Crash on " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
				card.setDesc(builder.toString());

				reports.createCard(card);
			}
		}

		JTextArea text = new JTextArea();

		text.setText(builder.toString());
		text.setEditable(false);

		JScrollPane pane = new JScrollPane(text);

		pane.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {

			}

			@Override
			public void componentMoved(ComponentEvent componentEvent) {

			}

			@Override
			public void componentShown(ComponentEvent componentEvent) {

			}

			@Override
			public void componentHidden(ComponentEvent componentEvent) {
				Log.info("Exit from crash report");
				Gdx.app.exit();
			}
		});

		JOptionPane.showMessageDialog(null, pane, "Burning Knight crash report", JOptionPane.ERROR_MESSAGE);
	}

	private static String getMemoryUsage() {
		Runtime runtime = Runtime.getRuntime();

		long i = runtime.maxMemory();
		long j = runtime.totalMemory();
		long k = runtime.freeMemory();
		long l = i / 1024L / 1024L;
		long i1 = j / 1024L / 1024L;
		long j1 = k / 1024L / 1024L;

		return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
	}

	private static String getJavaVMFlags() {
		RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
		List<String> list = runtimemxbean.getInputArguments();
		int i = 0;
		StringBuilder stringbuilder = new StringBuilder();

		for (String s : list) {
			if (s.startsWith("-X")) {
				if (i++ > 0) {
					stringbuilder.append(" ");
				}

				stringbuilder.append(s);
			}
		}

		return String.format("%d total; %s", i, stringbuilder.toString());
	}
}