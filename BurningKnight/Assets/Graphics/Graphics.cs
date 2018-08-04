using BurningKnight.Util.Animations;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.Assets.Graphics
{
	public class Graphics : AssetManager
	{
		public static SpriteBatch batch;
		public static TextureAtlas atlas;

		private static Color color = Color.White;

		public static Color Color
		{
			get => color;
			set => color = value;
		}

		/*
		 * Asset loading
		 */
		
		public override void LoadAssets()
		{
			base.LoadAssets();
			
			atlas = new TextureAtlas();
			atlas.Load(FileHandle.FromRoot("Atlas/atlas.atlas"));
			region = atlas.Get("actor-mummy-gray-run-00");
			Animation animation = new Animation("actor-gobbo");
			anim = animation.Get("idle");
		}

		private static AnimationData anim;
		private static TextureRegion region;
		
		/*
		 * Static methods
		 */

		public static void Clear(Color color)
		{
			batch.GraphicsDevice.Clear(color);
			batch.Begin();
			Draw(region, Vector2.Zero);
			batch.End();
		}

		public static void Draw(TextureRegion region, Vector2 position)
		{
			batch.Draw(region.texture, position, region.source, color);
		}
	}
}