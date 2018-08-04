using System;
using BurningKnight.Util.Files;

namespace BurningKnight.Entities.Creatures
{
	public class StatedCreature : Creature
	{
		protected State<StatedCreature> state;
		protected String lastState;

		public void Become(String id)
		{
			if (lastState == id)
			{
				return;
			}

			state?.OnExit();
			lastState = id;
			state = GetState(id);

			if (state == null)
			{
				Log.Error(GetType().Name + " doesn't have " + id + " state");
			}
			else
			{
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
		
		public class State<T> where T: StatedCreature
		{
			protected Creature self;

			public Creature Self
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
				
			}
		}
	}
}