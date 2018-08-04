using MoonSharp.Interpreter;

namespace BurningKnight.Items
{
	[MoonSharpUserData]
	public class Item
	{
		public string name;
		public string description;
		public float useTime = 1f;

		private bool stacks;
		private int count;

		public bool Stacks
		{
			get { return stacks; }
			set
			{
				stacks = value;

				if (!stacks)
				{
					count = 1;
				}
			}
		}

		public void LoadNames(string id)
		{
			
		}

		public int Count
		{
			get { return count; }
			set
			{
				if (value > 1 && !stacks)
				{
					return;
				}
				
				count = value;
				// todo: 0 count?
			}
		}

		public void Use()
		{
			
		}

		public bool CanBeUsed()
		{
			return true;
		}

		public void CopyFrom(Item item)
		{
			name = item.name;
			description = item.description;
			useTime = item.useTime;
			Stacks = item.stacks;
		}
	}
}