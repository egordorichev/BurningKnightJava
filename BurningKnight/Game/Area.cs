using System.Collections.Generic;
using BurningKnight.entity;

namespace BurningKnight.game
{
  public class Area
  {
    private readonly List<Entity> _entities = new List<Entity>();

    public void Add(Entity entity)
    {
    }

    public void Update(float dt)
    {
      for (int i = _entities.Count - 1; i >= 0; i--)
      {
        Entity entity = _entities[i];

        if (entity.ShouldUpdate()) entity.Update(dt);

        if (entity.Done) _entities.Remove(entity);
      }
    }

    public void Draw()
    {
      // todo: sort

      foreach (Entity entity in _entities) entity.Draw();
    }
  }
}