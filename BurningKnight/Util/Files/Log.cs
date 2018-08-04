using System;

namespace BurningKnight.Util.Files
{
	public static class Log
	{
		public static void info(object obj)
		{
			Console.ForegroundColor = ConsoleColor.Green;
			Console.WriteLine(obj);	
			Console.ResetColor();
		}
		
		public static void error(object obj)
		{
			Console.ForegroundColor = ConsoleColor.Red;
			Console.WriteLine(obj);	
			Console.ResetColor();
		}
		
		public static void debug(object obj)
		{
			if (Version.Debug)
			{
				Console.ForegroundColor = ConsoleColor.Blue;
				Console.WriteLine(obj);
				Console.ResetColor();
			}
		}
	}
}