using BurningKnight.entity.physics;
using BurningKnight.util.math;

namespace BurningKnight.entity.Level
{
	public class Level : PhysicEntity
	{
		public int Width = 16;
		public int Height = 16;
		private byte[] _data;
		private byte[] _decor;
		private byte[] _liquid;
		private int _size;

		public Level()
		{
			AlwaysDraw = true;
			AlwaysUpdate = true;
			Depth = -10;
		}

		public void InitData()
		{
			_size = Width * Height;
			
			_data = new byte[_size];
			_decor = new byte[_size];
			_liquid = new byte[_size];
		}

		public int Validate(int i)
		{
			return (int) MathUtils.Clamp(0, _size - 1, i);
		}

		public int ToIndex(int x, int y)
		{
			return Validate(x + y * Width);
		}

		public byte Get(int x, int y)
		{
			return _data[ToIndex(x, y)];
		}

		public byte Get(int i)
		{
			return _data[Validate(i)];
		}

		public void Set(int x, int y, byte v)
		{
			_data[ToIndex(x, y)] = v;
		}

		public void Set(int i, byte v)
		{
			_data[Validate(i)] = v;
		}
	}
}