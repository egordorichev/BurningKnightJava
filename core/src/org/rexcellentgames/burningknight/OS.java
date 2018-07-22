package org.rexcellentgames.burningknight;

public class OS {
	public static boolean linux;
	public static boolean windows;
	public static boolean macos;

	{
		String name = System.getProperty("os.name");

		if (name.contains("linux")) {
			linux = true;
		} else if (name.contains("windows")) {
			windows = true;
		} else {
			macos = true;
		}
	}
}