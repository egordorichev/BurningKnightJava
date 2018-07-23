using System;
using BurningKnight.entity.physics;
using BurningKnight.util.math;

namespace BurningKnight.entity.Creature
{
	public class Creature : PhysicEntity
	{
		private int _hp = 1;
		private int _hpMax = 1;
		private bool _dead;

		public bool Dead
		{
			get { return _dead; }
			set
			{
				if (_dead)
				{
					return;
				}
				
				_dead = value;
				OnDeath();
			}
		}

		public int HpMax
		{
			get { return _hpMax; }
			set
			{
				_hpMax = Math.Max(1, value);
				_hp = (int) MathUtils.Clamp(0, _hpMax, _hp);
			}
		}
		
		public int Hp
		{
			get { return _hp; }
			set
			{
				if (_dead)
				{
					return;
				}
				
				_hp = (int) MathUtils.Clamp(0, _hpMax, value);

				if (_hp == 0)
				{
					// Use Dead and not _dead to trigger OnDeath
					Dead = true;
				}
			}
		}

		protected virtual void OnDeath()
		{
			
		}
	}
}