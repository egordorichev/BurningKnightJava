namespace BurningKnight.util.math
{
	public class Random
	{
		private static System.Random _random = new System.Random();
		
		public static float Float(float min, float max) {
			return (float) (min + _random.NextDouble() * (max - min));
		}

		public static float FloatDice(float min, float max) {
			return (Float(min, max) + Float(min, max)) / 2;
		}

		public static float Float(float max) {
			return (float) (_random.NextDouble() * max);
		}

		public static float Float() {
			return (float) _random.NextDouble();
		}

		public static int Int(int max) {
			return max > 0 ? (int) (_random.NextDouble() * max) : 0;
		}

		public static int Int(int min, int max) {
			return min + (int) (_random.NextDouble() * (max - min));
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