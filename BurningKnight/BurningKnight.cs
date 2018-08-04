using BurningKnight.Assets;
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
		}
		
		protected override void Initialize()
		{
			base.Initialize();
		}
		
		protected override void LoadContent()
		{
			Window.Title = Version.GenerateTitle();
			Log.Info("Starting Burning Knight " + Version.String);
			
			Graphics.Batch = new SpriteBatch(GraphicsDevice);
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
			state?.Draw();
			base.Draw(gameTime);
		}
	}
}