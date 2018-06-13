package org.rexcellentgames.burningknight.entity.level.builders;

import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class CastleBuilder extends RegularBuilder {
	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		this.setupRooms(init);

		if (this.entrance == null) {
			return null;
		}

		ArrayList<Room> branchable = new ArrayList<>();

		this.entrance.setSize();
		this.entrance.setPos(0, 0);
		branchable.add(this.entrance);


		ArrayList<Room> roomsToBranch = new ArrayList<>(this.multiConnection);

		if (this.exit != null) {
			roomsToBranch.add(this.exit);
		}

		roomsToBranch.addAll(this.singleConnection);

		if (!this.createBranches(init, branchable, roomsToBranch, this.branchTunnelChances)) {
			return null;
		}

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