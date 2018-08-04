using System;

namespace BurningKnight.Util.Maths
{
	public class MathUtils
	{
		public static float Clamp(float min, float max, float val)
		{
			return Math.Min(max, Math.Max(min, val));
		}
	}
}