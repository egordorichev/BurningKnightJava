using System;

namespace BurningKnight.entity
{
  public class Entity
  {
    /*
     * On screen logic
     */

    protected bool AlwaysDraw = false;
    protected bool AlwaysUpdate = false;
    public int Depth;
    public bool Done;
    public float H = 16;
    public float W = 16;
    public float X;
    public float Y;

    public float Cx => X + W / 2;
    public float Cy => Y + H / 2;

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

    public float getDx(Entity entity)
    {
      return entity.Cx - Cx;
    }

    public float getDy(Entity entity)
    {
      return entity.Cy - Cy;
    }

    public float getAngleTo(Entity other)
    {
      return (float) Math.Atan2(getDy(other), getDx(other));
    }

    public float getDistanceTo(Entity other)
    {
      float dx = getDx(other);
      float dy = getDy(other);

      return (float) Math.Sqrt(dx * dx + dy * dy);
    }

    public bool ShouldDraw()
    {
      return AlwaysDraw || true; // todo: on screen logic
    }

    public bool ShouldUpdate()
    {
      return AlwaysUpdate || true;
    }
  }
}