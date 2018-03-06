package org.rexellentgames.dungeon.entity.level.builders;

import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.special.TowerBaseRoom;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class CastleBuilder extends RegularBuilder {
	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		this.setupRooms(init);

		if (this.entrance == null) {
			return null;
		}

		ArrayList<Room> branchable = new ArrayList<Room>();

		this.entrance.setSize();
		this.entrance.setPos(0, 0);
		branchable.add(this.entrance);


		ArrayList<Room> roomsToBranch = new ArrayList<Room>();
		roomsToBranch.addAll(this.multiConnection);

		if (this.exit != null) {
			roomsToBranch.add(this.exit);
		}

		roomsToBranch.addAll(this.singleConnection);

		this.createBranches(init, branchable, roomsToBranch, this.branchTunnelChances);
		findNeighbours(init);

		for (Room r : init) {
			for (Room n : r.getNeighbours()) {
				if (!n.getConnected().containsKey(r)
					&& Random.newFloat() < this.extraConnectionChance) {
					r.connectWithRoom(n);
				}
			}
		}

		return init;
	}
}