package org.rexcellentgames.burningknight.entity.level.rooms;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;
import org.rexcellentgames.burningknight.util.path.GraphNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class Room extends Rect implements GraphNode {
	public ArrayList<Room> neighbours = new ArrayList<>();
	public HashMap<Room, Door> connected = new HashMap<>();
	private int price = 1;
	private int distance = 0;
	public boolean hidden;
	public int numEnemies;
	public int lastNumEnemies;
	public int id;

	public int getMinWidth() {
		return 10;
	}

	public int getMinHeight() {
		return 10;
	}

	public int getMaxWidth() {
		return 25;
	}

	public int getMaxHeight() {
		return 25;
	}

	public abstract int getMaxConnections(Connection side);

	public abstract int getMinConnections(Connection side);

	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.randomFloor());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	public int getCurrentConnections(Connection direction) {
		if (direction == Connection.ALL) {
			return connected.size();

		} else {
			int total = 0;

			for (Room r : connected.keySet()) {
				Rect i = intersect(r);

				if (direction == Connection.LEFT && i.getWidth() == 0 && i.left == left) {
					total++;
				} else if (direction == Connection.TOP && i.getHeight() == 0 && i.top == top) {
					total++;
				} else if (direction == Connection.RIGHT && i.getWidth() == 0 && i.right == right) {
					total++;
				} else if (direction == Connection.BOTTOM && i.getHeight() == 0 && i.bottom == bottom) {
					total++;
				}
			}

			return total;
		}
	}

	public int getLastConnections(Connection direction) {
		if (this.getCurrentConnections(Connection.ALL) >= this.getMaxConnections(Connection.ALL)) {
			return 0;
		} else {
			return this.getMaxConnections(direction) - this.getCurrentConnections(direction);
		}
	}

	public boolean canConnect(Point p) {
		return (p.x == left || p.x == right) != (p.y == top || p.y == bottom);
	}

	public boolean canConnect(Connection direction) {
		int cnt = this.getLastConnections(direction);
		return cnt > 0;
	}

	public boolean canConnect(Room r) {
		Rect i = this.intersect(r);
		boolean foundPoint = false;

		for (Point p : i.getPoints()) {
			if (this.canConnect(p) && r.canConnect(p)) {
				foundPoint = true;
				break;
			}
		}

		if (!foundPoint) {
			return false;
		}

		if (i.getWidth() == 0 && i.left == left) {
			return this.canConnect(Connection.LEFT) && r.canConnect(Connection.LEFT);
		} else if (i.getHeight() == 0 && i.top == top) {
			return this.canConnect(Connection.TOP) && r.canConnect(Connection.TOP);
		} else if (i.getWidth() == 0 && i.right == right) {
			return this.canConnect(Connection.RIGHT) && r.canConnect(Connection.RIGHT);
		} else if (i.getHeight() == 0 && i.bottom == bottom) {
			return this.canConnect(Connection.BOTTOM) && r.canConnect(Connection.BOTTOM);
		} else {
			return false;
		}
	}

	public boolean connectTo(Room other) {
		if (this.neighbours.contains(other)) {
			return true;
		}

		Rect i = this.intersect(other);

		int w = i.getWidth();
		int h = i.getHeight();

		if ((w == 0 && h >= 2) ||
			(h == 0 && w >= 2)) {

			this.neighbours.add(other);
			other.neighbours.add(this);

			return true;
		}

		return false;
	}

	public boolean connectWithRoom(Room room) {
		if ((neighbours.contains(room) || connectTo(room))
			&& !connected.containsKey(room) && canConnect(room)) {

			connected.put(room, null);
			room.connected.put(this, null);

			return true;
		}

		return false;
	}

	public Point getRandomCell() {
		int x = Random.newInt(this.left + 1, this.right);
		int y = Random.newInt(this.top + 1, this.bottom);

		return new Point(x, y);
	}

	public Point getRandomFreeCell() {
		Point point;
		int at = 0;

		do {
			if (at++ > 200) {
				Log.error("To many attempts");
				return null;
			}

			point = getRandomCell();
		} while (!Dungeon.level.checkFor((int) point.x, (int) point.y, Terrain.PASSABLE));

		return point;
	}

	public Point getRandomDoorFreeCell() {
		Point point;
		int at = 0;

		while (true) {
			if (at++ > 200) {
				Log.error("To many attempts");
				return null;
			}

			point = getRandomCell();

			for (Door door : connected.values()) {
				int dx = (int) (door.x - point.x);
				int dy = (int) (door.y - point.y);
				float d = (float) Math.sqrt(dx * dx + dy * dy);

				if (d > 3 && Dungeon.level.checkFor((int) point.x, (int) point.y, Terrain.PASSABLE)) {
					return point;
				}
			}
		}
	}

	@Override
	public int getPrice() {
		return this.price;
	}

	@Override
	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int getDistance() {
		return this.distance;
	}

	@Override
	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public Collection<? extends GraphNode> getEdges() {
		return neighbours;
	}

	public Room getRandomNeighbour() {
		return this.neighbours.get(Random.newInt(this.neighbours.size()));
	}

	public HashMap<Room, Door> getConnected() {
		return this.connected;
	}

	public ArrayList<Room> getNeighbours() {
		return this.neighbours;
	}

	public boolean setSize() {
		return setSize(this.getMinWidth(), this.getMaxWidth(), this.getMinHeight(), this.getMaxHeight());
	}

	protected int validateWidth(int w) {
		return w;
	}

	protected int validateHeight(int h) {
		return h;
	}

	protected boolean setSize(int minW, int maxW, int minH, int maxH) {
		if (minW < this.getMinWidth()
			|| maxW > this.getMaxWidth()
			|| minH < this.getMinHeight()
			|| maxH > this.getMaxHeight()
			|| minW > maxW
			|| minH > maxH) {

			return false;
		} else {
			if (quad()) {
				int v = Math.min(validateWidth(Random.newInt(minW, maxW) - 1),
					validateHeight(Random.newInt(minH, maxH) - 1));

				this.resize(v, v);
			} else {
				this.resize(validateWidth(Random.newInt(minW, maxW) - 1),
					validateHeight(Random.newInt(minH, maxH) - 1));
			}

			return true;
		}
	}

	protected boolean quad() {
		return false;
	}

	public boolean setSizeWithLimit(int w, int h) {
		if (w < this.getMinWidth() || h < this.getMinHeight()) {
			return false;
		} else {
			setSize();

			if (getWidth() > w || getHeight() > h) {
				int ww = validateWidth(Math.min(getWidth(), w) - 1);
				int hh = validateHeight(Math.min(getHeight(), h) - 1);

				if (ww >= w || hh >= h) {
					return false;
				}

				resize(ww, hh);
			}

			return true;
		}
	}

	public void clearConnections() {
		for (Room r : this.neighbours) {
			r.neighbours.remove(this);
		}

		this.neighbours.clear();

		for (Room r : this.connected.keySet()) {
			r.connected.remove(this);
		}

		this.connected.clear();
	}

	public boolean canPlaceWater(Point p) {
		return inside(p);
	}

	public final ArrayList<Point> waterPlaceablePoints() {
		ArrayList<Point> points = new ArrayList<>();

		for (int i = left + 1; i <= right - 1; i++) {
			for (int j = top + 1; j <= bottom - 1; j++) {
				Point p = new Point(i, j);
				if (canPlaceWater(p)) points.add(p);
			}
		}

		return points;
	}

	public boolean canPlaceGrass(Point p) {
		return inside(p);
	}

	public final ArrayList<Point> grassPlaceablePoints() {
		ArrayList<Point> points = new ArrayList<>();

		for (int i = left + 1; i <= right - 1; i++) {
			for (int j = top + 1; j <= bottom - 1; j++) {
				Point p = new Point(i, j);
				if (canPlaceGrass(p)) points.add(p);
			}
		}

		return points;
	}

	public enum Connection {
		ALL, LEFT, RIGHT, TOP, BOTTOM
	}

	@Override
	public int getWidth() {
		return super.getWidth() + 1;
	}

	@Override
	public int getHeight() {
		return super.getHeight() + 1;
	}

	public Point getCenter() {
		return new Point(this.left + this.getWidth() / 2, this.top + this.getHeight() / 2);
	}

	protected Rect getConnectionSpace() {
		Point c = getDoorCenter();

		return new Rect((int) c.x, (int) c.y, (int) c.x, (int) c.y);
	}

	protected Point getDoorCenter() {
		Point doorCenter = new Point(0, 0);

		for (Door door : connected.values()) {
			doorCenter.x += door.x;
			doorCenter.y += door.y;
		}

		int n = this.connected.size();

		Point c = new Point((int) doorCenter.x / n, (int) doorCenter.y / n);
		if (Random.newFloat() < doorCenter.x % 1) c.x++;
		if (Random.newFloat() < doorCenter.y % 1) c.y++;
		c.x = (int) MathUtils.clamp(left + 1, right - 1, c.x);
		c.y = (int) MathUtils.clamp(top + 1, bottom - 1, c.y);

		return c;
	}

	protected void paintTunnel(Level level, byte floor) {
		paintTunnel(level, floor, false);
	}

	protected void paintTunnel(Level level, byte floor, boolean bold) {
		if (this.connected.size() == 0) {
			Log.error("Invalid connection room");
			return;
		}

		Rect c = getConnectionSpace();

		for (Door door : this.getConnected().values()) {
			Point start;
			Point mid;
			Point end;

			start = new Point(door);
			if (start.x == left) start.x++;
			else if (start.y == top) start.y++;
			else if (start.x == right) start.x--;
			else if (start.y == bottom) start.y--;

			int rightShift;
			int downShift;

			if (start.x < c.left) rightShift = (int) (c.left - start.x);
			else if (start.x > c.right) rightShift = (int) (c.right - start.x);
			else rightShift = 0;

			if (start.y < c.top) downShift = (int) (c.top - start.y);
			else if (start.y > c.bottom) downShift = (int) (c.bottom - start.y);
			else downShift = 0;

			if (door.x == left || door.x == right) {
				mid = new Point(start.x + rightShift, start.y);
				end = new Point(mid.x, mid.y + downShift);
			} else {
				mid = new Point(start.x, start.y + downShift);
				end = new Point(mid.x + rightShift, mid.y);
			}

			Painter.drawLine(level, start, mid, floor, bold);
			Painter.drawLine(level, mid, end, floor, bold);
		}
	}
}