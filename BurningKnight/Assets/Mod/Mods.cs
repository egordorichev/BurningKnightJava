using System.Collections.Generic;

namespace BurningKnight.assets
{
	public class Mods : AssetManager
	{
		private List<Mod> _mods = new List<Mod>();

		public Mods()
		{
			AddMod(new Mod("Test mode", "WIP", "test"));
		}

		public void AddMod(Mod mod)
		{
			_mods.Add(mod);
			mod.Init();
		}

		public void Update(float dt)
		{
			foreach (var mod in _mods)
			{
				mod.Update(dt);
			}
		}

		public void Draw()
		{
			foreach (var mod in _mods)
			{
				mod.Draw();
			}
		}

		public override void Destroy()
		{
			base.Destroy();
			
			foreach (var mod in _mods)
			{
				mod.Destroy();
			}
		}
	}
}