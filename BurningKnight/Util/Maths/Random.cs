using System;
using BurningKnight.Util.Files;

namespace BurningKnight.Util.Maths
{
	public class Random
	{
		private static System.Random random;

		static Random()
		{
			var seed = Guid.NewGuid().GetHashCode();
			random = new System.Random(seed);
			
			Log.Info("Setting random seed to " + seed);
		}
		
		public static float Float(float min, float max) {
			return (float) (min + random.NextDouble() * (max - min));
		}

		public static float FloatDice(float min, float max) {
			return (Float(min, max) + Float(min, max)) / 2;
		}

		public static float Float(float max) {
			return (float) (random.NextDouble() * max);
		}

		public static float Float() {
			return (float) random.NextDouble();
		}

		public static int Int(int max) {
			return max > 0 ? (int) (random.NextDouble() * max) : 0;
		}

		public static int Int(int min, int max) {
			return min + (int) (random.NextDouble() * (max - min));
		}

		public static bool Chance(float a) {
			return Float(100) <= a;
		}

		public static int Chances(float[] chances) {
			int length = chances.Length;
			float sum = 0;

			foreach (var chance in chances) {
				sum += chance;
			}

			float value = Float(sum);
			sum = 0;

			for (int i = 0; i < length; i++) {
				sum += chances[i];

				if (value < sum) {
					return i;
				}
			}

			return -1;
		}
	}
}