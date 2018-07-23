using System.Collections.Generic;

namespace BurningKnight.assets
{
  public class Mods : AssetManager
  {
    private readonly List<Mod> mods = new List<Mod>();

    public Mods()
    {
      AddMod(new Mod("Test mod", "WIP", "test"));
    }

    public void AddMod(Mod mod)
    {
      mods.Add(mod);
      mod.Init();
    }

    public void Update(float dt)
    {
      foreach (Mod mod in mods)
      {
        mod.Update(dt);
      }
    }

    public void Draw()
    {
      foreach (Mod mod in mods)
      {
        mod.Draw();
      }
    }

    public override void Destroy()
    {
      base.Destroy();

      foreach (Mod mod in mods)
      {
        mod.Destroy();
      }
    }
  }
}