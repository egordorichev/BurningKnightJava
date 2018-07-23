using BurningKnight.assets;
using BurningKnight.Entities.Physics;
using Microsoft.Xna.Framework;

namespace BurningKnight.Game
{
  public class InGameState : State
  {
    public override void Init()
    {
      base.Init();
      
      PhysicsWorld.Init();
    }

    public override void Destroy()
    {
      base.Destroy();
      
      PhysicsWorld.Destroy();
    }

    public override void Update(float dt)
    {
      base.Update(dt);
      
      PhysicsWorld.Update(dt);
    }

    public override void Draw()
    {
      Graphics.Clear(Color.Black);
    }
  }
}