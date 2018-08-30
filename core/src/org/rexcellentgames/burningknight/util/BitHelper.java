package org.rexcellentgames.burningknight.util;

public class BitHelper {
	public static boolean isBitSet(int val, int pos) {
		return (val & (1L << pos)) != 0;
	}

	public static int setBit(int val, int pos, boolean set) {
		if (set) {
			return (val | 1 << pos);
		}

		return (val & ~(1 << pos));
	}

	public static int getBit(int data, int bit) {
		return (data >> bit) & 1;
	}

	public static int putNumber(int data, int bit, int size, int number) {
		for (int i = 0; i < size; i++) {
			data = setBit(data, bit + i, isBitSet(number, i));
		}

		return data;
	}

	public static int getNumber(int data, int bit, int size) {
		int num = 0;

		for (int i = 0; i < size; i++) {
			num += getBit(data, bit + i) << i;
		}

		return num;
	}
}