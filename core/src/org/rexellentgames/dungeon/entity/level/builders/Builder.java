package org.rexellentgames.dungeon.entity.level.builders;

import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

import java.util.ArrayList;
import java.util.Iterator;

public class Builder {
	private static final double A = 180 / Math.PI;

	protected static void findNeighbours(ArrayList<Room> rooms) {
		Room[] ra = rooms.toArray(new Room[0]);

		for (int i = 0; i < ra.length - 1; i++) {
			for (int j = i + 1; j < ra.length; j++) {
				ra[i].connectTo(ra[j]);
			}
		}
	}

	protected static Rect findFreeSpace(Point start, ArrayList<Room> collision, int maxSize) {
		Rect space = new Rect((int) (start.x - maxSize),
			(int) (start.y - maxSize), (int) (start.x + maxSize), (int) (start.y + maxSize));

		ArrayList<Room> colliding = new ArrayList<Room>(collision);

		do {
			Iterator<Room> it = colliding.iterator();

			while (it.hasNext()) {
				Room room = it.next();

				if (room.isEmpty()
					|| Math.max(space.left, room.left) >= Math.min(space.right, room.right)
					|| Math.max(space.top, room.top) >= Math.min(space.bottom, room.bottom)) {
					it.remove();
				}
			}

			Room closestRoom = null;

			int closestDiff = Integer.MAX_VALUE;
			boolean inside = true;
			int curDiff = 0;

			for (Room curRoom : colliding) {
				if (start.x <= curRoom.left) {
					inside = false;
					curDiff += curRoom.left - start.x;
				} else if (start.x >= curRoom.right) {
					inside = false;
					curDiff += start.x - curRoom.right;
				}

				if (start.y <= curRoom.top) {
					inside = false;
					curDiff += curRoom.top - start.y;
				} else if (start.y >= curRoom.bottom) {
					inside = false;
					curDiff += start.y - curRoom.bottom;
				}

				if (inside) {
					space.set(new Rect((int) start.x, (int) start.y, (int) start.x, (int) start.y));
					return space;
				}

				if (curDiff < closestDiff) {
					closestDiff = curDiff;
					closestRoom = curRoom;
				}
			}

			int wDiff, hDiff;
			if (closestRoom != null) {
				wDiff = Integer.MAX_VALUE;

				if (closestRoom.left >= start.x) {
					wDiff = (space.right - closestRoom.left) * (space.getHeight() + 1);
				} else if (closestRoom.right <= start.x) {
					wDiff = (closestRoom.right - space.left) * (space.getHeight() + 1);
				}

				hDiff = Integer.MAX_VALUE;

				if (closestRoom.top >= start.y) {
					hDiff = (space.bottom - closestRoom.top) * (space.getWidth() + 1);
				} else if (closestRoom.bottom <= start.y) {
					hDiff = (closestRoom.bottom - space.top) * (space.getWidth() + 1);
				}

				if (wDiff < hDiff || wDiff == hDiff && Random.newInt(2) == 0) {
					if (closestRoom.left >= start.x && closestRoom.left < space.right) space.right = closestRoom.left;
					if (closestRoom.right <= start.x && closestRoom.right > space.left) space.left = closestRoom.right;
				} else {
					if (closestRoom.top >= start.y && closestRoom.top < space.bottom) space.bottom = closestRoom.top;
					if (closestRoom.bottom <= start.y && closestRoom.bottom > space.top) space.top = closestRoom.bottom;
				}

				colliding.remove(closestRoom);
			} else {
				colliding.clear();
			}
		} while (!colliding.isEmpty());

		return space;
	}

	protected static float angleBetweenRooms(Room from, Room to) {
		Point fromCenter = new Point((from.left + from.right) / 2f, (from.top + from.bottom) / 2f);
		Point toCenter = new Point((to.left + to.right) / 2f, (to.top + to.bottom) / 2f);

		return angleBetweenPoints(fromCenter, toCenter);
	}

	protected static float angleBetweenPoints(Point from, Point to) {
		double m = (to.y - from.y) / (to.x - from.x);
		float angle = (float) (A * (Math.atan(m) + Math.PI / 2.0));

		if (from.x > to.x) {
			angle -= 180f;
		}

		return angle;
	}

	protected static float placeRoom(ArrayList<Room> collision, Room prev, Room next, float angle) {
		angle %= 360f;
		
		if (angle < 0) {
			angle += 360f;
		}

		Point prevCenter = new Point((prev.left + prev.right) / 2f, (prev.top + prev.bottom) / 2f);

		double m = Math.tan(angle / A + Math.PI / 2.0);
		double b = prevCenter.y - m * prevCenter.x;

		Point start;
		Room.Connection direction;

		if (Math.abs(m) >= 1) {
			if (angle < 90 || angle > 270) {
				direction = Room.Connection.TOP;
				start = new Point((int) Math.round((prev.top - b) / m), prev.top);
			} else {
				direction = Room.Connection.BOTTOM;
				start = new Point((int) Math.round((prev.bottom - b) / m), prev.bottom);
			}
		} else {
			if (angle < 180) {
				direction = Room.Connection.RIGHT;
				start = new Point(prev.right, (int) Math.round(m * prev.right + b));
			} else {
				direction = Room.Connection.LEFT;
				start = new Point(prev.left, (int) Math.round(m * prev.left + b));
			}
		}

		if (direction == Room.Connection.TOP || direction == Room.Connection.BOTTOM) {
			start.x = (int) MathUtils.clamp(prev.left + 1, prev.right - 1, start.x);
		} else {
			start.y = (int) MathUtils.clamp(prev.top + 1, prev.bottom - 1, start.y);
		}
		
		Rect space = findFreeSpace(start, collision, Math.max(next.getMaxWidth(), next.getMaxHeight()));
		
		if (!next.setSizeWithLimit(space.getWidth() + 1, space.getHeight() + 1)) {
			return -1;
		}

		Point targetCenter = new Point();

		if (direction == Room.Connection.TOP) {
			targetCenter.y = prev.top - (next.getHeight() - 1) / 2f;
			targetCenter.x = (float) ((targetCenter.y - b) / m);
			next.setPos(Math.round(targetCenter.x - (next.getWidth() - 1) / 2f), prev.top - (next.getHeight() - 1));
		} else if (direction == Room.Connection.BOTTOM) {
			targetCenter.y = prev.bottom + (next.getHeight() - 1) / 2f;
			targetCenter.x = (float) ((targetCenter.y - b) / m);
			next.setPos(Math.round(targetCenter.x - (next.getWidth() - 1) / 2f), prev.bottom);
		} else if (direction == Room.Connection.RIGHT) {
			targetCenter.x = prev.right + (next.getWidth() - 1) / 2f;
			targetCenter.y = (float) (m * targetCenter.x + b);
			next.setPos(prev.right, Math.round(targetCenter.y - (next.getHeight() - 1) / 2f));
		} else if (direction == Room.Connection.LEFT) {
			targetCenter.x = prev.left - (next.getWidth() - 1) / 2f;
			targetCenter.y = (float) (m * targetCenter.x + b);
			next.setPos(prev.left - (next.getWidth() - 1), Math.round(targetCenter.y - (next.getHeight() - 1) / 2f));
		}

		if (direction == Room.Connection.TOP || direction == Room.Connection.BOTTOM) {
			if (next.right < prev.left + 2) {
				next.shift(prev.left + 2 - next.right, 0);
			} else if (next.left > prev.right - 2) {
				next.shift(prev.right - 2 - next.left, 0);
			}

			if (next.right > space.right) {
				next.shift(space.right - next.right, 0);
			} else if (next.left < space.left) {
				next.shift(space.left - next.left, 0);
			}
		} else {
			if (next.bottom < prev.top + 2) {
				next.shift(0, prev.top + 2 - next.bottom);
			} else if (next.top > prev.bottom - 2) {
				next.shift(0, prev.bottom - 2 - next.top);
			}

			if (next.bottom > space.bottom) {
				next.shift(0, space.bottom - next.bottom);
			} else if (next.top < space.top) {
				next.shift(0, space.top - next.top);
			}
		}

		if (next.connectWithRoom(prev)) {
			return angleBetweenRooms(prev, next);
		} else {
			return -1;
		}
	}

	public ArrayList<Room> build(ArrayList<Room> init) {
		return null;
	}
}