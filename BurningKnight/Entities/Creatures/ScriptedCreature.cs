using System.Collections.Generic;
using MoonSharp.Interpreter;

namespace BurningKnight.Entities.Creatures
{
	[MoonSharpUserData]
	public class ScriptedCreature : StatedCreature
	{
		public Table table;
		protected Dictionary<string, DynValue[]> states;
		
		[MoonSharpUserData]
		public class ScriptedState<T> : State<T> where T: ScriptedCreature
		{
			private DynValue cself;
			private DynValue[] callbacks;

			public override void OnEnter()
			{
				base.OnEnter();
				callbacks = Self.states[Self.lastState];				
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
	}
}