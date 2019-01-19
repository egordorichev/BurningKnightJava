package org.rexcellentgames.burningknight.entity.level.painters;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.fx.Firefly;
import org.rexcellentgames.burningknight.entity.item.key.BurningKey;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Patch;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Bush;
import org.rexcellentgames.burningknight.entity.level.entities.decor.Cobweb;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.levels.creep.CreepLevel;
import org.rexcellentgames.burningknight.entity.level.levels.forest.ForestLevel;
import org.rexcellentgames.burningknight.entity.level.levels.hall.HallLevel;
import org.rexcellentgames.burningknight.entity.level.levels.ice.IceLevel;
import org.rexcellentgames.burningknight.entity.level.levels.tech.TechLevel;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.BossEntranceRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class Painter {
	private float grass = 0f;
	private float dirt = 0f;
	private float water = 0f;
	private float cobweb = 0f;

	public Painter setWater(float v) {
		this.water = v;
		return this;
	}

	public Painter setCobweb(float v) {
		this.cobweb = v;
		return this;
	}

	public Painter setDirt(float v) {
		this.dirt = v;
		return this;
	}

	public Painter setGrass(float v) {
		this.grass = v;
		return this;
	}

	public void paint(Level level, ArrayList<Room> rooms) {
		if (rooms == null) {
			return;
		}

		int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;

		for (Room r : rooms) {
			if (r.left < leftMost) leftMost = r.left;
			if (r.top < topMost) topMost = r.top;
		}

		//subtract 1 for padding
		leftMost--;
		topMost--;

		int sz = 0;

		leftMost -= sz;
		topMost -= sz;

		int rightMost = 0, bottomMost = 0;

		for (Room r : rooms) {
			r.shift(-leftMost, -topMost);
			if (r.right > rightMost) rightMost = r.right;
			if (r.bottom > bottomMost) bottomMost = r.bottom;
		}

		//add 1 for padding
		rightMost++;
		bottomMost++;

		rightMost += sz;
		bottomMost += sz;

		//add 1 to account for 0 values
		Log.info("Setting level size to " + (1 + rightMost) + ":" +
			(bottomMost + 1));
		Level.setSize(rightMost + 1, bottomMost + 1);
		level.generateDecor();
		level.fill();

		for (Room room : rooms) {
			this.placeDoors(room);
			room.paint(level);

			if ((level instanceof HallLevel || level instanceof IceLevel) && Dungeon.depth > -1) {
				for (int y = room.top; y <= room.bottom; y++) {
					for (int x = room.left; x <= room.right; x++) {
						int i = Level.toIndex(x, y);

						if (level.liquidData[i] == Terrain.LAVA) {
							level.liquidData[i] = 0;
							level.set(i, Terrain.CHASM);
						}
					}
				}
			}

			if ((level instanceof IceLevel)) {
				for (int y = room.top; y <= room.bottom; y++) {
					for (int x = room.left; x <= room.right; x++) {
						int i = Level.toIndex(x, y);

						if (level.liquidData[i] == Terrain.WATER) {
							level.liquidData[i] = 0;
							level.set(i, Terrain.ICE);
						}
					}
				}
			}

			if (!(room instanceof BossEntranceRoom) && level instanceof ForestLevel && Random.chance(70)) {
				for (int i = 0; i < Random.newInt(1, 4); i++) {
					Point point = room.getRandomFreeCell();

					if (point != null) {
						Bush bush = new Bush();

						bush.x = point.x * 16 + Random.newFloat(-4, 4);
						bush.y = point.y * 16 + Random.newFloat(-4, 4);

						Dungeon.area.add(bush.add());
					}
				}
			}

			if (room.hidden) {
				for (int y = room.top; y <= room.bottom; y++) {
					for (int x = room.left; x <= room.right; x++) {
						level.hide(x, y);
					}
				}
			}
		}

		if (PathFinder.NEIGHBOURS8 == null) {
			PathFinder.setMapSize(Level.getWidth(), Level.getHeight());
		}

		if (Dungeon.depth > -1) {
			if (this.dirt > 0) {
				this.paintDirt(level, rooms);
			}

			if (this.grass > 0) {
				this.paintGrass(level, rooms);
			}

			if (this.cobweb > 0) {
				this.paintCobweb(level, rooms);
			}

			if (this.water > 0) {
				this.paintWater(level, rooms);
			}
		}

		this.decorate(level, rooms);
		this.paintDoors(level, rooms);
	}

	private void paintWater(Level level, ArrayList<Room> rooms) {
		boolean[] lake = Patch.generate(this.water, 5);
		boolean ice = level instanceof IceLevel;

		for (Room r : rooms) {
			for (Point p : r.waterPlaceablePoints()) {
				int i = Level.toIndex((int) p.x, (int) p.y);
				byte t = level.data[i];
				if (lake[i] && (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C) && level.liquidData[i] == 0) {
					level.set(i, ice ? Terrain.ICE : Terrain.WATER);
				}
			}
		}
	}

	private void paintCobweb(Level level, ArrayList<Room> rooms) {
		boolean[] lake = Patch.generate(this.water, 5);

		for (Room r : rooms) {
			for (Point p : r.waterPlaceablePoints()) {
				int i = Level.toIndex((int) p.x, (int) p.y);
				byte t = level.data[i];
				if (lake[i] && (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C) && level.liquidData[i] == 0) {
					level.set(i, Terrain.COBWEB);
				}
			}
		}
	}

	private void paintDirt(Level level, ArrayList<Room> rooms) {
		boolean[] grass = Patch.generate(this.dirt, 5);

		for (Room r : rooms) {
			for (Point p : r.grassPlaceablePoints()) {
				int i = Level.toIndex((int) p.x, (int) p.y);
				byte t = level.data[i];
				if (grass[i] && (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C) && level.liquidData[i] == 0) {
					level.set(i, Terrain.DIRT);
				}
			}
		}
	}

	private void paintGrass(Level level, ArrayList<Room> rooms) {
		boolean[] grass = Patch.generate(this.grass, 5);
		boolean[] dry = Patch.generate(this.grass, 5);
		ArrayList<Integer> cells = new ArrayList<>();

		for (Room r : rooms) {
			for (Point p : r.grassPlaceablePoints()) {
				int i = Level.toIndex((int) p.x, (int) p.y);
				byte t = level.data[i];
				if (grass[i] && (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C) && level.liquidData[i] == 0) {
					cells.add(i);
				}
			}
		}

		for (int i : cells) {
			int count = 1;

			for (int n : PathFinder.NEIGHBOURS8) {
				int k = i + n;

				if (Level.isValid(k) && grass[k]) {
					count++;
				}
			}

			boolean high = (Random.newFloat() < count / 12f);

			// false = dry[i]
			level.set(i, false ? (high ? Terrain.HIGH_DRY_GRASS : Terrain.DRY_GRASS) : (high ? Terrain.HIGH_GRASS : Terrain.GRASS));
		}
	}

	protected void decorate(Level level, ArrayList<Room> rooms) {
		for (Room room : rooms) {
			if (Random.chance(60)) {
					for (int i = 0; i < (Random.chance(50) ? 1 : Random.newInt(3, 6)); i++) {
						Firefly fly = new Firefly();

						fly.x = (room.left + 2) * 16 + Random.newFloat((room.getWidth() - 4) * 16);
						fly.y = (room.top + 2) * 16 + Random.newFloat((room.getHeight() - 4) * 16);

						Dungeon.area.add(fly.add());
					}
			}

			for (int y = room.top; y <= room.bottom; y++) {
				for (int x = room.left; x <= room.right; x++) {
					if (Dungeon.depth > -2 && level.get(x, y) == Terrain.WALL) {
						if (Random.chance(30)) {
							level.setDecor(x, y, (byte) (Random.newInt(Terrain.decor.length) + 1));
						}
					}

					if (Dungeon.depth > -1 && level.get(x, y) == Terrain.WALL && !(level instanceof IceLevel || level instanceof TechLevel)) {
						if (y > room.top && x > room.left  && level.get(x - 1, y - 1) == Terrain.WALL && level.get(x, y - 1) != Terrain.WALL && Random.chance(20)) {
							Cobweb web = new Cobweb();

							web.x = x * 16;
							web.y = y * 16 - 16;
							web.side = 0;

							Dungeon.area.add(web);
							LevelSave.add(web);
						} else if (y > room.top && x < room.right && level.get(x + 1, y - 1) == Terrain.WALL && level.get(x, y - 1) != Terrain.WALL && Random.chance(20)) {
							Cobweb web = new Cobweb();

							web.x = x * 16;
							web.y = y * 16 - 16;
							web.side = 1;

							Dungeon.area.add(web);
							LevelSave.add(web);
						} else if (y < room.bottom - 1 && x > room.left && level.get(x - 1, y + 1) == Terrain.WALL && level.get(x, y + 1) != Terrain.WALL && Random.chance(20)) {
							Cobweb web = new Cobweb();

							web.x = x * 16;
							web.y = y * 16 + 16;
							web.side = 2;

							Dungeon.area.add(web);
							LevelSave.add(web);
						} else if (y < room.bottom - 1 && x < room.right && level.get(x + 1, y + 1) == Terrain.WALL && level.get(x, y + 1) != Terrain.WALL && Random.chance(20)) {
							Cobweb web = new Cobweb();

							web.x = x * 16;
							web.y = y * 16 + 16;
							web.side = 3;

							Dungeon.area.add(web);
							LevelSave.add(web);
						}
					}
				}
			}
		}
	}

	private void paintDoors(Level level, ArrayList<Room> rooms) {
		for (Room r : rooms) {
			for (Room n : r.getConnected().keySet()) {
				Door d = r.getConnected().get(n);

				level.setDecor((int) d.x, (int) d.y + 1, (byte) 0);

				if (!(level instanceof CreepLevel)) {
					byte t = level.get((int) d.x, (int) d.y);
					boolean gt = (d.getType() != Door.Type.EMPTY && d.getType() != Door.Type.MAZE && d.getType() != Door.Type.TUNNEL && d.getType() != Door.Type.SECRET);

					if (t != Terrain.FLOOR_A && t != Terrain.FLOOR_B && t != Terrain.FLOOR_C && t != Terrain.FLOOR_D && t != Terrain.CRACK && gt) {
						org.rexcellentgames.burningknight.entity.level.entities.Door door = new org.rexcellentgames.burningknight.entity.level.entities.Door(
							(int) d.x, (int) d.y, !level.checkFor((int) d.x + 1, (int) d.y, Terrain.SOLID));

						if (d.getType() == Door.Type.REGULAR) {
							d.setType(Door.Type.ENEMY);
						}

						door.autoLock = (d.getType() == Door.Type.ENEMY || d.getType() == Door.Type.BOSS);
						door.lock = (d.getType() == Door.Type.LEVEL_LOCKED || d.getType() == Door.Type.LOCKED);

						if (d.getType() == Door.Type.LEVEL_LOCKED) {
							door.key = BurningKey.class;
						} else if (d.getType() == Door.Type.LOCKED) {
							door.key = KeyC.class;
						} else if (d.getType() == Door.Type.BOSS) {
							door.bkDoor = true;
						}

						door.lockable = door.lock;

						door.add();
						Dungeon.area.add(door);
					}
				}

				if (d.getType() == Door.Type.SECRET) {
					level.set((int) d.x, (int) d.y, Terrain.CRACK);
				} else {
					byte f = Terrain.randomFloor();

					for (int yy = -1; yy <= 1; yy++) {
						for (int xx = -1; xx <= 1; xx++) {
							if (Math.abs(xx) + Math.abs(yy) == 1) {
								byte tl = level.get((int) d.x + xx, (int) d.y + yy);

								if (tl != Terrain.WALL && tl != Terrain.CRACK && tl != Terrain.CHASM) {
									f = tl;
									break;
								}
							}
						}
					}

					level.set((int) d.x, (int) d.y, f);
				}
			}
		}
	}

	private void placeDoors(Room r) {
		/*if (r.getConnected().keySet().size() == 0) {
			throw new RuntimeException("Failed to connect room " + r.getClass().getSimpleName() + " to others: no connections!");
		}*/

		for (Room n : r.getConnected().keySet()) {
			Door door = r.getConnected().get(n);
			if (door == null) {
				Rect i = r.intersect(n);
				ArrayList<Point> doorSpots = new ArrayList<>();

				for (Point p : i.getPoints()) {
					if (r.canConnect(p) && n.canConnect(p)) {
						doorSpots.add(p);
					}
				}

				if (doorSpots.size() > 0) {
					Point point = doorSpots.get(Random.newInt(doorSpots.size()));
					door = new Door(point);

					r.getConnected().put(n, door);
					n.getConnected().put(r, door);
				} else {
					r.getConnected().remove(n);
					n.getConnected().remove(r);

					throw new RuntimeException("Failed to connect rooms " + r.getClass().getSimpleName() + " and " + n.getClass().getSimpleName());
				}
			}
		}
	}

	public static void set(Level level, int cell, byte value) {
		level.set(cell, value);
	}

	public static void set(Level level, int x, int y, byte value) {
		set(level, x + y * Level.getWidth(), value);
	}

	public static void setBold(Level level, int x, int y, byte value) {
		for (int yy = y - 1; yy < y + 2; yy++) {
			for (int xx = x - 1; xx < x + 2; xx++) {
				byte t = level.get(xx, yy);
				if (t != value) {
					if (xx != x || yy != y) {
						if (t == Terrain.WALL) {
							continue;
						}
					}

					set(level, xx, yy, value);
				}
			}
		}
	}

	public static void set(Level level, Point p, byte value) {
		set(level, (int) p.x, (int) p.y, value);
	}

	public static void fill(Level level, int x, int y, int w, int h, byte value) {
		for (int yy = y; yy < y + h; yy++) {
			for (int xx = x; xx < x + w; xx++) {
				set(level, xx, yy, value);
			}
		}
	}

	public static void triangle(Level level, Point from, Point p1, Point p2, byte v) {
		if (p1.x != p2.x) {
			// x

			for (int x = (int) p1.x; x < p2.x; x++) {
				drawLine(level, from, new Point(x, p1.y), v);
			}
		} else {
			// y

			for (int y = (int) p1.y; y < p2.y; y++) {
				drawLine(level, from, new Point(p1.x, y), v);
			}
		}
	}

	public static void fill(Level level, Rect rect, byte value) {
		fill(level, rect.left, rect.top, rect.getWidth(), rect.getHeight(), value);
	}

	public static void fill(Level level, Rect rect, int m, byte value) {
		fill(level, rect.left + m, rect.top + m, rect.getWidth() - m * 2, rect.getHeight() - m * 2, value);
	}

	public static void fill(Level level, Rect rect, int l, int t, int r, int b, byte value) {
		fill(level, rect.left + l, rect.top + t, rect.getWidth() - (l + r), rect.getHeight() - (t + b), value);
	}

	public static void drawLine(Level level, Point from, Point to, byte value) {
		drawLine(level, from, to, value, false);
	}

	public static void drawLine(Level level, Point from, Point to, byte value, boolean bold) {
		float x = from.x;
		float y = from.y;
		float dx = to.x - from.x;
		float dy = to.y - from.y;

		boolean movingbyX = Math.abs(dx) >= Math.abs(dy);
		//normalize
		if (movingbyX) {
			dy /= Math.abs(dx);
			dx /= Math.abs(dx);
		} else {
			dx /= Math.abs(dy);
			dy /= Math.abs(dy);
		}

		if (bold) {
			setBold(level, Math.round(x), Math.round(y), value);
		} else {
			set(level, Math.round(x), Math.round(y), value);
		}

		while ((movingbyX && to.x != x) || (!movingbyX && to.y != y)) {
			x += dx;
			y += dy;

			if (bold) {
				setBold(level, Math.round(x), Math.round(y), value);
			} else {
				set(level, Math.round(x), Math.round(y), value);
			}
		}
	}

	public static void fillEllipse(Level level, Rect rect, byte value) {
		fillEllipse(level, rect.left, rect.top, rect.getWidth(), rect.getHeight(), value);
	}

	public static void fillEllipse(Level level, Rect rect, int m, byte value) {
		fillEllipse(level, rect.left + m, rect.top + m, rect.getWidth() - m * 2, rect.getHeight() - m * 2, value);
	}

	public static void fillEllipse(Level level, int x, int y, int w, int h, byte value) {
		//radii
		double radH = h / 2f;
		double radW = w / 2f;
		boolean liquid = Level.matchesFlag(value, Terrain.LIQUID_LAYER);

		//fills each row of the ellipse from top to bottom
		for (int i = 0; i < h; i++) {

			//y coordinate of the row for determining ellipsis width
			//always want to test the middle of a tile, hence the 0.5 shift
			double rowY = -radH + 0.5 + i;

			//equation is derived from ellipsis formula: y^2/radH^2 + x^2/radW^2 = 1
			//solves for x and then doubles to get the width
			double rowW = 2.0 * Math.sqrt((radW * radW) * (1.0 - (rowY * rowY) / (radH * radH)));

			//need to round to nearest even or odd number, depending on width
			if (w % 2 == 0) {
				rowW = Math.round(rowW / 2.0) * 2.0;

			} else {
				rowW = Math.floor(rowW / 2.0) * 2.0;
				rowW++;
			}

			int cell = x + (w - (int) rowW) / 2 + ((y + i) * Level.getWidth());
			Arrays.fill(liquid ? level.liquidData : level.data, cell, cell + (int) rowW, value);
		}
	}

	public static Point drawInside(Level level, Room room, Point from, int n, byte value) {
		Point step = new Point();

		if (from.x == room.left) {
			step.set(+1, 0);
		} else if (from.x == room.right) {
			step.set(-1, 0);
		} else if (from.y == room.top) {
			step.set(0, +1);
		} else if (from.y == room.bottom) {
			step.set(0, -1);
		}

		Point p = new Point(from).offset(step);

		for (int i = 0; i < n; i++) {
			if (value != -1) {
				set(level, p, value);
			}

			p.offset(step);
		}

		return p;
	}
}