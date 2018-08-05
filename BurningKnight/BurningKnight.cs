using System;
using BurningKnight.Assets;
using BurningKnight.Assets.Graphics;
using BurningKnight.Game;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight
{
	public class BurningKnight : Microsoft.Xna.Framework.Game
	{
		public static GraphicsDeviceManager manager;
		private State state;
		
		public BurningKnight()
		{
			Content.RootDirectory = "Content/";
			AssetsHelper.content = Content;
			
			manager = new GraphicsDeviceManager(this);

			int scale = 2;

			manager.PreferMultiSampling = true;
			manager.PreferredBackBufferWidth = Display.Width * scale;
			manager.PreferredBackBufferHeight = Display.Height * scale;
			
			Window.AllowUserResizing = true;
			
			// Todo: min window size
		}
		
		protected override void Initialize()
		{
			base.Initialize();
		}
		
		protected override void LoadContent()
		{
			Window.Title = Version.GenerateTitle();
			Log.Info("Starting Burning Knight " + Version.String);
			
			Graphics.batch = new SpriteBatch(GraphicsDevice);
			
			GraphicsDevice.SamplerStates[0] = SamplerState.PointClamp;

			AssetsHelper.Load();
			SetState(State.InGame);
		}
		
		protected override void UnloadContent()
		{
			state?.Destroy();
			AssetsHelper.Destroy();
		}

		public void SetState(State newState)
		{
			state?.Destroy();
			state = newState;
			state?.Init();
		}
				
		protected override void Update(GameTime gameTime)
		{
			base.Update(gameTime);
			state?.Update(gameTime.ElapsedGameTime.Milliseconds * 0.001f);
		}
		
		protected override void Draw(GameTime gameTime)
		{
			// Render to the texture
			FBOManager.Apply(FBOManager.surface);
			state?.Draw();
			FBOManager.Apply(null);
			
			// Render the texture
			Graphics.Clear(Color.Black);
			
			int w = GraphicsDevice.PresentationParameters.BackBufferWidth;
			int h = GraphicsDevice.PresentationParameters.BackBufferHeight;

			float scale = Math.Min((float) w / Display.Width, (float) h / Display.Height);

			if (Display.PixelPerfect)
			{
				scale = (float) Math.Floor(scale);
			}

			int tw = (int) Math.Ceiling(scale * Display.Width);
			int th = (int) Math.Ceiling(scale * Display.Height);
			
			Graphics.batch.Begin(SpriteSortMode.Immediate, BlendState.AlphaBlend, 
				SamplerState.PointClamp, DepthStencilState.Default, 
				RasterizerState.CullNone);
			
			Graphics.batch.Draw(FBOManager.surface, new Rectangle((w - tw) / 2, (h - th) / 2, tw, th), Color.White);
			Graphics.batch.End();
			
			// Whatever
			base.Draw(gameTime);
		}
	}
}