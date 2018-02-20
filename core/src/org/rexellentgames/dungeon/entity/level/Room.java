package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.painter.*;
import org.rexellentgames.dungeon.util.path.GraphNode;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Rect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Room extends Rect implements GraphNode {
	public enum Type {
		NULL(null),
		REGULAR(RegularPainter.class),
		ENTRANCE(EntrancePainter.class),
		EXIT(ExitPainter.class),
		TUNNEL(TunnelPainter.class),
		HOLE(HolePainter.class),
		SECRET(SecretPainter.class);

		private Method paint;

		Type(Class<? extends Painter> painter) {
			try {
				paint = painter.getMethod("paint", Level.class, Room.class);
			} catch (Exception e) {
				paint = null;
			}
		}

		public void paint( Level level, Room room ) {
			try {
				paint.invoke(null, level, room);
			} catch (Exception e) {
				Dungeon.reportException(e);
			}
		}
	}

	public static final ArrayList<Type> SPECIAL = new ArrayList<Type>(Arrays.asList(
		Type.HOLE, Type.SECRET
	));

	private ArrayList<Room> neighbours = new ArrayList<Room>();
	private HashMap<Room, Door> connected = new HashMap<Room, Door>();
	private Type type = Type.NULL;
	private int price = 1;
	private int distance = 0;

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void connectTo(Room room) {
		Rect i = this.intersect(room);

		if ((i.getWidth() == 0 && i.getHeight() >= 3) ||
			(i.getHeight() == 0 && i.getWidth() >= 3)) {

			this.neighbours.add(room);
			room.neighbours.add(this);
		}
	}

	public void connectWithRoom(Room room) {
		if (!this.connected.containsKey(room)) {
			this.connected.put(room, null);
			room.connected.put(this, null);
		}
	}

	@Override
	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int getPrice() {
		return this.price;
	}

	@Override
	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int getDistance() {
		return this.distance;
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
}