using System.Collections.Generic;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures.Enemies
{
	public class EnemyRegistry
	{
		private static Dictionary<string, EnemyData> enemies = new Dictionary<string, EnemyData>();

		public static Enemy Create(string id)
		{
			EnemyData data = enemies[id];

			if (data == null)
			{
				return null;
			}
			
			Enemy enemy = new Enemy();

			enemy.states = data.states;
			enemy.id = id;
			
			// todo: parse
			Table table = data.table;

			return enemy;
		}

		public static void Define(string id, EnemyData data)
		{
			enemies[id] = data;
		}

		public static void AddState(string id, string state, DynValue[] functions)
		{
			EnemyData data = enemies[id];

			if (data == null)
			{
				return;
			}

			data.states[state] = functions;
		}
	}
}