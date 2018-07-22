package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;

public class UiImageButton extends UiButton {
	private TextureRegion region;

	{
		depth = 19;
		isSelectable = false;
		alwaysActive = true;
		alwaysRender = true;
	}

	public UiImageButton(String texture, int x, int y) {
		super(null, x, y);

		region = Graphics.getTexture(texture);
		w = region.getRegionWidth();
		h = region.getRegionHeight();
	}

	@Override
	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.render(region, this.x + w / 2 ,this.y + h / 2, 0,
			w / 2, h / 2, false, false, this.scale, this.scale);
	}
}