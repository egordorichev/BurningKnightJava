using System.Collections.Generic;
using BurningKnight.Util.Files;

namespace BurningKnight.Assets.Mods
{
	public class ModManager : AssetManager
	{
		private List<Mod> mods = new List<Mod>();
		
		public override void LoadAssets()
		{
			FileHandle mods = FileHandle.FromRoot("Mods/");

			if (!mods.Exists())
			{
				mods.MakeDirectory();
			}
			
			foreach (var file in mods.ListDirectoryHandles())
			{
				ParseDirectory(file);
			}
		}

		public override void Destroy()
		{
			base.Destroy();
			
			foreach (var mod in mods)
			{
				mod.Destroy();
			}
		}

		private void ParseDirectory(FileHandle dir)
		{
			Mod mod = new Mod(dir);
			mods.Add(mod);
			
			mod.Init();
		}

		public void Update(float dt)
		{
			foreach (var mod in mods)
			{
				mod.Update(dt);
			}
		}

		public void Draw()
		{
			foreach (var mod in mods)
			{
				mod.Draw();
			}
		}
	}
}