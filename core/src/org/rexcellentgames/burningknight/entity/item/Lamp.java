package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class Lamp extends Item {
	public static Lamp instance;

	{
		name = Locale.get("lamp");
		sprite = "item (lamp)";
		description = Locale.get("lamp_desc");
		identified = true;
		useTime = 0.2f;
		cursed = true;
	}

	public float val = Dungeon.type == Dungeon.Type.INTRO ? 90f : 100f;
	private boolean lightUp;
	private boolean added;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion sprite = this.getSprite();

		float xx = x + (flipped ? -w / 2 : w / 2);

		if (this.r > 0) {
			Graphics.batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			Graphics.shape.setColor(1, 0.5f, 0, 0.4f);
			Graphics.shape.circle(x + w / 2 + (flipped ? -w/4 : w/4), y + sprite.getRegionHeight() / 2,
				(float) (10f + Math.cos(Dungeon.time * 3) * Math.sin(Dungeon.time * 4)) * r);

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();
		}

		Graphics.render(sprite, xx + sprite.getRegionWidth() / 2, y, 0, sprite.getRegionWidth() / 2, 0, flipped, false);
	}

	@Override
	public void setOwner(Creature owner) {
		super.setOwner(owner);

		Camera.follow(Player.instance, false);

		if (!this.added  && Dungeon.type != Dungeon.Type.INTRO) {
			this.added = true;

			if (BurningKnight.instance == null) {
				BurningKnight knight = new BurningKnight();

				Dungeon.area.add(knight);
				knight.attackTp = true;
				knight.findStartPoint();
			}

			BurningKnight.instance.become("fadeIn");

			Camera.shake(10);

			BurningKnight.instance.dialog = BurningKnight.onLampTake;
			PlayerSave.add(BurningKnight.instance);
		}
	}

	private float r;

	@Override
	public void init() {
		super.init();
		instance = this;
	}

	@Override
	public void use() {
		super.use();

		this.lightUp = !this.lightUp;

		Tween.to(new Tween.Task(this.lightUp ? 1 : 0, 0.4f) {
			@Override
			public float getValue() {
				return r;
			}

			@Override
			public void setValue(float value) {
				r = value;
			}
		});

		play();
	}

	public boolean isOn() {
		return this.lightUp;
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeFloat(this.val);
		writer.writeBoolean(this.lightUp);
		writer.writeBoolean(this.added);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.val = reader.readFloat();
		this.lightUp = reader.readBoolean();
		this.added = reader.readBoolean();

		if (this.lightUp) {
			this.r = 1;
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.lightUp) {
			if (this.val > 0) {
				this.val = Math.max(this.val - dt, 0);
				Dungeon.level.addLightInRadius(this.owner.x + 8, this.owner.y + 8, 0, 0, 0, 2f * (this.val / 100 + 0.3f), 8f, false);
			} else {
				this.lightUp = false;
			}
		}/* else {
			if (BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive") && !Player.instance.isDead()) {
				float d = this.owner.getDistanceTo(BurningKnight.instance.x + BurningKnight.instance.w / 2,
					BurningKnight.instance.y + BurningKnight.instance.h / 2) / 16;

				if (d <= BurningKnight.LIGHT_SIZE - 1) {
					float v = Dungeon.level.getLight(Math.round(this.owner.x / 16), Math.round(this.owner.y / 16));

					if (Random.newFloat() < 1f / (d * 2)) {
						ChargeFx fx = new ChargeFx(BurningKnight.instance.x + BurningKnight.instance.w / 2 - 2, BurningKnight.instance.y + BurningKnight.instance.h / 2 - 2, 0.3f);
						fx.owner = this.owner;

						Dungeon.area.add(fx);
					}
				}
			}
		}*/
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n");
		builder.append(Math.round(this.val));
		builder.append("% charged");

		return builder;
	}

	@Override
	public int getValue() {
		return Math.round(this.val);
	}

	@Override
	public void secondUse() {
		super.secondUse();

		if (BurningKnight.instance != null) {
			float d = this.owner.getDistanceTo(BurningKnight.instance.x + BurningKnight.instance.w / 2,
				BurningKnight.instance.y + BurningKnight.instance.h / 2) / 16;

			if (d < 64f) {
				BurningKnight.instance.become("fadeOut");
				BurningKnight.instance.attackTp = true;
			}
		}

		play();
	}

	public static void play() {
		Audio.playSfx("curse_lamp_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));
	}
}