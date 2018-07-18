package org.rexcellentgames.burningknight.entity.level.entities.decor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;

public class Cobweb {
	public static TextureRegion textures[] = new TextureRegion[] {
		Graphics.getTexture("props-cobweb_top_left"),
		Graphics.getTexture("props-cobweb_top_right"),
		Graphics.getTexture("props-cobweb_bottom_left"),
		Graphics.getTexture("props-cobweb_bottom_right")
	};

	public int side;
}