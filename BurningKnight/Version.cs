using System.Collections.Generic;
using BurningKnight.Util.Math;

namespace BurningKnight
{
  public static class Version
  {
    public const double MAJOR = 0.1;
    public const double MINOR = 0.1;
    public const bool DEBUG = true;
    public static string String { get; } = MAJOR + "." + MINOR + (DEBUG ? " dev" : "");

    private static readonly List<string> titles = new List<string>(new[]
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
      if (Random.Chance(0.1f)) titles.Add("You feel lucky");

      if (Random.Chance(0.01f)) titles.Add("You feel very lucky");

      if (Random.Chance(0.001f)) titles.Add("This title should never appear, like really");
    }

    public static string GenerateTitle()
    {
      return "Burning Knight " + String + ": " + titles[Random.Int(titles.Count)];
    }
  }
}