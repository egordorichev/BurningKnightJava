using System;
using System.Collections.Generic;
using FarseerPhysics.Dynamics;
using Microsoft.Xna.Framework;

namespace BurningKnight.Entities.Physics
{
  public static class PhysicsWorld
  {
    private const float TIME_STEP = 1.0f / 60.0f;

    private static World world;
    private static bool initialized;
    private static readonly List<Body> toRemove = new List<Body>();
    private static float accumulator;

    public static void Init()
    {
      if (initialized)
      {
        return;
      }

      world = new World(Vector2.One);
      initialized = true;
    }

    public static void Destroy()
    {
      if (!initialized)
      {
        return;
      }

      world.Clear();
      toRemove.Clear();
      initialized = false;
    }

    public static void Update(float dt)
    {
      if (!initialized)
      {
        return;
      }

      accumulator += Math.Min(dt, 0.25f);

      while (accumulator >= TIME_STEP)
      {
        world.Step(TIME_STEP);
        accumulator -= TIME_STEP;
      }

      if (toRemove.Count <= 0)
      {
        return;
      }

      foreach (Body body in toRemove) world.RemoveBody(body);

      toRemove.Clear();
    }

    public static void Remove(ref Body body)
    {
      if (body != null)
      {
        toRemove.Add(body);

        body = null;
      }
    }
  }
}