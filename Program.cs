using System;

namespace BurningKnight
{
	public static class Program
	{
		[STAThread]
		static void Main()
		{
			using (var game = new BurningKnight())
				game.Run();
		}
	}
}