package org.rexellentgames.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class UiLog {
	public static UiLog instance;
	private ArrayList<Line> lines = new ArrayList<Line>();

	public UiLog() {
		instance = this;
	}

	public void print(String string) {
		System.out.println(string);
		this.lines.add(new Line(string));
	}

	public void update(float dt) {
		for (int i = this.lines.size() - 1; i >= 0; i--) {
			final Line line = this.lines.get(i);

			line.label.act(dt);
			line.time += dt;

			if (line.time > 7f && !line.remove) {
				line.remove = true;

				Tween.to(new Tween.Task(0, 3f) {
					@Override
					public float getValue() {
						return line.a;
					}

					@Override
					public void setValue(float value) {
						line.a = value;
					}

					@Override
					public void onEnd() {
						lines.remove(line);
					}
				});
			}
		}
	}

	public void render() {
		for (int i = 0; i < this.lines.size(); i++) {
			Line line = this.lines.get(i);

			line.label.setPosition(2, i * 18 + 2);
			line.label.draw(Graphics.batch, 1);
			line.label.setColor(1, 1, 1, line.a);
		}
	}

	private class Line {
		public Label label;
		public float time;
		public boolean remove;
		public float a = 1f;

		public Line(String string) {
			this.label = new Label(string, new Label.LabelStyle(Graphics.small, Color.WHITE));
		}
	}
}