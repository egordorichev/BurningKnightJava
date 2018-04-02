package org.rexellentgames.dungeon.util;

import org.rexellentgames.dungeon.Dungeon;

import java.util.Arrays;
import java.util.LinkedList;

public class PathFinder {
	public static float[] distance;

	private static boolean[] goals;
	private static int[] queue;

	private static int size = 0;

	private static int[] dir;
	private static int[][] dirCheck;

	public static void setMapSize(int width, int height) {
		int size = width * height;

		if (PathFinder.size != size) {

			PathFinder.size = size;
			distance = new float[size];
			goals = new boolean[size];
			queue = new int[size];

			dir = new int[]{-1, +1, -width, +width, -width - 1, -width + 1, +width - 1, +width + 1};
			dirCheck = new int[][]{{}, {}, {}, {}, {-1, -width}, {-width, 1}, {-1, +width}, {+width, 1}};

			// dir = new int[]{-1, +1, -width, +width};
		}
	}

	public static Path find(int from, int to, boolean[] passable, boolean light) {
		if (!buildDistanceMap(from, to, passable, light)) {
			return null;
		}

		Path result = new Path();
		int s = from;

		// From the starting position we are moving downwards,
		// until we reach the ending point
		do {
			float minD = distance[s];
			int mins = s;

			for (int i = 0; i < dir.length; i++) {

				int n = s + dir[i];

				boolean good = true;

				for (int check : dirCheck[i]) {
					int k = n + check;

					if (k >= 0 && k < size && !passable[k]) {
						good = false;

					}
				}

				if (good) {
					float thisD = distance[n];
					if (thisD < minD) {
						minD = thisD;
						mins = n;
					}
				}
			}
			s = mins;
			result.add(s);
		} while (s != to);

		return result;
	}

	public static int getStep(int from, int to, boolean[] passable, boolean light) {

		if (!buildDistanceMap(from, to, passable, light)) {
			return -1;
		}

		// From the starting position we are making one step downwards
		float minD = distance[from];
		int best = from;

		float step, stepD;

		for (int i = 0; i < dir.length; i++) {
			int index = (int) (step = from + dir[i]);
			if (index >= 0 && index < distance.length && (stepD = distance[index]) < minD) {
				boolean good = true;

				for (int check : dirCheck[i]) {
					int k = index + check;

					if (k >= 0 && k < size && !passable[k]) {
						good = false;
						
					}
				}

				if (good) {

					minD = stepD;
					best = (int) step;
				}
			}
		}

		return best;
	}

	public static int getStepBack(int cur, int from, boolean[] passable) {

		float d = buildEscapeDistanceMap(cur, from, 2f, passable);
		for (int i = 0; i < size; i++) {
			goals[i] = distance[i] == d;
		}
		if (!buildDistanceMap(cur, goals, passable)) {
			return -1;
		}

		int s = cur;

		// From the starting position we are making one step downwards
		float minD = distance[s];
		int mins = s;

		for (int i = 0; i < dir.length; i++) {

			int n = s + dir[i];
			float thisD = distance[n];

			if (thisD < minD) {
				minD = thisD;
				mins = n;
			}
		}

		return mins;
	}

	public static boolean buildDistanceMap(int from, int to, boolean[] passable, boolean light) {
		if (from == to) {
			return false;
		}

		Arrays.fill(distance, Integer.MAX_VALUE);

		boolean pathFound = false;

		int head = 0;
		int tail = 0;

		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;

		while (head < tail) {

			// Remove from queue
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}

			float nextDistance = distance[step] + (light ? Math.max(0.1f, 10f * Dungeon.level.getLight(step)) : 1f);

			for (int i = 0; i < dir.length; i++) {
				int n = step + dir[i];
				if (n >= 0 && n < distance.length && (n == from || (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)))) {
					boolean good = true;

					for (int check : dirCheck[i]) {
						int k = n + check;

						if (k >= 0 && k < size && !passable[k]) {
							good = false;
							
						}
					}

					if (good) {
						if (tail > queue.length) {
							return false;
						}

						if (tail++ >= queue.length - 1) {
							return false;
						}

						// Add to queue
						queue[tail] = n;
						distance[n] = nextDistance;
					}
				}
			}
		}

		return pathFound;
	}

	public static void buildDistanceMap(int to, boolean[] passable, int limit) {

		Arrays.fill(distance, Integer.MAX_VALUE);

		int head = 0;
		int tail = 0;

		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;

		while (head < tail) {

			// Remove from queue
			int step = queue[head++];

			float nextDistance = distance[step] + 1;
			if (nextDistance > limit) {
				return;
			}

			for (int i = 0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)) {
					boolean good = true;

					for (int check : dirCheck[i]) {
						int k = n + check;

						if (k >= 0 && k < size && !passable[k]) {
							good = false;
							
						}
					}

					if (good) {
						// Add to queue
						queue[tail++] = n;
						distance[n] = nextDistance;
					}
				}

			}
		}
	}

	public static boolean buildDistanceMap(int from, boolean[] to, boolean[] passable) {

		if (to[from]) {
			return false;
		}

		Arrays.fill(distance, Integer.MAX_VALUE);

		boolean pathFound = false;

		int head = 0;
		int tail = 0;

		// Add to queue
		for (int i = 0; i < size; i++) {
			if (to[i]) {
				queue[tail++] = i;
				distance[i] = 0;
			}
		}

		while (head < tail) {

			// Remove from queue
			int step = queue[head++];
			if (step == from) {
				pathFound = true;
				break;
			}
			float nextDistance = distance[step] + 1;

			for (int i = 0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n == from || (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance))) {
					boolean good = true;

					for (int check : dirCheck[i]) {
						int k = n + check;

						if (k >= 0 && k < size && !passable[k]) {
							good = false;
							
						}
					}

					if (good) {
						// Add to queue
						queue[tail++] = n;
						distance[n] = nextDistance;
					}
				}

			}
		}

		return pathFound;
	}

	public static float buildEscapeDistanceMap(int cur, int from, float factor, boolean[] passable) {

		Arrays.fill(distance, Integer.MAX_VALUE);

		int destDist = Integer.MAX_VALUE;

		int head = 0;
		int tail = 0;

		// Add to queue
		queue[tail++] = from;
		distance[from] = 0;

		float dist = 0;

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

			float nextDistance = dist + 1;

			for (int i = 0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n >= 0 && n < size && passable[n] && distance[n] > nextDistance) {
					boolean good = true;

					for (int check : dirCheck[i]) {
						int k = n + check;

						if (k >= 0 && k < size && !passable[k]) {
							good = false;
							
						}
					}

					if (good) {
						// Add to queue
						queue[tail++] = n;
						distance[n] = nextDistance;
					}
				}

			}
		}

		return dist;
	}

	public static void buildDistanceMap(int to, boolean[] passable) {

		Arrays.fill(distance, Integer.MAX_VALUE);

		int head = 0;
		int tail = 0;

		// Add to queue
		queue[tail++] = to;
		distance[to] = 0;

		while (head < tail) {

			// Remove from queue
			int step = queue[head++];
			float nextDistance = distance[step] + 1;

			for (int i = 0; i < dir.length; i++) {

				int n = step + dir[i];
				if (n >= 0 && n < size && passable[n] && (distance[n] > nextDistance)) {
					boolean good = true;

					for (int check : dirCheck[i]) {
						int k = n + check;

						if (k >= 0 && k < size && !passable[k]) {
							good = false;
							
						}
					}

					if (good) {
						// Add to queue
						queue[tail++] = n;
						distance[n] = nextDistance;
					}
				}

			}
		}
	}

	public static class Path extends LinkedList<Integer> {

	}
}