using BurningKnight.Entities.Physics;

namespace BurningKnight.Entities.item
{
  public class ItemHolder : PhysicsEntity
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