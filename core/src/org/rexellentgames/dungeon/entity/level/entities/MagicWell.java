package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.entities.fx.WellFx;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

public class MagicWell extends UsableProp {
	public static ShaderProgram shader;

	static {
		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.internal("shaders/dist.vert").readString();
		fragmentShader = Gdx.files.internal("shaders/dist.frag").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	private static TextureRegion[] water = new TextureRegion[] {
		Graphics.getTexture("prop (water_none)"),
		Graphics.getTexture("prop (water_heal)")
	};

	{
		sprite = "prop (tub)";
		collider = new Rectangle(4, 5, 30 - 8, 12);
	}

	private boolean s;

	@Override
	public boolean use() {
		this.used = true;


		int r = Random.newInt(3);

		switch (r) {
			case 0: default:
				Player.instance.modifyHp(Player.instance.getHpMax() - Player.instance.getHp(), null);
				Log.info("[green]You take a sip and feel refreshed!");
				break;
			/*case 1:

				break;
			case 2:
				break;*/
		}
		return true;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;
			for (int x = (int) (this.x / 16); x < Math.ceil((this.x + 32) / 16); x++) {
				Dungeon.level.setPassable(x, (int) ((this.y + 11) / 16), false);
				Dungeon.level.setPassable(x, (int) ((this.y) / 16), false);
			}
		}
	}

	private WellFx fx;

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player && !this.used) {
			this.fx = new WellFx(this, "take_a_sip");
			Dungeon.area.add(fx);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (this.fx != null && entity instanceof Player) {
			this.fx.remove();
			this.fx = null;
		}
	}

	@Override
	public void render() {
		super.render();

		Graphics.batch.end();
		shader.begin();

		TextureRegion r = water[this.used ? 0 : 1];
		Texture t = r.getTexture();

		shader.setUniformf("time", Dungeon.time);
		shader.setUniformf("pos", new Vector2(((float) r.getRegionX()) / t.getWidth(), ((float) r.getRegionY()) / t.getHeight()));
		shader.setUniformf("size", new Vector2(((float) r.getRegionWidth()) / t.getWidth(), ((float) r.getRegionHeight()) / t.getHeight()));
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();

		Graphics.render(r, this.x + 5, this.y + 8);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}
}