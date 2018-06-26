package org.rexcellentgames.burningknight;

public class Version {
  public static boolean debug = true;
  public static double major = 0.0;
  public static double minor = 15.3;
  public static String string = asString();

  public static String asString() {
    return "v" + major + "." + minor;
  }
}