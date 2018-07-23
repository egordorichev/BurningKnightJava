using System;

namespace BurningKnight.util.math
{
	public static class MathUtils
	{
		public static float Clamp(float min, float max, float val)
		{
			return Math.Max(min, Math.Min(max, val));
		}
	}
}