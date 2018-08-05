using BurningKnight.Assets;
using BurningKnight.Assets.Graphics;
using BurningKnight.Entities;
using BurningKnight.Entities.Physics;
using BurningKnight.Util.Animations;
using Microsoft.Xna.Framework;

namespace BurningKnight.Game
{
	public class InGameState : State
	{
		private static AnimationData anim;
		
		public override void Init()
		{
			base.Init();
			PhysicWorld.Init();
			
			area.Add(new Camera());

			Animation animation = new Animation("actor-mummy", "-gray");
			anim = animation.Get("idle");			
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
			
			area.Update(dt);
			ui.Update(dt);
		}

		public override void Draw()
		{
			Graphics.Clear(Color.Black);
			Camera.BeginBatch();
			
			area.Draw();
			anim.Update(0.03f);
			anim.Draw(Vector2.Zero);
			AssetsHelper.mods.Draw();
			
			Graphics.batch.End();
		}

		public override void DrawUi()
		{
			base.DrawUi();
			
			Graphics.Clear(Color.Transparent);
			Graphics.batch.Begin();
			
			ui.Draw();
			
			Graphics.batch.End();
		}
	}
}