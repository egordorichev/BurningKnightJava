package org.rexcellentgames.burningknight;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class Crash {
  private static String ERROR_MESSAGE = "BurningKnight had stopped running, because it encountered a problem.\n"
    + "\nPlease, copy and report the entire text to email egordorichev@gmail.com or\n"
    + "DM egordorichev on twitter (https://twitter.com/egordorichev)\n\n";

  public static void report(Thread thread, Throwable throwable) {
    StringBuilder builder = new StringBuilder();

    builder.append("BurningKnight had crashed!\n-------------------\n");
    builder.append(ERROR_MESSAGE);

    builder.append("--- BEGIN CRASH REPORT ---");
    builder.append("\nBurningKnight version: ").append(Version.asString());
    builder.append("\nOS: ").append(System.getProperty("os.name")).append(" (").append(System.getProperty("os.arch")).append(") version ").append(System.getProperty("os.version"));
    builder.append("\nJava version: ").append(System.getProperty("java.version")).append(", ").append(System.getProperty("java.vendor"));
    builder.append("\nJava VM version: ").append(System.getProperty("java.vm.name")).append(" (").append(System.getProperty("java.vm.info")).append("), ").append(System.getProperty("java.vm.vendor"));
    builder.append("\nJava VM flags: ").append(getJavaVMFlags());
    builder.append("\nMemory: ").append(getMemoryUsage());
    builder.append("\n--- Exception cause: ---\n");

    StringWriter writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    throwable.printStackTrace();

    builder.append(writer.toString());
    builder.append("\n--- END CRASH REPORT ---\n");

    JTextArea text = new JTextArea();

    text.setText(builder.toString());
    text.setEditable(false);

    JOptionPane.showMessageDialog(null, new JScrollPane(text), "Burning Knight crash report", JOptionPane.ERROR_MESSAGE);
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

    for (String s: list) {
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