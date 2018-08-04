using System;

namespace BurningKnight.Entities
{
  public class Entity
  {
    public int depth;
    public bool shouldRemove;
    public float h = 16;
    public float w = 16;
    public float x;
    public float y;

    public float Cx => x + w / 2;
    public float Cy => y + h / 2;
    
    protected bool alwaysDraw = false;
    protected bool alwaysUpdate = false;
    
    public virtual void Init()
    {
    }

    public virtual void Destroy()
    {
    }

    public virtual void Update(float dt)
    {
    }

    public virtual void Draw()
    {
    }

    public float GetDx(Entity entity)
    {
      return entity.Cx - Cx;
    }

    public float GetDy(Entity entity)
    {
      return entity.Cy - Cy;
    }

    public float GetAngleTo(Entity other)
    {
      return (float) Math.Atan2(GetDy(other), GetDx(other));
    }

    public float GetDistanceTo(Entity other)
    {
      float dx = GetDx(other);
      float dy = GetDy(other);

      return (float) Math.Sqrt(dx * dx + dy * dy);
    }

    public bool ShouldDraw()
    {
      return alwaysDraw || true; // todo: on screen logic
    }

    public bool ShouldUpdate()
    {
      return alwaysUpdate || true;
    }
  }
}