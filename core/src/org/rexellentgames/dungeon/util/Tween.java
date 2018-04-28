package org.rexellentgames.dungeon.util;

import java.util.ArrayList;

public class Tween {
	private static ArrayList<Task> tasks = new ArrayList<Task>();
	private static float t = 1.70158f;

	public enum Type {
		LINEAR {
			@Override
			public float get(float p) {
				return p;
			}
		},

		SINE_IN {
			@Override
			public float get(float p) {
				return (float) -Math.cos(p * (Math.PI / 2)) + 1;
			}
		},

		QUAD_IN {
			@Override
			public float get(float p) {
				return p * p;
			}
		},

		QUAD_OUT {
			@Override
			public float get(float p) {
				return (p -= 1) * p * p + 1;
			}
		},

		QUAD_IN_OUT {
			@Override
			public float get(float p) {
				if ((p *= 2) < 1) {
					return 0.5f * p * p * p;
				}

				return 0.5f * ((p -= 2) * p * p + 2);
			}
		},

		BACK_IN {
			@Override
			public float get(float p) {
				return p * p * ((t + 1) * p - t);
			}
		},

		BACK_OUT {
			@Override
			public float get(float p) {
				return (p -= 1) * p * ((t + 1) * p + t) + 1;
			}
		},

		BACK_IN_OUT {
			@Override
			public float get(float p) {
				if ((p *= 2) < 1) {
					return 0.5f * (p * p * (((t *= (1.525f)) +1 ) * p - t));
				}

				return 0.5f * ((p -= 2) * p * (((t *= (1.525f)) + 1) * p + t) + 2);
			}
		};

		public float get(float p) {
			return 0;
		}
	}

	public static void to(Task task) {
		tasks.add(task);
	}

	public static void update(float dt) {
		for (int i = tasks.size() - 1; i >= 0; i--) {
			Task task = tasks.get(i);

			if (task.delay > 0) {
				task.delay -= dt;
				continue;
			}

			task.progress += dt * task.rate;

			float x = (task.progress >= 1 ? task.function(1) : task.function(task.progress));

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
		public float delay;
		public Type type;

		public Task(float end, float t) {
			this(end, t, Type.QUAD_IN);
		}

		public Task(float end, float t, Type type) {
			this.end = end;
			this.start = this.getValue();
			this.rate = 1 / t;
			this.difference = (this.end - this.start);
			this.type = type;
		}

		public Task delay(float d) {
			this.delay = d;
			return this;
		}

		public float getValue() {
			return 0;
		}

		public void setValue(float value) {

		}

		public void onEnd() {

		}

		public float function(float p) {
			return this.type.get(p);
		}
	}
}