package org.rexellentgames.dungeon.util;

import java.util.ArrayList;

public class Tween {
	private static ArrayList<Task> tasks = new ArrayList<Task>();

	public static void to(Task task) {
		tasks.add(task);
	}

	public static void update(float dt) {
		for (int i = tasks.size() - 1; i >= 0; i--) {
			Task task = tasks.get(i);
			task.progress += dt * task.rate;

			float x = (task.progress >= 1 ? 1 : task.function(task.progress));

			task.setValue(task.start + x * task.difference);

			if (task.progress >= 1) {
				tasks.remove(i);
				task.onEnd();
			}
		}
	}

	public static class Task {
		public float start;
		public float end;
		public float rate;
		public float progress;
		public float difference;

		public Task(float end, float t) {
			this.end = end;
			this.start = this.getValue();
			this.rate = 1 / t;
			this.difference = (this.end - this.start);
		}

		public float getValue() {
			return 0;
		}

		public void setValue(float value) {

		}

		public void onEnd() {

		}

		public float function(float p) {
			return p * p;
		}
	}
}