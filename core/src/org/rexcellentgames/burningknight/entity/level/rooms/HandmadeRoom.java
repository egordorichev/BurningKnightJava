package org.rexcellentgames.burningknight.entity.level.rooms;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.entity.creature.npc.Trader;
import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.creature.player.Spawn;
import org.rexcellentgames.burningknight.entity.level.Control;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.ClassSelector;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

import java.util.ArrayList;
import java.util.HashMap;

public class HandmadeRoom extends RegularRoom {
	public static TiledMap map;
	public static MapLayer rooms;
	public static MapLayer objects;
	public static TiledMapTileLayer tiles;
	public static HashMap<String, RoomData> datas = new HashMap<>();
	public static HashMap<String, ArrayList<MapObject>> sorted = new HashMap<>();

	public static class RoomData {
		byte data[];
		byte w;
		byte h;
		public Rectangle rect;
		public ArrayList<Rect> sub = new ArrayList<>();
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
			} else if (layer.getName().equals("objects")) {
				objects = layer;
			}
		}

		for (MapObject object : rooms.getObjects()) {
			if (object.getName().equals("sub")) {
				continue;
			}

			if (object instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				RoomData data = new RoomData();

				data.rect = rect;
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

				ArrayList<MapObject> list = new ArrayList<>();
				sorted.put(object.getName(), list);

				for (MapObject o : objects.getObjects()) {
					if (o instanceof RectangleMapObject) {
						RectangleMapObject r = (RectangleMapObject) o;
						Rectangle rc = r.getRectangle();

						if (rect.contains(rc)) {
							rc.setX(rc.getX() - rect.getX());
							rc.setY(rc.getY() - rect.getY());

							list.add(o);
						}
					}
				}
			}
		}

		for (MapObject object : rooms.getObjects()) {
			if (!(object instanceof RectangleMapObject) || !object.getName().equals("sub")) {
				continue;
			}

			for (RoomData data : datas.values()) {
				Rectangle r = ((RectangleMapObject) object).getRectangle();

				if (data.rect.overlaps(r)) {
					Rect rect = new Rect();

					rect.left = (int) (r.x - data.rect.x) / 16;
					rect.top = (int) (r.y - data.rect.y) / 16;

					rect.resize((int) r.getWidth() / 16, (int) r.getHeight() / 16);

					data.sub.add(rect);
					break;
				}
			}
		}
	}

	public static void destroy() {
		if (!inited) {
			return;
		}

		map.dispose();
	}

	public void addSubRooms(ArrayList<Room> rooms) {
		Log.error("Adding sub rooms " + this.data.sub.size());

		for (Rect sub : this.data.sub) {
			SubRoom room = new SubRoom();

			room.left = sub.left + 1;
			room.top = sub.top + 1;
			room.resize(sub.getWidth() - 1, sub.getHeight() - 1);

			rooms.add(room);
		}
	}

	public RoomData data;
	private String id;

	public HandmadeRoom(String id) {
		this.data = datas.get(id);

		if (this.data == null) {
			throw new RuntimeException("Handmade " + id + " does not exist!");
		}

		this.id = id;
	}

	protected void parse(ArrayList<MapObject> list) {
		float x = this.left * 16;
		float y = this.top * 16;

		for (MapObject o : list) {
			String name = o.getName();

			if (name == null) {
				continue;
			}

			Rectangle rect = ((RectangleMapObject) o).getRectangle();

			if (name.startsWith("class_")) {
				ClassSelector c = new ClassSelector(name.replace("class_", ""));

				c.x = x + rect.x + (rect.width - c.w) / 2 + 16;
				c.y = y + rect.y + (rect.height - c.h) / 2 + 16;

				Dungeon.area.add(c.add());
			} else if (name.startsWith("control_")) {
				Control c = new Control();

				c.id = name.replace("control_", "");
				c.x = x + rect.x + 16;
				c.y = y + rect.y + 16;

				Dungeon.area.add(c.add());
			} else if (name.startsWith("sk_")) {
				Log.error("Add trader :(");

				String id = name.replace("sk_", "");

				Trader trader = new Trader();

				trader.id = id;
				trader.x = x + rect.x + 16;
				trader.y = y + rect.y + 16 - 8;

				if (id.equals("b") || Version.debug) { // fixme, remove
					trader.saved = true;
				}

				Dungeon.area.add(trader.add());
			} else if (name.startsWith("sp_")) {
				String id = name.replace("sp_", "");

				Upgrade trader = new Upgrade();

				trader.x = x + rect.x + 16;
				trader.y = y + rect.y + 16 - 8;
				trader.setIdd(id);

				switch (id) {
					case "a": trader.setType(Upgrade.Type.ACCESSORY); break;
					case "d": trader.setType(Upgrade.Type.WEAPON); break;
					case "c": trader.setType(Upgrade.Type.CONSUMABLE); break;
					case "h": trader.setType(Upgrade.Type.DECOR); break;
				}

				Dungeon.area.add(trader.add());
			} else if (name.equals("start")) {
				Spawn spawn = new Spawn();

				spawn.x = x + rect.x + 16;
				spawn.y = y + rect.y + 16;

				Dungeon.area.add(spawn.add());
			} else {
				Log.error("Unknown entity " + name);
			}
		}
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
				boolean dr = false;

				switch (t) {
					case 1: tt = Terrain.FLOOR_A; break;
					case 2: tt = Terrain.FLOOR_B; break;
					case 3: tt = Terrain.FLOOR_D; break;
					case 4: tt = Terrain.FLOOR_C; break;
					case 5: tt = Terrain.DIRT; break;
					case 6: tt = Terrain.LAVA; break;
					case 7: tt = Terrain.WATER; break;
					case 8: tt = Terrain.WALL; break;
					case 9: tt = Terrain.CHASM; break;
					case 10: tt = Terrain.GRASS; break;
					case 11:
						int xx = x + this.left + 1;
						int yy = y + this.left + 1;

						dr = true;
						Door door = new Door(xx, yy, this.data.data[x - 1 + (y * this.data.w)] != 8);
						door.add();
						Dungeon.area.add(door);
					break;
					case 12: tt = Terrain.CRACK; break;
					case 13:
						dr = true;
						Exit exit = new Exit();

						exit.x = (x + this.left + 1) * 16;
						exit.y = (y + this.top + 1) * 16 - 8;

						Dungeon.level.set(this.left + x + 1, this.top + y + 1, Terrain.FLOOR_B);
						Dungeon.level.set(this.left + x + 1, this.top + y + 1, Terrain.EXIT);

						Dungeon.area.add(exit.add());
						break;
					case 14:
						dr = true;
						Entrance entrance = new Entrance();

						entrance.x = (x + this.left + 1) * 16;
						entrance.y = (y + this.top + 1) * 16 - 6;

						Dungeon.area.add(entrance.add());
					break;
					default: Log.error("Unknown tile " + t);
				}

				if (!dr) {
					level.set(this.left + 1 + x, this.top + 1 + y, tt);
				}
			}
		}

		this.parse(sorted.get(id));
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