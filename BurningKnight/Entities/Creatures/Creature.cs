﻿using System;
using BurningKnight.Util.Maths;
using Microsoft.Xna.Framework;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures
{
	[MoonSharpUserData]
	public class Creature : Entity
	{
		public string lastState;
		public string id;
		public Vector2 velocity = new Vector2();

		public int Hp
		{
			get { return hp; }
			set
			{
				if (dead)
				{
					return;
				}
				
				hp = (int) MathUtils.Clamp(0, hpMax, value);

				if (hp == 0)
				{
					// to trigger onDeath()
					Dead = true;
				}
			}
		}

		public int HpMax
		{
			get { return hpMax; }
			set
			{
				hpMax = Math.Max(1, value);
				hp = (int) MathUtils.Clamp(0, hpMax, value);
			}
		}

		public bool Dead
		{
			get { return dead; }
			set
			{
				if (dead)
				{
					return;
				}
				
				dead = value;
				onDeath();
			}
		}

		private bool dead;
		private int hp = 1;
		private int hpMax = 1;

		protected virtual void onDeath()
		{
			
		}
	}
}