package org.rexcellentgames.burningknight.entity.level.builders;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class LineBuilder extends RegularBuilder {
	private float direction = Random.newFloat(0, 360);

	public LineBuilder setAngle(float angle) {
		this.direction = angle % 360f;
		return this;
	}

	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		setupRooms(init);

		if (entrance == null) {
			Log.error("No entrance!");
			return null;
		}

		ArrayList<Room> branchable = new ArrayList<>();

		entrance.setSize();
		entrance.setPos(0, 0);
		branchable.add(entrance);

		if (multiConnection.size() == 0) {
			placeRoom(init, entrance, boss, Random.newFloat(360));
			return init;
		}

		int roomsOnPath = (int) (this.multiConnection.size() * pathLength) + Random.chances(pathLenJitterChances);
		roomsOnPath = Math.min(roomsOnPath, this.multiConnection.size());

		Room curr = entrance;

		float[] pathTunnels = pathTunnelChances.clone();
		boolean boss = preboss != null;

		for (int i = 0; i <= roomsOnPath + (boss ? 1 : 0); i++) {
			if (i == roomsOnPath && exit == null) {
				continue;
			}

			int tunnels = Random.chances(pathTunnels);

			if (tunnels == -1) {
				pathTunnels = pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}

			pathTunnels[tunnels]--;

			if (i != 0 && (!boss || i < roomsOnPath - 1) && Dungeon.depth != 0) {
				for (int j = 0; j < tunnels; j++) {
					ConnectionRoom t = ConnectionRoom.create();

					if (placeRoom(init, curr, t, direction + Random.newFloat(-pathVariance, pathVariance)) == -1) {
						return null;
					}

					branchable.add(t);
					init.add(t);
					curr = t;
				}
			}

			Room r;

			if (boss) {
				r = (i > roomsOnPath ? exit : (i == roomsOnPath ? preboss : this.multiConnection.get(i)));
			} else {
				r = (i == roomsOnPath ? exit : this.multiConnection.get(i));
			}

			if (placeRoom(init, curr, r, direction + Random.newFloat(-pathVariance, pathVariance)) == -1) {
				return null;
			}

			branchable.add(r);
			curr = r;
		}

		ArrayList<Room> roomsToBranch = new ArrayList<>();

		for (int i = roomsOnPath; i < this.multiConnection.size(); i++) {
			roomsToBranch.add(this.multiConnection.get(i));
		}

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

		return init;
	}
}