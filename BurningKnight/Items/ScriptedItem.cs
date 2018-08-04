using MoonSharp.Interpreter;

namespace BurningKnight.Items
{
	public class ScriptedItem : Item
	{
		public Table table;

		public ScriptedItem(Table table)
		{
			this.table = table;
		}
	}
}