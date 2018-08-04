using System.Collections.Generic;
using BurningKnight.Util.Files;
using Newtonsoft.Json;

namespace BurningKnight.Assets.Locales
{
	public class Locale
	{
		public string id;
		private Dictionary<string, string> locale = new Dictionary<string, string>();

		public void Put(string id, string key)
		{
			locale[id] = key;
		}

		public string Get(string id)
		{
			return !locale.ContainsKey(id) ? (id == LocaleManager.Default ? "Missing locale" : LocaleManager.Get(id, LocaleManager.Default)) : locale[id];
		}

		public void Load()
		{
			Log.Info("Loading " + id + " locale");
			
			FileHandle dir = FileHandle.FromRoot("Locales/");
			
			Log.Info(dir.FullPath);

			if (!dir.Exists())
			{
				Log.Warn("No Locales directory found");
				return;
			}

			FileHandle self = dir.FindFile(id + ".json");

			if (!self.Exists())
			{
				Log.Warn("Did not find the locale file");
				return;
			}
			
			string json = self.ReadAll();
			var deserialized = JsonConvert.DeserializeObject<Dictionary<string, string>>(json);

			foreach (var reg in deserialized)
			{
				Put(reg.Key, reg.Value);	
			}
		}
	}
}