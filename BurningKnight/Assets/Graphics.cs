using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.Assets
{
	public class Graphics : AssetManager
	{
		public static SpriteBatch Batch;

		/*
		 * Asset loading
		 */
		
		public override void LoadAssets()
		{
			base.LoadAssets();

			
		}
		
		/*
		 * Static methods
		 */

		public static void clear(Color color)
		{
			Batch.GraphicsDevice.Clear(color);
		}
	}
}