using BurningKnight.Assets.Graphics;
using Comora;
using Microsoft.Xna.Framework;

namespace BurningKnight.Entities
{
	public class Camera : Entity
	{
		public static Camera instance;
		
		private Entity target;
		public Comora.Camera camera;

		public float Left => x - Display.Width / 2;
		public float Top => y - Display.Height / 2;
		public float Right => x + Display.Width / 2;
		public float Bottom => y + Display.Height / 2;

		public Camera()
		{
			alwaysUpdate = true;
			instance = this;
			camera = new Comora.Camera(Graphics.batch.GraphicsDevice);
			camera.ResizeMode = AspectMode.FillStretch;

			x = Display.Width / 2;
			y = Display.Height / 2;
		}
		
		public Entity Target
		{
			get => target;
			set => target = value;
		}

		public override void Update(float dt)
		{
			base.Update(dt);			
			camera.Position = new Vector2(x, y);
		}

		public void Jump()
		{
			if (target == null)
			{
				return;
			}

			x = target.Cx;
			y = target.Cy;
		}

		public static void BeginBatch()
		{
			if (instance != null)
			{
				Graphics.batch.Begin(instance.camera);
			}
			else
			{
				Graphics.batch.Begin();				
			}
		}
	}
}