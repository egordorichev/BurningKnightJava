using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace BurningKnight.assets
{
  public class Graphics : AssetManager
  {
    public static SpriteBatch Batch { get; private set; }

    public override void LoadAssets()
    {
      base.LoadAssets();
      
      Batch = new SpriteBatch(BurningKnight.Manager.GraphicsDevice);
    }

    public static void Clear(Color color)
    {
      Batch.GraphicsDevice.Clear(color);
    }
  }
}