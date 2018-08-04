using BurningKnight.Entities.Physics;

namespace BurningKnight.Item
{
	public class ItemHolder : PhysicEntity
	{
		private Item item;
		
		public Item Item
		{
			set
			{
				// Todo: body, size, texture, etc etc
				item = value;
			}
		}
	}
}