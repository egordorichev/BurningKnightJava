package org.rexcellentgames.burningknight.entity.level.rooms;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.HashMap;

public class HandmadeRoom extends RegularRoom {
	public static TiledMap map;
	public static MapLayer rooms;
	public static TiledMapTileLayer tiles;
	public static HashMap<String, RoomData> datas = new HashMap<>();

	public static class RoomData {
		byte data[];
		byte w;
		byte h;
	}

	private static boolean inited;

	public static void init() {
		if (inited) {
			return;
		}

		inited = true;

		Log.info("Loading handmade rooms");

		map = new TmxMapLoader().load("maps/main.tmx");
		MapLayers layers = map.getLayers();

		for (MapLayer layer : layers) {
			if (layer.getName().equals("tiles")) {
				tiles = (TiledMapTileLayer) layer;
			} else if (layer.getName().equals("rooms")) {
				rooms = layer;
			}
		}

		for (MapObject object : rooms.getObjects()) {
			if (object instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				RoomData data = new RoomData();

				data.w = (byte) (rect.width / 16);
				data.h = (byte) (rect.height / 16);
				data.data = new byte[data.w * data.h];

				int rx = (int) (rect.x / 16);
				int ry = (int) (rect.y / 16);

				for (int x = 0; x < data.w; x++) {
					for (int y = 0; y < data.h; y++) {
						TiledMapTileLayer.Cell cell = tiles.getCell(rx + x, ry + y);
						data.data[x + y * data.w] = cell == null ? 9 : (byte) cell.getTile().getId();
					}
				}

				datas.put(object.getName(), data);
			}
		}
	}

	public static void destroy() {
		if (!inited) {
			return;
		}

		map.dispose();
	}

	public RoomData data;

	public HandmadeRoom(String id) {
		this.data = datas.get(id);
	}

	public HandmadeRoom() {
		this("grid_room");
	}

	@Override
	public boolean canConnect(Point p) {
		float y = p.y;
		float x = p.x;

		if (y == this.top) {
			y += 1;
		} else if (y == this.bottom) {
			y -= 1;
		}

		if (x == this.left) {
			x += 1;
		} else if (x == this.right) {
			x -= 1;
		}

		int i = (int) ((x - this.left - 1) + (y - this.top - 1) * this.data.w);

		byte t = this.data.data[i];

		if (t == 6 || t == 8 || t == 9) {
			return false;
		}
		return super.canConnect(p);
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int x = 0; x < this.data.w; x++) {
			for (int y = 0; y < this.data.h; y++) {
				byte tt = Terrain.FLOOR_A;
				byte t = this.data.data[x + y * this.data.w];

				switch (t) {
					case 2: tt = Terrain.FLOOR_B; break;
					case 3: tt = Terrain.FLOOR_D; break;
					case 4: tt = Terrain.FLOOR_C; break;
					case 5: tt = Terrain.DIRT; break;
					case 6: tt = Terrain.LAVA; break;
					case 7: tt = Terrain.WATER; break;
					case 8: tt = Terrain.WALL; break;
					case 9: tt = Terrain.CHASM; break;
					case 10: tt = Terrain.GRASS; break;
				}

				level.set(this.left + 1 + x, this.top + 1 + y, tt);
			}
		}
	}

	@Override
	public int getMinHeight() {
		return this.data.h + 2;
	}

	@Override
	public int getMinWidth() {
		return this.data.w + 2;
	}

	@Override
	public int getMaxWidth() {
		return this.data.w + 3;
	}

	@Override
	public int getMaxHeight() {
		return this.data.h + 3;
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 16;
		}

		return 4;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}
}