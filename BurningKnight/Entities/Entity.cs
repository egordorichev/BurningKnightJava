using System;
using BurningKnight.Util;

namespace BurningKnight.Entities
{
	public class Entity
	{
		public float x;
		public float y;
		public float w = 16;
		public float h = 16;
		public bool done;
		
		protected int depth;
		public int Depth => depth;

		public float Cx => x + w / 2;
		public float Cy => y + h / 2;

		protected bool alwaysDraw;
		protected bool alwaysUpdate;

		public bool AlwaysDraw => alwaysDraw;
		public bool AlwaysUpdate => alwaysUpdate;

		private bool onScreen;
		public bool OnScreen => onScreen;

		public void CheckIfOnScreen()
		{
			if (Camera.instance == null)
			{
				onScreen = true;
				return;
			}
			
			onScreen = CollisionHelper.Check(x, y, x + w, y + h, Camera.instance.Left, Camera.instance.Top, Camera.instance.Right, Camera.instance.Bottom);
		}
		
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