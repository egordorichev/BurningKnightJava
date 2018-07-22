using BurningKnight.assets;
using BurningKnight.game;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight
{
	public class BurningKnight : Game
	{
		public static GraphicsDeviceManager manager;
		private State state;
		
		public BurningKnight()
		{
			manager = new GraphicsDeviceManager(this)
			{
				PreferMultiSampling = false
			};

			Window.AllowUserResizing = true;
		}
		
		protected override void Initialize()
		{
			base.Initialize();
		}
		
		protected override void LoadContent()
		{
			Graphics.batch = new SpriteBatch(GraphicsDevice);
			Assets.Load();
			SetState(State.INGAME);
		}
		
		protected override void UnloadContent()
		{
			state?.Destroy();
			Assets.Destroy();
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
			state?.Update(gameTime);
		}
		
		protected override void Draw(GameTime gameTime)
		{
			state?.Draw();
			base.Draw(gameTime);
		}
	}
}