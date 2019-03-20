package org.rexcellentgames.burningknight.util;

public class Utils {
	public static String pascalCaseToSnakeCase(String string) {
		return string.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
	}
}