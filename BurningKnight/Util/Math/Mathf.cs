namespace BurningKnight.Util.Math
{
  public static class Mathf
  {
    public static float Clamp(float min, float max, float value)
    {
      return System.Math.Max(min, System.Math.Min(max, value));
    }
  }
}