﻿using BurningKnight.assets;
using BurningKnight.game;
using BurningKnight.util.files;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight
{
	public class BurningKnight : Game
	{
		public static GraphicsDeviceManager Manager;
		private State _state;
		
		public BurningKnight()
		{
			Manager = new GraphicsDeviceManager(this)
			{
				PreferMultiSampling = false
			};

			int scale = 2;
			
			Manager.PreferredBackBufferWidth = Display.Width * scale;
			Manager.PreferredBackBufferHeight = Display.Height * scale;

			Window.AllowUserResizing = true;
			Window.Title = Version.GenerateTitle();
		}
		
		protected override void Initialize()
		{
			base.Initialize();
			Log.info("Starting Burning Knight " + Version.String);
		}
		
		protected override void LoadContent()
		{
			Graphics.Batch = new SpriteBatch(GraphicsDevice);
			Assets.Load();
			SetState(State.INGAME);
		}
		
		protected override void UnloadContent()
		{
			_state?.Destroy();
			Assets.Destroy();
		}

		public void SetState(State newState)
		{
			_state?.Destroy();
			_state = newState;
			_state?.Init();
		}
				
		protected override void Update(GameTime gameTime)
		{
			base.Update(gameTime);
			_state?.Update(gameTime.ElapsedGameTime.Milliseconds * 1000f);
		}
		
		protected override void Draw(GameTime gameTime)
		{
			_state?.Draw();
			base.Draw(gameTime);
		}
	}
}