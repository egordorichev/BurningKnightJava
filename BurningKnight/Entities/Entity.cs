using System;

namespace BurningKnight.Entities
{
	public class Entity
	{
		public float x;
		public float y;
		public float w = 16;
		public float h = 16;

		public float Cx => x + w / 2;
		public float Cy => y + h / 2;

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
	}
}