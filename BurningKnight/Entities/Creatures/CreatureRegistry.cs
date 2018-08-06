using System.Collections.Generic;
using BurningKnight.Entities.Creatures.Enemies;
using BurningKnight.Util.Files;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures
{
	public class CreatureRegistry
	{
		private static Dictionary<string, CreatureData> enemies = new Dictionary<string, CreatureData>();

		public static ScriptedCreature Create(string id)
		{
			CreatureData data = enemies[id];

			if (data == null)
			{
				Log.Warn("Creature " + id + " is not defined");
				return null;
			}
			
			ScriptedCreature c = new ScriptedCreature();

			c.states = data.states;
			c.id = id;
			
			// todo: parse
			Table table = data.data;

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
				Log.Warn("Creature " + id + " is not defined, failed to add state " + state);
				return;
			}

			data.states[state] = functions;
		}
	}
}