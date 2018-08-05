using System;
using BurningKnight.Assets;
using BurningKnight.Assets.Graphics;
using BurningKnight.Game;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Random = BurningKnight.Util.Maths.Random;

namespace BurningKnight
{
	public class BurningKnight : Microsoft.Xna.Framework.Game
	{
		public static GameTime gameTime;
		public static GraphicsDeviceManager manager;
		private static State state;

		public static Area Area => state.area;
		public static Area Ui => state.area;
		public static State State => state;
		
		public BurningKnight()
		{
			Content.RootDirectory = "Content/";
			AssetsHelper.content = Content;
			
			manager = new GraphicsDeviceManager(this);

			int scale = 2;

			manager.PreferMultiSampling = true;
			manager.PreferredBackBufferWidth = Display.Width * scale;
			manager.PreferredBackBufferHeight = Display.Height * scale;
			manager.ApplyChanges();
			
			Window.AllowUserResizing = true;
			Window.ClientSizeChanged += ClientSizeChanged;

			void ClientSizeChanged(object sender, EventArgs e)
			{
				CalculateSizes();
			}

			// Todo: min window size
		}
		
		protected override void Initialize()
		{
			base.Initialize();
			CalculateSizes();
		}

		private void CalculateSizes()
		{
			int ww = GraphicsDevice.PresentationParameters.BackBufferWidth;
			int wh = GraphicsDevice.PresentationParameters.BackBufferHeight;

			float scale = Math.Min((float) ww / Display.Width, (float) wh / Display.Height);

			if (Display.PixelPerfect)
			{
				scale = (float) Math.Floor(scale);
			}

			int tw = (int) Math.Ceiling(scale * Display.Width);
			int th = (int) Math.Ceiling(scale * Display.Height);

			displayRectangle = new Rectangle((ww - tw) / 2, (wh - th) / 2, tw, th);
		}
		
		protected override void LoadContent()
		{
			Log.Info("Starting Burning Knight " + Version.String);
			Window.Title = Version.GenerateTitle();
			
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
			BurningKnight.gameTime = gameTime;
			
			base.Update(gameTime);
			state?.Update(gameTime.ElapsedGameTime.Milliseconds * 0.001f);
		}
		
		protected override void Draw(GameTime gameTime)
		{
			RenderGame();
			RenderUi();
			
			// Whatever
			base.Draw(gameTime);
		}

		private Rectangle displayRectangle;
		
		private void RenderGame()
		{
			Graphics.Clear(Color.Black);
			
			// Render to the texture
			FBOManager.Apply(FBOManager.surface);
			state?.Draw();
			FBOManager.Apply(null);
		}

		private void RenderUi()
		{
			// Render to the texture
			FBOManager.Apply(FBOManager.shadows);

			// Render the game into that buffer
			Graphics.batch.Begin(SpriteSortMode.Immediate, BlendState.AlphaBlend, 
				SamplerState.PointClamp, DepthStencilState.Default, 
				RasterizerState.CullNone);
						
			Graphics.batch.Draw(FBOManager.surface, FBOManager.shadows.Bounds, Color.White);
			Graphics.batch.End();
			
			// Render the ui
			state?.DrawUi();
			FBOManager.Apply(null);

			// Render the texture
			Graphics.batch.Begin(SpriteSortMode.Immediate, BlendState.AlphaBlend,
				SamplerState.PointClamp, DepthStencilState.Default,
				RasterizerState.CullNone);

			Graphics.batch.Draw(FBOManager.shadows, displayRectangle, Color.White);
			Graphics.batch.End();
		}
	}
}