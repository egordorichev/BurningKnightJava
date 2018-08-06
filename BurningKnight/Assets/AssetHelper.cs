using System.Collections.Generic;
using BurningKnight.Assets.Locales;
using BurningKnight.Assets.Mods;
using Microsoft.Xna.Framework.Content;

namespace BurningKnight.Assets
{
	public static class AssetsHelper
	{
		public const string ContentRoot = "Content/";
		
		private static List<AssetManager> managers = new List<AssetManager>();
		public static ContentManager content;
		public static ModManager mods;
		
		public static void Load()
		{
			managers.Add(new LocaleManager());
			managers.Add(new Graphics.Graphics());
			managers.Add(new Audio());
			managers.Add(mods = new ModManager());
			
			foreach (var manager in managers)
			{
				manager.LoadAssets();
			}
		}

		public static void Destroy()
		{
			foreach (var manager in managers)
			{
				manager.Destroy();
			}
		}
	}
}