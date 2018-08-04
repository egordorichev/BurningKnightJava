using System.Collections.Generic;

namespace BurningKnight.Items
{
	public static class ItemRegistry
	{
		private static Dictionary<string, ScriptedItem> items = new Dictionary<string, ScriptedItem>();

		public static void Register(string id, ScriptedItem item)
		{
			items[id] = item;
		}

		public static ScriptedItem Create(string id)
		{
			ScriptedItem item = items[id];

			if (item == null)
			{
				return null;
			}
			
			ScriptedItem clone = new ScriptedItem(item.table);

			clone.CopyFrom(item);
			
			return clone;
		}
	}
}