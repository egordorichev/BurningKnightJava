using System.Collections.Generic;
using BurningKnight.Entities;
using BurningKnight.Util.Files;

namespace BurningKnight.Assets.Saves
{
	public class PlayerSave
	{
		private static List<SaveableEntity> list = new List<SaveableEntity>();

		public static void Add(SaveableEntity e)
		{
			list.Add(e);
		}

		public static void Remove(SaveableEntity e)
		{
			list.Remove(e);
		}
		
		public static void Save(FileWriter stream)
		{
			
		}

		public static void Load(FileReader stream)
		{
			
		}

		public static void Generate()
		{
			
		}
	}
}