using System.Collections.Generic;
using BurningKnight.Util.Files;

namespace BurningKnight.Assets.Locales
{
	public class LocaleManager : AssetManager
	{
		public const string Default = "en";
		private static Dictionary<string, Locale> locales = new Dictionary<string, Locale>();
		private static Locale active;
		private static Locale fallBack;

		public override void LoadAssets()
		{
			base.LoadAssets();
			LoadLocale("en");
		}

		public void LoadLocale(string id)
		{
			if (active != null && active.id == id)
			{
				return;
			}

			if (fallBack != null && fallBack.id == id)
			{
				active = fallBack;
				return;
			}

			if (locales.ContainsKey(id))
			{
				active = locales[id];
			}
			else
			{
				Locale locale = new Locale();
				locale.id = id;
				locale.Load();

				active = locale;

				if (id == Default)
				{
					fallBack = locale;
				}

				locales[id] = locale;
			}
		}

		public static string Get(string id, string lang = Default)
		{
			if (active != null && lang == active.id)
			{
				return active.Get(id);
			}

			return locales.ContainsKey(lang) ? locales[lang].Get(id) : (fallBack == null ? "Missing locale" : fallBack.Get(id));
		}
	}
}