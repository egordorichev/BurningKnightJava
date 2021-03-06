package org.rexcellentgames.burningknight.entity.level.builders;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.rooms.PrebossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.BossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.LampRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RegularBuilder extends Builder {
	protected EntranceRoom entrance;
	protected EntranceRoom exit;
	protected BossRoom boss;
	protected PrebossRoom preboss;
	protected LampRoom lamp;
	protected float pathVariance = 45f;
	protected float pathLength = 0.5f;
	protected float[] pathLenJitterChances = new float[]{0, 1, 0};
	protected float[] pathTunnelChances = new float[]{1, 3, 1};
	protected float[] branchTunnelChances = new float[]{2, 2, 1};
	protected float extraConnectionChance = 0.2f;

	protected ArrayList<Room> multiConnection = new ArrayList<>();
	protected ArrayList<Room> singleConnection = new ArrayList<>();

	public void setupRooms(ArrayList<Room> rooms) {
		this.entrance = null;
		this.exit = null;
		this.lamp = null;
		this.multiConnection.clear();
		this.singleConnection.clear();

		for (Room room : rooms) {
			room.setEmpty();
		}

		for (Room room : rooms) {
			if (room instanceof BossRoom) {
				this.boss = (BossRoom) room;
				this.exit = (EntranceRoom) room;
			} else if (room instanceof EntranceRoom && ((EntranceRoom) room).exit) {
				this.exit = (EntranceRoom) room;
			} else if (room instanceof EntranceRoom) {
				this.entrance = (EntranceRoom) room;
			} else if (room instanceof PrebossRoom) {
				this.preboss = (PrebossRoom) room;
			} else if (room.getMaxConnections(Room.Connection.ALL) == 1) {
				this.singleConnection.add(room);
			} else if (room.getMaxConnections(Room.Connection.ALL) > 1) {
				this.multiConnection.add(room);
			}

			if (room instanceof LampRoom) {
				this.lamp = (LampRoom) room;
			}
		}

		if (Dungeon.type != Dungeon.Type.INTRO) {
			this.weightRooms(this.multiConnection);

			if (GameSave.runId != 0 || Dungeon.depth != 1) {
				// Collections.shuffle(rooms, new java.util.Random(Random.getSeed().hashCode()));
			}

			this.multiConnection = new ArrayList<>(new LinkedHashSet<>(this.multiConnection));
		}
	}

	protected void weightRooms(ArrayList<Room> rooms) {
		for (Room room : rooms.toArray(new Room[0])) {
			if (room instanceof org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom) {
				for (int i = 1; i < ((org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom) room).getSize().getConnectionWeight(); i++) {
					rooms.add(room);
				}
			}
		}
	}

	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		return init;
	}

	public RegularBuilder setPathVariance(float var) {
		this.pathVariance = var;
		return this;
	}

	public RegularBuilder setPathLength(float len, float[] jitter) {
		this.pathLength = len;
		this.pathLenJitterChances = jitter;
		return this;
	}

	public RegularBuilder setTunnelLength(float[] path, float[] branch) {
		this.pathTunnelChances = path;
		this.branchTunnelChances = branch;
		return this;
	}

	public RegularBuilder setExtraConnectionChance(float chance) {
		this.extraConnectionChance = chance;
		return this;
	}

	protected boolean createBranches(ArrayList<Room> rooms, ArrayList<Room> branchable, ArrayList<Room> roomsToBranch, float[] connChances) {
		int i = 0;
		int n = 0;
		float angle;
		int tries;
		Room curr;
		ArrayList<Room> connectingRoomsThisBranch = new ArrayList<>();

		float[] connectionChances = connChances.clone();

		while (i < roomsToBranch.size()) {
			Room r = roomsToBranch.get(i);
			n++;
			connectingRoomsThisBranch.clear();

			do {
				curr = branchable.get(Random.newInt(branchable.size()));
			} while (curr instanceof org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom);

			int connectingRooms = Random.chances(connectionChances);

			if (connectingRooms == -1) {
				connectionChances = connChances.clone();
				connectingRooms = Random.chances(connectionChances);
			}

			connectionChances[connectingRooms]--;

			for (int j = 0; j < connectingRooms; j++) {
				ConnectionRoom t = ConnectionRoom.create();
				tries = 3;

				do {
					angle = placeRoom(rooms, curr, t, randomBranchAngle(curr));
					tries--;
				} while (angle == -1 && tries > 0);
				if (angle == -1) {
					for (Room c : connectingRoomsThisBranch) {
						c.clearConnections();
						rooms.remove(c);
					}

					connectingRoomsThisBranch.clear();
					break;
				} else {
					connectingRoomsThisBranch.add(t);
					rooms.add(t);
				}

				curr = t;
			}

			if (connectingRoomsThisBranch.size() != connectingRooms) {
				if (n > 30) {
					return false;
				}

				continue;
			}

			tries = 10;

			do {
				angle = placeRoom(rooms, curr, r, randomBranchAngle(curr));
				tries--;
			} while (angle == -1 && tries > 0);

			if (angle == -1) {
				for (Room t : connectingRoomsThisBranch) {
					t.clearConnections();
					rooms.remove(t);
				}
				connectingRoomsThisBranch.clear();

				if (n > 30) {
					return false;
				}

				continue;
			}

			for (Room aConnectingRoomsThisBranch : connectingRoomsThisBranch) {
				if (Random.newInt(3) <= 1) branchable.add(aConnectingRoomsThisBranch);
			}

			if (r.getMaxConnections(Room.Connection.ALL) > 1 && Random.newInt(3) == 0) {
				if (r instanceof org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom) {
					for (int j = 0; j < ((RegularRoom) r).getSize().getConnectionWeight(); j++) {
						branchable.add(r);
					}
				} else {
					branchable.add(r);
				}
			}

			i++;
		}

		return true;
	}

	protected float randomBranchAngle(Room r) {
		return Random.newFloat(360f);
	}
}