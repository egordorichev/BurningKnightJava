using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;

namespace BurningKnight.Game.Inputs
{
	public static class Input
	{
		public enum State
		{
			HELD, RELEASED, PRESSED, UP
		}
		
		private static Dictionary<string, List<Keys>> bindings = new Dictionary<string, List<Keys>>();
		private static Dictionary<Keys, State> states = new Dictionary<Keys, State>();
		private static Dictionary<Keys, State> next = new Dictionary<Keys, State>();

		public static void Init()
		{
			Bind("move_left", Keys.A);
			Bind("move_left", Keys.Left);
		}

		public static void Bind(string id, Keys key)
		{
			if (!bindings.ContainsKey(id))
			{
				bindings[id] = new List<Keys>();
			}
			
			bindings[id].Add(key);
			states[key] = State.RELEASED;
		}

		public static void Update(float dt)
		{
			KeyboardState state = Keyboard.GetState();
			/*MouseState mouse = Mouse.GetState();

			GamePadState gamepad = GamePad.GetState(PlayerIndex.One, 
				GamePadDeadZone.Circular);*/	
			
			
			next.Clear();
			
			foreach (var pair in states)
			{
				bool down = state.IsKeyDown(pair.Key);

				if (down)
				{
					if (pair.Value == State.PRESSED)
					{
						next[pair.Key] = State.HELD;
					} else if (pair.Value != State.HELD)
					{
						next[pair.Key] = State.PRESSED;
					}
				}
				else
				{
					if (pair.Value == State.RELEASED)
					{
						next[pair.Key] = State.UP;
					} else if (pair.Value != State.UP)
					{
						next[pair.Key] = State.RELEASED;
					}
				}
			}

			foreach (var pair in next)
			{
				states[pair.Key] = pair.Value;
			}
		}

		public static bool WasReleased(string id)
		{
			return CheckState(id, State.RELEASED);
		}

		public static bool WasPressed(string id)
		{
			return CheckState(id, State.PRESSED);
		}

		public static bool IsDown(string id)
		{
			return CheckState(id, State.HELD);
		}

		public static bool CheckState(string id, State state)
		{
			if (!bindings.ContainsKey(id))
			{
				return false;
			}

			foreach (var binding in bindings[id])
			{
				if (states[binding] == state)
				{
					return true;
				}
			}

			return false;
		}
	}
}