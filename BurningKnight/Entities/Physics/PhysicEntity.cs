using FarseerPhysics.Dynamics;
using Microsoft.Xna.Framework;

namespace BurningKnight.Entities.Physics
{
	public class PhysicEntity : SaveableEntity
	{
		protected Body body;
		protected Vector2 velocity = new Vector2();

		public override void Update(float dt)
		{
			if (body != null)
			{
				x = body.Position.X;
				y = body.Position.Y;

				body.LinearVelocity = velocity;
			}
			
			base.Update(dt);
		}

		public override void Destroy()
		{
			base.Destroy();
			body = PhysicWorld.Remove(body);
		}
	}
}