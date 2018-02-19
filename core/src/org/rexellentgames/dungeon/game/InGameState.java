package org.rexellentgames.dungeon.game;

public class InGameState extends State {
	private Area area;

	@Override
	public void init() {
		this.area = new Area(this);
	}

	@Override
	public void update(float dt) {
		this.area.update(dt);
	}

	@Override
	public void render() {
		this.area.render();
	}
}