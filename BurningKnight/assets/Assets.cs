using System.Collections.Generic;

namespace BurningKnight.assets
{
	public class Assets
	{		
		private static List<AssetManager> managers = new List<AssetManager>();
		
		public static void Load()
		{
			managers.Add(new Graphics());
			
			foreach (var manager in managers)
			{
				// Todo: might need TargetAssets()
				
				manager.TargetAssets();
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