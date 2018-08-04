using System.Collections.Generic;
using Random = BurningKnight.Util.Maths.Random;

namespace BurningKnight
{
	public static class Version
	{
		public const double Major = 0.1;
		public const double Minor = 0.1;
		public const bool Debug = true;
		public static string String = Major + "." + Minor + (Debug ? " dev" : "");

		private static List<string> _titles = new List<string>(new[]
		{
			"Fireproof",
			"Might burn",
			"'Friendly' fire",
			"Get ready to burn",
			"Do you need some heat?",
			"BBQ is ready!",
			"Hot sales!",
			"AAAAAA",
			"It burns burns burns",
			"Not for children under -1",
			"Unhandled fire",
			"Chili music",
			"Fire trap",
			"On-fire",
			"Hot potatoo",
			"Is this loss?"
		});

		static Version()
		{
			if (Random.Chance(0.1f))
			{
				_titles.Add("You feel lucky");
			}
			
			if (Random.Chance(0.01f))
			{
				_titles.Add("You feel very lucky");
			}
			
			if (Random.Chance(0.001f))
			{
				_titles.Add("This title should never appear");
			}
		}
		
		public static string GenerateTitle()
		{
			return "Burning Knight " + String + ": " + _titles[Random.Int(_titles.Count)];
		}
	}
}