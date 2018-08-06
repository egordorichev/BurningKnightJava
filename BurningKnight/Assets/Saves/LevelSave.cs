using System;
using System.Collections.Generic;
using BurningKnight.Entities;
using BurningKnight.Util.Files;

namespace BurningKnight.Assets.Saves
{
	public static class LevelSave
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
			stream.WriteInt(list.Count);

			for (int i = 0; i < list.Count; i++)
			{
				SaveableEntity e = list[i];

				stream.WriteString();
			}
		}

		public static void Load(FileReader stream)
		{
			list.Clear();
			
			int count = stream.ReadInt();

			if (count == 0)
			{
				stream.Close();
				return;
			}

			for (int i = 0; i < count; i++)
			{
				string id = stream.ReadString();
				Type type = Type.GetType(id);

				if (type == null)
				{
					Log.Error("Failed to load " + id);
					continue;
				}

				SaveableEntity e = (SaveableEntity) type.GetConstructor(null)?.Invoke(null);

				if (e == null)
				{
					Log.Error("Failed to create " + id + " (no empty constructor?)");
					continue;
				}
				
				list.Add(e);
				BurningKnight.Area.Add(e);
				
				e.Load(stream);
			}
			
			stream.Close();
		}

		public static void Generate()
		{
			
		}
	}
}