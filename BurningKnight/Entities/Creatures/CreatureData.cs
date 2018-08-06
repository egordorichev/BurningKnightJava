using System.Collections.Generic;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures
{
	public class CreatureData
	{
		public Table table;
		public Table data;
		public Dictionary<string, DynValue[]> states = new Dictionary<string, DynValue[]>();
	}
}