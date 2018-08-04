using System.Collections.Generic;
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
				callbacks = ((ScriptedCreature) Self).states[Self.lastState];				
				callbacks[0]?.Function.Call(this, Self);
			}

			public override void Update(float dt)
			{
				base.Update(dt);
				callbacks[2]?.Function.Call(this, Self, dt);
			}

			public override void OnExit()
			{
				base.OnExit();				
				callbacks[1]?.Function.Call(this, Self);
			}
		}

		protected override void SetupState(string id)
		{
			DynValue[] state = states[id];

			if (state != null)
			{
				ScriptedState st = new ScriptedState();

				st.callbacks = state;
				
				this.state = st;
			}
		}
	}
}