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
			ww = GraphicsDevice.PresentationParameters.BackBufferWidth;
			wh = GraphicsDevice.PresentationParameters.BackBufferHeight;

			scale = Math.Min((float) ww / Display.Width, (float) wh / Display.Height);

			if (Display.PixelPerfect)
			{
				scale = (float) Math.Floor(scale);
			}

			tw = (int) Math.Ceiling(scale * Display.Width);
			th = (int) Math.Ceiling(scale * Display.Height);
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

		private float scale;
		private int tw;
		private int th;
		private int ww;
		private int wh;
		
		private void RenderGame()
		{
			// Render to the texture
			FBOManager.Apply(FBOManager.surface);
			state?.Draw();
			FBOManager.Apply(null);
			
			// Render the texture
			Graphics.Clear(Color.Black);
			
			Graphics.batch.Begin(SpriteSortMode.Immediate, BlendState.AlphaBlend, 
				SamplerState.PointClamp, DepthStencilState.Default, 
				RasterizerState.CullNone);
						
			Graphics.batch.Draw(FBOManager.surface, new Rectangle((ww - tw) / 2, (wh - th) / 2, tw, th), Color.White);
			Graphics.batch.End();
		}

		private void RenderUi()
		{
			// Render to the texture
			FBOManager.Apply(FBOManager.shadows);
			state?.Draw();
			FBOManager.Apply(null);

			// Render the texture
			Graphics.batch.Begin(SpriteSortMode.Immediate, BlendState.AlphaBlend,
				SamplerState.PointClamp, DepthStencilState.Default,
				RasterizerState.CullNone);

			Graphics.batch.Draw(FBOManager.surface, new Rectangle((ww - tw) / 2, (wh - th) / 2, tw, th), Color.White);
			Graphics.batch.End();
		}
	}
}