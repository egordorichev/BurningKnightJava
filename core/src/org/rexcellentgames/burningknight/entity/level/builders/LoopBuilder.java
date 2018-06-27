package org.rexcellentgames.burningknight.entity.level.builders;

import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class LoopBuilder extends RegularBuilder {
	private int exponent;
	private float intensity = 1;
	private float offset;
	private Point loopCenter;

	public LoopBuilder setShape(int exponent, float intensity, float offset) {
		this.exponent = Math.abs(exponent);
		this.intensity = intensity % 1;
		this.offset = offset % 0.5f;

		return this;
	}

	private float targetAngle(float percentAlong) {
		percentAlong += offset;
		return 360f * (float) (
			intensity * curveEquation(percentAlong)
				+ (1 - intensity) * (percentAlong)
				- offset);
	}

	private double curveEquation(double x) {
		return Math.pow(4, 2 * exponent)
			* (Math.pow((x % 0.5f) - 0.25, 2 * exponent + 1))
			+ 0.25 + 0.5 * Math.floor(2 * x);
	}

	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		this.setupRooms(init);

		if (this.entrance == null) {
			return null;
		}

		this.entrance.setPos(0, 0);
		this.entrance.setSize();

		float startAngle = Random.newFloat(0, 360);

		ArrayList<Room> loop = new ArrayList<>();
		int roomsOnLoop = (int) (this.multiConnection.size() * this.pathLength) + Random.chances(this.pathLenJitterChances);

		roomsOnLoop = Math.min(roomsOnLoop, this.multiConnection.size());
		roomsOnLoop++;

		float[] pathTunnels = this.pathTunnelChances.clone();

		for (int i = 0; i < roomsOnLoop; i++) {
			if (i == 0) {
				loop.add(entrance);
			} else {
				loop.add(this.multiConnection.remove(0));
			}

			int tunnels = Random.chances(pathTunnels);

			if (tunnels == -1) {
				pathTunnels = this.pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}

			pathTunnels[tunnels]--;

			for (int j = 0; j < tunnels; j++) {
				loop.add(ConnectionRoom.create());
			}
		}

		if (this.exit != null) {
			loop.add((loop.size() + 1) / 2, this.exit);
		}

		Room prev = this.entrance;
		float targetAngle;

		for (int i = 1; i < loop.size(); i++) {
			Room r = loop.get(i);
			targetAngle = startAngle + this.targetAngle(i / (float) loop.size());

			if (placeRoom(init, prev, r, targetAngle) != -1) {
				prev = r;

				if (!init.contains(prev)) {
					init.add(prev);
				}
			} else {
				// fixme this is lazy, there are ways to do this without relying on chance
				return null;
			}
		}

		// fixme this is still fairly chance reliant
		// should just write a general function for stitching two rooms together in builder

		while (!prev.connectTo(this.entrance)) {
			// ConnectionRoom c = ConnectionRoom.create();
			RegularRoom c = RegularRoom.create();

			if (placeRoom(loop, prev, c, angleBetweenRooms(prev, this.entrance)) == -1) {
				return null;
			}

			loop.add(c);
			init.add(c);

			prev = c;
		}

		loopCenter = new Point();

		for (Room r : loop) {
			loopCenter.x += (r.left + r.right) / 2f;
			loopCenter.y += (r.top + r.bottom) / 2f;
		}

		loopCenter.x /= loop.size();
		loopCenter.y /= loop.size();

		ArrayList<Room> branchable = new ArrayList<>(loop);

		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(this.multiConnection);
		roomsToBranch.addAll(this.singleConnection);
		weightRooms(branchable);

		createBranches(init, branchable, roomsToBranch, branchTunnelChances);

		findNeighbours(init);

		for (Room r : init) {
			for (Room n : r.getNeighbours()) {
				if (!n.getConnected().containsKey(r)
					&& Random.newFloat() < extraConnectionChance) {
					r.connectWithRoom(n);
				}
			}
		}

		if (!prev.getConnected().containsKey(this.entrance)) {
			prev.neighbours.add(this.entrance);
			this.entrance.neighbours.add(prev);

			prev.getConnected().put(this.entrance, null);
			this.entrance.getConnected().put(prev, null);
		}

		return init;
	}
}