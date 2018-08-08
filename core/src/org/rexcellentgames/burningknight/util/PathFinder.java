package org.rexcellentgames.burningknight.util;

import java.util.Arrays;

public class PathFinder {

	public static int[] distance;
	//performance-light shortcuts for some common pathfinder cases
	//they are in array-access order for increased memory performance
	public static int[] NEIGHBOURS4;
	public static int[] NEIGHBOURS8;
	public static int[] NEIGHBOURS9;
	public static int[][] check;
	public static int[][] checkLR;

	//similar to their equivalent neighbour arrays, but the order is clockwise.
	//Useful for some logic functions, but is slower due to lack of array-access order.
	public static int[] CIRCLE4;
	public static int[] CIRCLE8;
	private static int[] maxVal;
	private static boolean[] goals;
	private static int[] queue;
	private static int size = 0;
	private static int width = 0;
	private static int[] dir;
	private static int[] dirLR;

	public static void setMapSize(int width, int height) {

		PathFinder.width = width;
		PathFinder.size = width * height;

		distance = new int[size];
		goals = new boolean[size];
		queue = new int[size];

		maxVal = new int[size];
		Arrays.fill(maxVal, Integer.MAX_VALUE);

		dir = new int[]{-1, +1, -width, +width, -width - 1, -width + 1, +width - 1, +width + 1};
		dirLR = new int[]{-1 - width, -1, -1 + width, -width, +width, +1 - width, +1, +1 + width};

		NEIGHBOURS4 = new int[]{-width, -1, +1, +width};
		NEIGHBOURS8 = new int[]{-width - 1, -width, -width + 1, -1, +1, +width - 1, +width, +width + 1};
		NEIGHBOURS9 = new int[]{-width - 1, -width, -width + 1, -1, 0, +1, +width - 1, +width, +width + 1};

		check = new int[][]{
			{}, {}, {}, {},
			{-width, -1}, {-width, 1}, {width, -1}, {width, 1}
		};

		checkLR = new int[][]{
			{-1, -width}, {}, {-1, width}, {},
			{}, {-width, 1}, {width, +1}, {}
		};

		CIRCLE4 = new int[]{-width, +1, +width, -1};
		CIRCLE8 = new int[]{-width - 1, -width, -width + 1, +1, +width + 1, +width, +width - 1, -1};
	}

	public static boolean goodMove(int from, int i, boolean[] passable) {
		/*for (int j = 0; j < check[i].length; j++) {
			if (!passable[from + check[i][j]]) {
				return false;
			}
		}*/

		return true;
	}

	public static boolean goodMoveLR(int from, int i, boolean[] passable) {
		/*for (int j = 0; j < checkLR[i].length; j++) {
			if (!passable[from + checkLR[i][j]]) {
				return false;
			}
		}*/

		return true;
	}

	public static int getStep(int from, int to, boolean[] passable) {
		if (!buildDistanceMap(from, to, passable)) {
			return -1;
		}

		// From the starting position we are making one step downwards
		int minD = distance[from];
		int best = from;

		int step, stepD;

		for (int i = 0; i < dir.length; i++) {
			if ((stepD = distance[step = from + dir[i]]) < minD && goodMove(step, i, passable)) {
				minD = stepD;
				best = step;
			}
		}

		return best;
	}

	public static int getStepBack(int cur, int from, boolean[] passable) {
		int d = buildEscapeDistanceMap(cur, from, 2f, passable);

		for (int i = 0; i < size; i++) {
			goals[i] = distance[i] == d;
		}

		if (!buildDistanceMap(cur, goals, passable)) {
			return -1;
		}

		int s = cur;

		int minD = distance[s];
		int mins = s;

		for (int i = 0; i < dir.length; i++) {
			int n = s + dir[i];
			int thisD = distance[n];

			if (goodMove(from, i, passable) && thisD < minD) {
				minD = thisD;
				mins = n;
			}
		}

		return mins;
	}

	private static boolean buildDistanceMap(int from, int to, boolean[] passable) {
		if (from == to) {
			return false;
		}

		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);

		boolean pathFound = false;

		int head = 0;
		int tail = 0;

		queue[tail++] = to;
		distance[to] = 0;

		while (head < tail) {
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}

			int nextDistance = distance[step] + 1;
			int start = (step % width == 0 ? 3 : 0);
			int end = ((step + 1) % width == 0 ? 3 : 0);

			for (int i = start; i < dirLR.length - end; i++) {
				int n = step + dirLR[i];

				if ((n == from || (n >= 0 && n < size && passable[n] && goodMoveLR(step, i, passable) && (distance[n] > nextDistance)))) {
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
			}
		}

		return pathFound;
	}

	private static boolean buildDistanceMap(int from, boolean[] to, boolean[] passable) {
		if (to[from]) {
			return false;
		}

		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);

		boolean pathFound = false;

		int head = 0;
		int tail = 0;

		for (int i = 0; i < size; i++) {
			if (to[i]) {
				queue[tail++] = i;
				distance[i] = 0;
			}
		}

		while (head < tail) {
			int step = queue[head++];

			if (step == from) {
				pathFound = true;
				break;
			}

			int nextDistance = distance[step] + 1;

			int start = (step % width == 0 ? 3 : 0);
			int end = ((step + 1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {
				int n = step + dirLR[i];

				if ((n == from || (n >= 0 && n < size && passable[n] && goodMoveLR(step, i, passable) && (distance[n] > nextDistance)))) {
					queue[tail++] = n;
					distance[n] = nextDistance;
				}
			}
		}

		return pathFound;
	}

	private static int buildEscapeDistanceMap(int cur, int from, float factor, boolean[] passable) {
		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);

		int destDist = Integer.MAX_VALUE;

		int head = 0;
		int tail = 0;

		// Add to queue
		queue[tail++] = from;
		distance[from] = 0;

		int dist = 0;

		while (head < tail) {

			// Remove from queue
			int step = queue[head++];
			dist = distance[step];

			if (dist > destDist) {
				return destDist;
			}

			if (step == cur) {
				destDist = (int) (dist * factor) + 1;
			}

			int nextDistance = dist + 1;

			int start = (step % width == 0 ? 3 : 0);
			int end = ((step + 1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {

				int n = step + dirLR[i];
				if (n >= 0 && n < size && passable[n] && goodMoveLR(step, i, passable) && distance[n] > nextDistance) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}

			}
		}

		return dist;
	}

	public static void buildDistanceMap(int to, boolean[] passable) {

		System.arraycopy(maxVal, 0, distance, 0, maxVal.length);

		int head = 0;
		int tail = 0;

		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;

		while (head < tail) {

			// Remove from queue
			int step = queue[head++];
			int nextDistance = distance[step] + 1;

			int start = (step % width == 0 ? 3 : 0);
			int end = ((step + 1) % width == 0 ? 3 : 0);
			for (int i = start; i < dirLR.length - end; i++) {

				int n = step + dirLR[i];
				if (n >= 0 && n < size && passable[n] && goodMoveLR(step, i, passable) && (distance[n] > nextDistance)) {
					// Add to queue
					queue[tail++] = n;
					distance[n] = nextDistance;
				}

			}
		}
	}
}