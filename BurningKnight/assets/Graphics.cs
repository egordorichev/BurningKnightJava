using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.assets
{
	public class Graphics : AssetManager
	{
		public static SpriteBatch batch;

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
			batch.GraphicsDevice.Clear(color);
		}
	}
}