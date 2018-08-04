using System.Collections.Generic;
using Microsoft.Xna.Framework.Content;

namespace BurningKnight.assets
{
  public static class Assets
  {
    public static ContentManager content;
    public static Mods Mods { get; private set; }

    private static readonly List<AssetManager> managers = new List<AssetManager>();
    
    public static void Load()
    {
      managers.Add(new Graphics());
      managers.Add(new Audio());
      managers.Add(Mods = new Mods());

      foreach (AssetManager manager in managers)
      {
        manager.LoadAssets();
      }
    }

    public static void Destroy()
    {
      foreach (AssetManager manager in managers)
      {
        manager.Destroy();
      }
    }
  }
}