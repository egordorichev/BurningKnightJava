using System;
using System.Collections.Generic;

namespace BurningKnight.Util.Maths
{
	public static class Tween
	{
		public static float Linear(float p)
		{
			return p;
		}
		
		public static float SineIn(float p) {
			return (float) -Math.Cos(p * (Math.PI / 2)) + 1;
		}

		public static float SineOut(float p) {
			return (float) Math.Sin(p * (Math.PI / 2));
		}

		public static float QuadIn(float p) {
			return p * p;
		}

		public static float QuadOut(float p) {
			return (p -= 1) * p * p + 1;
		}

		public static float QuadInOut(float p) {
			if ((p *= 2) < 1) {
				return 0.5f * p * p * p;
			}

			return 0.5f * ((p -= 2) * p * p + 2);
		}
		
		private const float time = 1.70158f;

		public static float BackIn(float p) {
			return p * p * ((time + 1) * p - time);
		}

		public static float BackOut(float p) {
			return (p -= 1) * p * ((time + 1) * p + time) + 1;
		}

		public static float BackInOut(float p) {
			float s = time;
			float t = p;
			if ((t *= 2) < 1) return 0.5f * (t * t * (((s *= (1.525f)) + 1) * t - s));
			return 0.5f * ((t -= 2) * t * (((s *= (1.525f)) + 1) * t + s) + 2);
		}

		private static List<Task> tasks = new List<Task>();
		
		public static void Update(float dt)
		{
			for (int i = tasks.Count - 1; i >= 0; i--) {
				Task task = tasks[i];

				if (!task.RunWhenPaused() && BurningKnight.State.paused) {
					continue;
				}
			
				if (task._delay > 0) {
					task._delay -= dt;
					continue;
				}

				task._progress += dt * task._rate;

				float x = task._progress >= 1 ? task._type(1) : task._type(task._progress);

				task.Set(task._start + x * task._difference);

				if (task._progress >= 1) {
					tasks.Remove(task);
					task.OnEnd();
				}
			}
		}

		public static Task To(Task task)
		{
			tasks.Add(task);
			return task;
		}

		public static void Remove(Task task)
		{
			tasks.Remove(task);
		}
		
		public class Task
		{
			public float _delay;
			public float _progress;
			public float _start;
			public float _difference;
			public float _rate;
			public Func<float, float> _type;
			
			public float Delay
			{
				get => _delay;
				set => _delay = value;
			}

			public Task(float end, float time, Func<float, float> type = null)
			{
				_start = Get();
				_rate = 1 / time;
				_difference = (end - _start);
				_type = type ?? Linear;
			}

			public Func<float> Get = () => 0f;
			public Func<float, float> Set = (v) => v;

			public virtual void OnEnd()
			{
				
			}

			public virtual bool RunWhenPaused()
			{
				return false;
			}
		}
	}
}