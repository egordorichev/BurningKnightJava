using System;
using System.IO;

namespace BurningKnight.Util.Files
{
  public static class Log
  {
    public static bool SaveToFile { get; set; } = true;

    private static readonly string filePath = Path.Combine(Directory.GetCurrentDirectory(), "logs", $"log-{DateTime.Now:yyyy-MM-dd-hh-mm-ss}.txt");

    public static void Info(object obj)
    {
      WriteToFile("INFO", obj);

      Console.ForegroundColor = ConsoleColor.Green;

      Console.WriteLine(obj);

      Console.ResetColor();
    }

    public static void Error(object obj)
    {
      WriteToFile("ERROR", obj);

      Console.ForegroundColor = ConsoleColor.Red;

      Console.WriteLine(obj);

      Console.ResetColor();
    }

    public static void Debug(object obj)
    {
      if (Version.DEBUG)
      {
        WriteToFile("DEBUG", obj);

        Console.ForegroundColor = ConsoleColor.Blue;

        Console.WriteLine(obj);

        Console.ResetColor();
      }
    }

    private static void WriteToFile(string prefix, object obj)
    {
      if (!Directory.Exists(Path.GetDirectoryName(filePath)))
      {
        Directory.CreateDirectory(Path.GetDirectoryName(filePath));
      }

      File.AppendAllText(filePath, $"{DateTime.Now.ToLongTimeString()} [{prefix}]: {obj}\n");
    }
  }
}