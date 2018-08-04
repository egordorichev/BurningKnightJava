using BurningKnight.Entities.Physics;
using BurningKnight.Util.Math;

namespace BurningKnight.Entities.Level
{
  public class Level : PhysicsEntity
  {
    private byte[] data;
    private byte[] decor;
    private byte[] liquid;
    private int size;
    public int height = 16;
    public int width = 16;

    public Level()
    {
      alwaysDraw = true;
      alwaysUpdate = true;
      depth = -10;
    }

    public void InitData()
    {
      size = width * height;

      data = new byte[size];
      decor = new byte[size];
      liquid = new byte[size];
    }

    public int Validate(int i)
    {
      return (int) Mathf.Clamp(0, size - 1, i);
    }

    public int ToIndex(int x, int y)
    {
      return Validate(x + y * width);
    }

    public byte Get(int x, int y)
    {
      return data[ToIndex(x, y)];
    }

    public byte Get(int i)
    {
      return data[Validate(i)];
    }

    public void Set(int x, int y, byte v)
    {
      data[ToIndex(x, y)] = v;
    }

    public void Set(int i, byte v)
    {
      data[Validate(i)] = v;
    }
  }
}