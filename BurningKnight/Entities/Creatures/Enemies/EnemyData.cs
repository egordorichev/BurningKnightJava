using System.Collections.Generic;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures.Enemies
{
	public class EnemyData
	{
		public Table table;
		public Dictionary<string, DynValue[]> states = new Dictionary<string, DynValue[]>();
	}
}