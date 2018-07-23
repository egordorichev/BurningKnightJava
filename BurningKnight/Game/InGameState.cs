﻿using BurningKnight.assets;
using BurningKnight.entity.physics;
using Microsoft.Xna.Framework;

namespace BurningKnight.game
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
    }

    public override void Draw()
    {
      Graphics.clear(Color.Black);
    }
  }
}