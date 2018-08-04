using System;
using BurningKnight.Util.Files;

namespace BurningKnight.Entities.Creatures
{
	public class StatedCreature : Creature
	{
		protected State<StatedCreature> state;

		public void Become(string id)
		{
			if (lastState == id)
			{
				return;
			}

			state?.OnExit();
			lastState = id;
			SetupState(id);
			
			if (state == null)
			{
				Log.Error(GetType().Name + " doesn't have " + id + " state");
			}
			else
			{
				state.Self = this;
				state.OnEnter();
			}
		}

		public override void Update(float dt)
		{
			base.Update(dt);
			state?.Update(dt);
		}

		protected virtual State<StatedCreature> GetState(string id)
		{
			return null;
		}

		protected virtual void SetupState(string id)
		{
			state = GetState(id);
		}
		
		public class State<T> where T: StatedCreature
		{
			protected T self;

			public float t;
			public T Self
			{
				get { return self; }
				set { self = value; }
			}

			public virtual void OnEnter()
			{
				
			}

			public virtual void OnExit()
			{
				
			}

			public virtual void Update(float dt)
			{
				t += dt;
			}
		}
	}
}