using System;
using BurningKnight.Entities.Physics;
using BurningKnight.Util.Math;

namespace BurningKnight.Entities.Creature
{
  public class Creature : PhysicsEntity
  {
    private bool dead;
    private int hp = 1;
    private int hpMax = 1;

    public bool Dead
    {
      get => dead;
      set
      {
        if (dead) return;

        dead = value;
        OnDeath();
      }
    }

    public int HpMax
    {
      get => hpMax;
      set
      {
        hpMax = Math.Max(1, value);
        hp = (int) Mathf.Clamp(0, hpMax, hp);
      }
    }

    public int Hp
    {
      get => hp;
      set
      {
        if (dead)
        {
          return;
        }

        hp = (int) Mathf.Clamp(0, hpMax, value);

        if (hp == 0)
        {
          Dead = true;
        }
      }
    }

    protected virtual void OnDeath()
    {
    }
  }
}