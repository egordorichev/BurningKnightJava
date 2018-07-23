using System;
using System.Collections.Generic;
using FarseerPhysics.Dynamics;
using Microsoft.Xna.Framework;

namespace BurningKnight.entity.physics
{
  public static class PhysicWorld
  {
    private const float TimeStep = 1 / 60f;

    private static World _world;
    private static bool _inited;
    private static readonly List<Body> _toRemove = new List<Body>();
    private static float _accumulator;

    public static void Init()
    {
      if (_inited) return;

      _world = new World(Vector2.One);
      _inited = true;
    }

    public static void Destroy()
    {
      if (!_inited) return;

      _world.Clear();
      _toRemove.Clear();
      _inited = false;
    }

    public static void Update(float dt)
    {
      if (!_inited) return;

      _accumulator += Math.Min(dt, 0.25f);

      while (_accumulator >= TimeStep)
      {
        _world.Step(TimeStep);
        _accumulator -= TimeStep;
      }

      if (_toRemove.Count <= 0) return;

      foreach (Body body in _toRemove) _world.RemoveBody(body);

      _toRemove.Clear();
    }

    /*
     * Creating / Removing bodies
     */

    public static Body Remove(Body body)
    {
      if (body == null) return null;

      _toRemove.Add(body);
      return null;
    }
  }
}