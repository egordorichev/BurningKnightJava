using System.Collections.Generic;
using BurningKnight.Entities;

namespace BurningKnight.Game
{
  public class Area
  {
    private readonly List<Entity> entities = new List<Entity>();

    public void Add(Entity entity)
    {
      entities.Add(entity);
    }

    public void Update(float dt)
    {
      for (int i = entities.Count - 1; i >= 0; i--)
      {
        Entity entity = entities[i];

        if (entity.ShouldUpdate())
        {
          entity.Update(dt);
        }

        if (entity.shouldRemove)
        {
          entities.Remove(entity);
        }
      }
    }

    public void Draw()
    {
      // todo: sort

      foreach (Entity entity in entities)
      {
        entity.Draw();
      }
    }
  }
}