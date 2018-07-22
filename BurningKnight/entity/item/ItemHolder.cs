using BurningKnight.entity.physics;

namespace BurningKnight.entity.item
{
	public class ItemHolder : PhysicEntity
	{
		public Item Item
		{
			set
			{
				// Todo: body, size, texture, etc etc
				Item = value;
			}
		}
	}
}