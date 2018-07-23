using System.Collections.Generic;
using Microsoft.Xna.Framework.Content;

namespace BurningKnight.assets
{
  public static class Assets
  {
    private static readonly List<AssetManager> _managers = new List<AssetManager>();
    public static ContentManager Content;
    public static Mods Mods;

    public static void Load()
    {
      _managers.Add(new Graphics());
      _managers.Add(new Audio());
      _managers.Add(Mods = new Mods());

      foreach (AssetManager manager in _managers) manager.LoadAssets();
    }

    public static void Destroy()
    {
      foreach (AssetManager manager in _managers) manager.Destroy();
    }
  }
}