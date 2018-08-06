using BurningKnight.Assets;
using BurningKnight.Assets.Graphics;
using BurningKnight.Entities;
using BurningKnight.Entities.Creatures;
using BurningKnight.Entities.Physics;
using BurningKnight.Game.Inputs;
using BurningKnight.Util.Animations;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

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

			Creature player = CreatureRegistry.Create("bk:player");
			area.Add(player);
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
			Camera.BeginBatch();
			area.Draw();
			Graphics.batch.End();
		}

		public override void DrawUi()
		{
			base.DrawUi();
			
			Graphics.batch.Begin();
			AssetsHelper.mods.Draw();
			ui.Draw();
			Graphics.batch.End();
		}
	}
}