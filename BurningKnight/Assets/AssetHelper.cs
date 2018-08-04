using System.Collections.Generic;

namespace BurningKnight.Assets
{
	public static class AssetsHelper
	{		
		private static List<AssetManager> managers = new List<AssetManager>();
		
		public static void Load()
		{
			managers.Add(new Graphics());
			managers.Add(new Audio());
			managers.Add(new Mods());
			
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