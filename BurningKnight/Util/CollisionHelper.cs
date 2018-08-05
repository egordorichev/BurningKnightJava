namespace BurningKnight.Util
{
	public static class CollisionHelper
	{
		public static bool Check(float l1, float t1, float r1, float b1, float l2, float t2, float r2, float b2)
		{
			return r1 >= l2 &&
        b1 >= t2 &&
        l1 <= r2 &&
        t1 <= b2;
		} 
	}
}