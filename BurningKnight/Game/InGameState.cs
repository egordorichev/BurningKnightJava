using BurningKnight.Assets;
using BurningKnight.Assets.Graphics;
using BurningKnight.Entities.Physics;
using BurningKnight.Util.Animations;
using Microsoft.Xna.Framework;

namespace BurningKnight.Game
{
	public class InGameState : State
	{
		public override void Init()
		{
			base.Init();
			PhysicWorld.Init();			
		}

		public override void Destroy()
		{
			base.Destroy();
			PhysicWorld.Destroy();
		}

		public override void Update(float dt)
		{
			base.Update(dt);
			PhysicWorld.Update(dt);
			AssetsHelper.mods.Update(dt);
		}

		public override void Draw()
		{
			Graphics.Clear(Color.Black);
			AssetsHelper.mods.Draw();
		}
	}
}