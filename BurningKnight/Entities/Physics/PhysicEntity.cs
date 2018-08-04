using FarseerPhysics.Dynamics;
using Microsoft.Xna.Framework;

namespace BurningKnight.Entities.Physics
{
	public class PhysicEntity : Entity
	{
		protected Body Body;
		protected Vector2 Velocity = new Vector2();

		public override void Update(float dt)
		{
			if (Body != null)
			{
				X = Body.Position.X;
				Y = Body.Position.Y;

				Body.LinearVelocity = Velocity;
			}
			
			base.Update(dt);
		}

		public override void Destroy()
		{
			base.Destroy();
			Body = PhysicWorld.Remove(Body);
		}
	}
}