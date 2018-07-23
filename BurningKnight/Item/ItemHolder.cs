using BurningKnight.entity.physics;

namespace BurningKnight.entity.item
{
  public class ItemHolder : PhysicEntity
  {
    private Item _item;

    public Item Item
    {
      set
      {
        // Todo: body, size, texture, etc etc
        _item = value;
      }
    }
  }
}