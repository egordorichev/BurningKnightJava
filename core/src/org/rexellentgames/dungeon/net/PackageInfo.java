package org.rexellentgames.dungeon.net;

import com.esotericsoftware.kryonet.Connection;

public class PackageInfo {
	public Object object;
	public Connection connection;

	public PackageInfo(Object object, Connection connection) {
		this.object = object;
		this.connection = connection;
	}
}