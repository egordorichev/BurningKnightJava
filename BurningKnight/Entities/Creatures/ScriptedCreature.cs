using System.Collections.Generic;
using BurningKnight.Util.Files;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures
{
	[MoonSharpUserData]
	public class ScriptedCreature : StatedCreature
	{
		public Dictionary<string, DynValue[]> states;
		
		[MoonSharpUserData]
		public class ScriptedState : State<StatedCreature>
		{
			public DynValue[] callbacks;

			public override void OnEnter()
			{
				base.OnEnter();

				if (!((ScriptedCreature) Self).states.ContainsKey(self.lastState))
				{
					Log.Error(Self.GetType().Name + " has no " + self.lastState + " state");
					return;
				}
				
				callbacks = ((ScriptedCreature) Self).states[Self.lastState];				
				callbacks?[0]?.Function.Call(this, Self);
			}

			public override void Update(float dt)
			{
				base.Update(dt);
				callbacks?[2]?.Function.Call(this, Self, dt);
			}

			public override void OnExit()
			{
				base.OnExit();				
				callbacks?[1]?.Function.Call(this, Self);
			}
		}

		protected override void SetupState(string id)
		{
			if (!states.ContainsKey(id))
			{
				return;
			}
			
			ScriptedState st = new ScriptedState();
			st.callbacks = states[id];
			state = st;
		}
	}
}