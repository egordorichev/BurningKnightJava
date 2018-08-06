using BurningKnight.Util.Files;

namespace BurningKnight.Assets.Saves
{
	public static class SaveManager
	{
		public const string SaveDirectory = AssetsHelper.ContentRoot + "Saves/";
		public static byte CurrentSaveSlot = 0;
		
		public enum SaveType
		{
			Level,
			Player,
			Global
		}

		public static string GetSave(SaveType type)
		{
			switch (type)
			{
				case SaveType.Level: return SaveDirectory + "slot-" + CurrentSaveSlot + "/level.save";
				case SaveType.Player: return SaveDirectory + "slot-" + CurrentSaveSlot + "/player.save";
				case SaveType.Global: default: return SaveDirectory + "global.save";
			}
		}
		
		public static void Load(SaveType type)
		{
			FileHandle handle = new FileHandle(GetSave(type));

			if (!handle.Exists())
			{
				Log.Info("Save does not exist, generating a new one");
				Generate(type);
				return;
			}
			
			Log.Info("Loading " + type + ", slot-" + CurrentSaveSlot);
			
			switch (type)
			{
				case SaveType.Level:
					LevelSave.Load(new FileReader(handle));
					break;
				case SaveType.Player:
					PlayerSave.Load(new FileReader(handle));
					break;
				case SaveType.Global:
					GlobalSave.Load(new FileReader(handle));
					break;
			}
		}

		public static void LoadAll()
		{
			Load(SaveType.Global);
			Load(SaveType.Level);
			Load(SaveType.Player);
		}

		public static void Save(SaveType type)
		{
			Log.Info("Saving " + type + ", slot-" + CurrentSaveSlot);

			string path = GetSave(type);
			
			switch (type)
			{
				case SaveType.Level:
					LevelSave.Save(new FileWriter(path));
					break;
				case SaveType.Player:
					PlayerSave.Save(new FileWriter(path));
					break;
				case SaveType.Global:
					GlobalSave.Save(new FileWriter(path));
					break;
			}
		}

		public static void Generate(SaveType type)
		{
			Log.Info("Generating " + type + ", slot-" + CurrentSaveSlot);
			
			switch (type)
			{
				case SaveType.Level:
					LevelSave.Generate();
					break;
				case SaveType.Player:
					PlayerSave.Generate();
					break;
				case SaveType.Global:
					GlobalSave.Generate();
					break;
			}
			
			// And save, ofc
			Save(type);
		}
	}
}