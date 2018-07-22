using System.Collections.Generic;

namespace BurningKnight.assets
{
	public static class Assets
	{		
		private static List<AssetManager> _managers = new List<AssetManager>();
		
		public static void Load()
		{
			_managers.Add(new Graphics());
			_managers.Add(new Audio());
			_managers.Add(new Mods());
			
			foreach (var manager in _managers)
			{
				// Todo: might need TargetAssets()
				
				manager.TargetAssets();
				manager.LoadAssets();
			}
		}

		public static void Destroy()
		{
			foreach (var manager in _managers)
			{
				manager.Destroy();
			}
		}
	}
}