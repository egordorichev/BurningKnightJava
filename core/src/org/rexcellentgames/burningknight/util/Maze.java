package org.rexcellentgames.burningknight.util;

import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.util.geometry.Rect;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class Maze {
	public static boolean EMPTY = false;
	public static boolean FILLED = true;

	public static boolean[][] generate(Room r) {
		boolean[][] maze = new boolean[r.getWidth()][r.getHeight()];

		for (int x = 0; x < maze.length; x++) {
			for (int y = 0; y < maze[0].length; y++) {
				if (x == 0 || x == maze.length - 1 ||
					y == 0 || y == maze[0].length - 1) {

					maze[x][y] = FILLED;

				}
			}
		}

		//set spaces where there are doors
		for (Door d : r.getConnected().values()) {
			maze[(int) (d.x - r.left)][(int) (d.y - r.top)] = EMPTY;
		}

		return generate(maze);
	}

	public static boolean[][] generate(Rect r) {
		return generate(r.getWidth() + 1, r.getHeight() + 1);
	}

	public static boolean[][] generate(int width, int height) {
		return generate(new boolean[width][height]);
	}

	public static boolean[][] generate(boolean[][] maze) {
		int fails = 0;
		int x, y, moves;
		int[] mov;
		while (fails < 2500) {

			//find a random wall point
			do {
				x = Random.newInt(maze.length);
				y = Random.newInt(maze[0].length);
			} while (!maze[x][y]);

			//decide on how we're going to move
			mov = decideDirection(maze, x, y);
			if (mov == null) {
				fails++;
			} else {

				fails = 0;
				moves = 0;
				do {
					x += mov[0];
					y += mov[1];
					maze[x][y] = FILLED;
					moves++;
				} while (Random.newInt(moves) == 0 && checkValidMove(maze, x, y, mov));

			}

		}

		return maze;
	}

	private static int[] decideDirection(boolean[][] maze, int x, int y) {

		//attempts to move up
		if (Random.newInt(4) == 0 && //1 in 4 chance
			checkValidMove(maze, x, y, new int[]{0, -1})) {
			return new int[]{0, -1};
		}

		//attempts to move right
		if (Random.newInt(3) == 0 && //1 in 3 chance
			checkValidMove(maze, x, y, new int[]{1, 0})) {
			return new int[]{1, 0};
		}

		//attempts to move down
		if (Random.newInt(2) == 0 && //1 in 2 chance
			checkValidMove(maze, x, y, new int[]{0, 1})) {
			return new int[]{0, 1};
		}

		//attempts to move left
		if (
			checkValidMove(maze, x, y, new int[]{-1, 0})) {
			return new int[]{-1, 0};
		}

		return null;
	}

	private static boolean checkValidMove(boolean[][] maze, int x, int y, int[] mov) {
		int sideX = 1 - Math.abs(mov[0]);
		int sideY = 1 - Math.abs(mov[1]);

		//checking two tiles forward in the movement, and the tiles to their left/right
		for (int i = 0; i < 2; i++) {
			x += mov[0];
			y += mov[1];
			//checks if tiles we're examining are valid and open
			if (!(x > 0 && x < maze.length - 1 && y > 0 && y < maze[0].length - 1 && !maze[x][y] && !maze[x + sideX][y + sideY] && !maze[x - sideX][y - sideY])) {
				return false;
			}
		}
		return true;
	}
}