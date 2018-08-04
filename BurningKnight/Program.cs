using System;

namespace BurningKnight
{
  public static class Program
  {
    [STAThread]
    private static void Main()
    {
      using (BurningKnight game = new BurningKnight())
      {
        game.Run();
      }
    }
  }
}