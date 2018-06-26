package org.rexcellentgames.burningknight.entity.level.painters;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.key.KeyB;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Patch;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.secret.SecretRoom;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class Painter {
  private static byte[] floors = new byte[]{
    1, 34, 35, 36, 37, 38, 39, 40
  };
  private float grass = 0f;
  private float water = 0f;

  public static void set(Level level, int cell, byte value) {
    level.set(cell, value);
  }

  public static void set(Level level, int x, int y, byte value) {
    set(level, x + y * Level.getWidth(), value);
  }

  public static void setBold(Level level, int x, int y, byte value) {
    for (int xx = x - 1; xx < x + 2; xx++) {
      for (int yy = y - 1; yy < y + 2; yy++) {
        if (level.get(xx, yy) != value) {
          set(level, xx, yy, (xx == x && yy == y) ? value : Terrain.WALL);
        }
      }
    }
  }

  public static void set(Level level, Point p, byte value) {
    set(level, (int) p.x, (int) p.y, value);
  }

  public static void fill(Level level, int x, int y, int w, int h, byte value) {
    for (int xx = x; xx < x + w; xx++) {
      for (int yy = y; yy < y + h; yy++) {
        set(level, xx, yy, value);
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
    boolean liquid = (value == Terrain.DIRT || value == Terrain.LAVA || value == Terrain.WATER);

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

  public Painter setWater(float v) {
    this.water = v;
    return this;
  }

  public Painter setGrass(float v) {
    this.grass = v;
    return this;
  }

  public void paint(Level level, ArrayList<Room> rooms) {
    int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;

    for (Room r: rooms) {
      if (r.left < leftMost) leftMost = r.left;
      if (r.top < topMost) topMost = r.top;
    }

    //subtract 1 for padding
    leftMost--;
    topMost--;

    leftMost -= 10;
    topMost -= 10;

    int rightMost = 0, bottomMost = 0;

    for (Room r: rooms) {
      r.shift(-leftMost, -topMost);
      if (r.right > rightMost) rightMost = r.right;
      if (r.bottom > bottomMost) bottomMost = r.bottom;
    }

    //add 1 for padding
    rightMost++;
    bottomMost++;

    rightMost += 10;
    bottomMost += 10;

    //add 1 to account for 0 values
    Level.setSize(rightMost + 1, bottomMost + 1);
    level.generateDecor();
    level.fill();

    for (Room room: rooms) {
      this.placeDoors(room);
      room.paint(level);

      if (room instanceof SecretRoom) {
        for (int x = room.left; x <= room.right; x++) {
          for (int y = room.top; y <= room.bottom; y++) {
            level.hide(x, y);
          }
        }
      }
    }

    if (this.grass > 0) {
      this.paintGrass(level, rooms);
    }

    if (this.water > 0) {
      this.paintWater(level, rooms);
    }

    this.paintDoors(level, rooms);
  }

  private void paintWater(Level level, ArrayList<Room> rooms) {
    boolean[] lake = Patch.generate(this.water, 5);

    for (Room r: rooms) {
      for (Point p: r.waterPlaceablePoints()) {
        int i = Level.toIndex((int) p.x, (int) p.y);
        byte t = level.data[i];
        if (lake[i] && (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C) && level.liquidData[i] == 0) {
          level.set(i, Terrain.WATER);
        }
      }
    }
  }

  private void paintGrass(Level level, ArrayList<Room> rooms) {
    boolean[] grass = Patch.generate(this.grass, 5);

    for (Room r: rooms) {
      for (Point p: r.grassPlaceablePoints()) {
        int i = Level.toIndex((int) p.x, (int) p.y);
        byte t = level.data[i];
        if (grass[i] && (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C) && level.liquidData[i] == 0) {
          level.set(i, Terrain.DIRT);

					/*if (Random.chance(10)) {
						Plant plant = Plant.random();

						plant.x = p.x * 16;
						plant.y = p.y * 16 - 4;

						LevelSave.add(plant);
						Dungeon.area.add(plant);

						plant.grow();
					}*/
        }
      }
    }
  }

  public void draw(Level level, ArrayList<Room> rooms) {
    Log.info("Making all pretty...");
    this.decorate(level, rooms);
  }

  protected void decorate(Level level, ArrayList<Room> rooms) {
    for (Room room: rooms) {
      int y = room.bottom;

      for (int x = room.left; x < room.right; x++) {
        if (level.get(x, y) == Terrain.WALL) {
          if (Random.chance(20)) {
            level.setDecor(x, y, (byte) (Random.newInt(Terrain.decor.length) + 1));
          }
        }
      }
    }
  }

  private void paintDoors(Level level, ArrayList<Room> rooms) {
    for (Room r: rooms) {
      for (Room n: r.getConnected().keySet()) {
        Door d = r.getConnected().get(n);
        byte t = level.get((int) d.x, (int) d.y);
        boolean gt = (d.getType() != Door.Type.EMPTY && d.getType() != Door.Type.MAZE && d.getType() != Door.Type.TUNNEL && d.getType() != Door.Type.SECRET);

        if (t != Terrain.FLOOR_A && t != Terrain.FLOOR_A && t != Terrain.CRACK && gt) {
          org.rexcellentgames.burningknight.entity.level.entities.Door door = new org.rexcellentgames.burningknight.entity.level.entities.Door(
            (int) d.x, (int) d.y, !level.checkFor((int) d.x + 1, (int) d.y, Terrain.SOLID));

          if (d.getType() == Door.Type.REGULAR) {
            d.setType(Door.Type.ENEMY);
          }

          door.autoLock = (d.getType() == Door.Type.ENEMY);
          door.lock = (d.getType() == Door.Type.LEVEL_LOCKED || d.getType() == Door.Type.LOCKED);

          if (d.getType() == Door.Type.LEVEL_LOCKED) {
            door.key = KeyC.class;
          } else if (d.getType() == Door.Type.LOCKED) {
            door.key = KeyB.class;
          }

          door.rooms[0] = r;
          door.rooms[1] = n;
          door.lockable = door.lock;

          door.add();
          Dungeon.area.add(door);
        }

        if (d.getType() == Door.Type.SECRET) {
          level.set((int) d.x, (int) d.y, Terrain.CRACK);
        } else if (gt) {
          level.set((int) d.x, (int) d.y, Terrain.FLOOR_A);
        } else {
          level.set((int) d.x, (int) d.y, Terrain.FLOOR_A);
        }
      }
    }
  }

  private void placeDoors(Room r) {
		/*if (r.getConnected().keySet().size() == 0) {
			throw new RuntimeException("Failed to connect room " + r.getClass().getSimpleName() + " to others: no connections!");
		}*/

    for (Room n: r.getConnected().keySet()) {
      Door door = r.getConnected().get(n);
      if (door == null) {
        Rect i = r.intersect(n);
        ArrayList<Point> doorSpots = new ArrayList<>();

        for (Point p: i.getPoints()) {
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
}