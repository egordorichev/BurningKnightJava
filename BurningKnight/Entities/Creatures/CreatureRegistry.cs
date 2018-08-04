using System.Collections.Generic;
using BurningKnight.Entities.Creatures.Enemies;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures
{
	public class CreatureRegistry
	{
		private static Dictionary<string, CreatureData> enemies = new Dictionary<string, CreatureData>();

		public static Creature Create(string id)
		{
			CreatureData data = enemies[id];

			if (data == null)
			{
				return null;
			}
			
			ScriptedCreature c = new ScriptedCreature();

			c.states = data.states;
			c.id = id;
			
			// todo: parse
			Table table = data.table;

			return c;
		}

		public static void Define(string id, CreatureData data)
		{
			enemies[id] = data;
		}

		public static void AddState(string id, string state, DynValue[] functions)
		{
			CreatureData data = enemies[id];

			if (data == null)
			{
				return;
			}

			data.states[state] = functions;
		}
	}
}