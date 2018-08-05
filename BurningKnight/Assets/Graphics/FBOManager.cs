using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.Assets.Graphics
{
	public static class FBOManager
	{
		public static RenderTarget2D surface;
		public static RenderTarget2D shadows;
		
		public static void Init()
		{
			surface = new RenderTarget2D(
				Graphics.batch.GraphicsDevice,
				Display.Width,
				Display.Height,
				false,
				Graphics.batch.GraphicsDevice.PresentationParameters.BackBufferFormat,
				DepthFormat.Depth24);
			
			shadows = new RenderTarget2D(
				Graphics.batch.GraphicsDevice,
				Display.Width,
				Display.Height,
				false,
				Graphics.batch.GraphicsDevice.PresentationParameters.BackBufferFormat,
				DepthFormat.Depth24);
		}

		public static void Resize(int w, int h)
		{
			
		}

		public static void Destroy()
		{
			surface.Dispose();
			shadows.Dispose();
		}

		public static void Apply(RenderTarget2D target)
		{
			Graphics.batch.GraphicsDevice.SetRenderTarget(target);
		}
	}
}