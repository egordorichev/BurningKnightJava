using System;

namespace BurningKnight.Util.Files
{
	public static class Log
	{
		public static void Info(object obj)
		{
			Console.ForegroundColor = ConsoleColor.Green;
			Console.WriteLine(obj);	
			Console.ResetColor();
		}
		
		public static void Error(object obj)
		{
			Console.ForegroundColor = ConsoleColor.Red;
			Console.WriteLine(obj);	
			Console.ResetColor();
		}
		
		public static void Warn(object obj)
		{
			Console.ForegroundColor = ConsoleColor.DarkYellow;
			Console.WriteLine(obj);	
			Console.ResetColor();
		}
		
		public static void Lua(object obj)
		{
			Console.ForegroundColor = ConsoleColor.Cyan;
			Console.WriteLine(obj);	
			Console.ResetColor();
		}
		
		public static void Debug(object obj)
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