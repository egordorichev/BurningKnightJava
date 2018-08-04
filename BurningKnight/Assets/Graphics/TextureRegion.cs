using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.Assets.Graphics
{
	public class TextureRegion
	{
		public Rectangle source;
		public Texture2D texture;

		public int X => source.Left;
		public int Y => source.Top;
		public int W => source.Width;
		public int H => source.Height;
	}
}