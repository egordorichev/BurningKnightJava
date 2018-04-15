package org.rexellentgames.dungeon.entity;

import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

public class NetworkedEntity extends Entity {
	public Point vel = new Point();
	private boolean sleeping;
	protected Point oldPosition = new Point();
	protected float t;
	protected String state = "idle";
	public boolean local = false;

	public String getState() {
		return state;
	}

	public String getParam() {
		return "";
	}

	public boolean isSleeping() {
		return this.sleeping;
	}

	@Override
	public void update(float dt) {
		this.sleeping = (this.x != this.oldPosition.x || this.y != this.oldPosition.y);

		super.update(dt);

		oldPosition.x = this.x;
		oldPosition.y = this.y;
	}

	public void become(String state) {
		if (!this.state.equals(state)) {
			this.state = state;
			this.t = 0;

			if (Network.SERVER) {
				Network.server.getServerHandler().sendToAll(Packets.makeSetEntityState(this.getId(), this.state));
			}
		}
	}
}