package org.rexellentgames.dungeon.entity.level.rooms;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;
import org.rexellentgames.dungeon.util.path.GraphNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public abstract class Room extends Rect implements GraphNode {
	public static final ArrayList<Type> SPECIAL = new ArrayList<Type>(Arrays.asList(
		Type.HOLE
	));

	private ArrayList<Room> neighbours = new ArrayList<Room>();
	private HashMap<Room, org.rexellentgames.dungeon.entity.level.features.Door> connected = new HashMap<Room, org.rexellentgames.dungeon.entity.level.features.Door>();
	private Type type;
	private int price = 1;
	private int distance = 0;

	public Room(Type type) {
		this.type = type;
	}

	public int getMinWidth() {
		return 3;
	}

	public int getMinHeight() {
		return 3;
	}

	public int getMaxWidth() {
		return 9;
	}

	public int getMaxHeight() {
		return 9;
	}

	public abstract int getMaxConnections(Connection side);

	public abstract int getMinConnections(Connection side);

	public void paint(Level level) {
		fill(level, this.left, this.top, this.right - 1, this.bottom - 1, Terrain.FLOOR);
	}

	protected void fill(Level level, int l, int t, int r, int b, int v) {
		for (int x = l; x < r; x++) {
			for (int y = t; y < b; y++) {
				level.set(x, y, (short) v);
			}
		}
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
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
			Log.info("No point");
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
		if (this.connectTo(room) && !this.connected.containsKey(room) && this.canConnect(room)) {
			this.connected.put(room, null);
			room.connected.put(this, null);

			return true;
		}

		return false;
	}

	public int getRandomCell() {
		int x = Random.newInt(this.left + 1, this.right);
		int y = Random.newInt(this.top + 1, this.bottom);

		return x + y * Level.getWIDTH();
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

	protected boolean setSize(int minW, int maxW, int minH, int maxH) {
		if (minW < this.getMinWidth()
			|| maxW > this.getMaxWidth()
			|| minH < this.getMinHeight()
			|| maxH > this.getMaxHeight()
			|| minW > maxW
			|| minH > maxH) {

			return false;
		} else {
			this.resize(Random.newInt(minW, maxW) - 1,
				Random.newInt(minH, maxH) - 1);

			return true;
		}
	}

	public boolean setSizeWithLimit(int w, int h) {
		if (w < this.getMinWidth() || h < this.getMinHeight()) {
			return false;
		} else {
			setSize();

			if (getWidth() > w || getHeight() > h) {
				resize(Math.min(getWidth(), w) - 1, Math.min(getHeight(), h) - 1);
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

	public enum Connection {
		ALL, LEFT, RIGHT, TOP, BOTTOM
	}

	public enum Type {
		REGULAR,
		ENTRANCE,
		EXIT,
		TUNNEL,
		HOLE
	}

	@Override
	public int getWidth() {
		return super.getWidth() + 1;
	}

	@Override
	public int getHeight() {
		return super.getHeight() + 1;
	}
}