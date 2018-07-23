using BurningKnight.assets;
using BurningKnight.Game;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework;

namespace BurningKnight
{
  public class BurningKnight : Microsoft.Xna.Framework.Game
  {
    public static GraphicsDeviceManager Manager { get; private set; }
    private State state;
    
    public BurningKnight()
    {
      Manager = new GraphicsDeviceManager(this);

      int scale = 2;

      Manager.PreferMultiSampling = true;
      Manager.PreferredBackBufferWidth = Display.WIDTH * scale;
      Manager.PreferredBackBufferHeight = Display.HEIGHT * scale;

      Window.AllowUserResizing = true;

      Content.RootDirectory = "Content\\bin";
    }

    protected override void Initialize()
    {
      base.Initialize();

      Window.Title = Version.GenerateTitle();
      
      Log.Info("Starting Burning Knight " + Version.String);
    }

    protected override void LoadContent()
    {
      Assets.content = Content;

      Assets.Load();
      
      SetState(State.InGame);
    }

    protected override void UnloadContent()
    {
      state.Destroy();
      
      Assets.Destroy();
      
      Content.Unload();
    }

    public void SetState(State newState)
    {
      state?.Destroy();
      
      state = newState;
      
      state.Init();
    }

    protected override void Update(GameTime gameTime)
    {
      base.Update(gameTime);

      float dt = gameTime.ElapsedGameTime.Milliseconds * 1000f;

      state.Update(dt);
      
      Assets.Mods.Update(dt);
    }

    protected override void Draw(GameTime gameTime)
    {
      state.Draw();
      
      Assets.Mods.Draw();

      base.Draw(gameTime);
    }
  }
}