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
		
		private static Dictionary<string, List<InputNames>> bindings = new Dictionary<string, List<InputNames>>();
		private static Dictionary<InputNames, State> states = new Dictionary<InputNames, State>();
		private static Dictionary<InputNames, State> next = new Dictionary<InputNames, State>();

		public static void Init()
		{
			Bind("move_left", InputNames.ControllerX);
		}

		public static void Bind(string id, InputNames key)
		{
			if (!bindings.ContainsKey(id))
			{
				bindings[id] = new List<InputNames>();
			}
			
			bindings[id].Add(key);
			states[key] = State.RELEASED;
		}

		private static int mouseWheelValue;
		public static int MouseWheelValue => mouseWheelValue;

		private static Vector2 leftStick;
		public static Vector2 LeftStick => leftStick;

		private static Vector2 rightStick;
		public static Vector2 RightStick => rightStick;

		public static void Update(float dt)
		{
			KeyboardState state = Keyboard.GetState();
			MouseState mouse = Mouse.GetState();

			mouseWheelValue = mouse.ScrollWheelValue;
			
			GamePadState gamepad = GamePad.GetState(PlayerIndex.One, 
				GamePadDeadZone.Circular);
			
			next.Clear();
			
			foreach (var pair in states)
			{
				bool down = false;

				switch (pair.Key)
				{
					case InputNames.MouseWheel:
						down = mouse.ScrollWheelValue != 0;
						break;
					case InputNames.MouseRight:
						down = mouse.RightButton == ButtonState.Pressed;
						break;
					case InputNames.MouseLeft:
						down = mouse.LeftButton == ButtonState.Pressed;
						break;
					case InputNames.MouseMiddle:
						down = mouse.MiddleButton == ButtonState.Pressed;
						break;
					case InputNames.ControllerA:
						down = gamepad.IsConnected && gamepad.Buttons.A == ButtonState.Pressed;
						break;
					case InputNames.ControllerB:
						down = gamepad.IsConnected && gamepad.Buttons.B == ButtonState.Pressed;
						break;
					case InputNames.ControllerX:
						down = gamepad.IsConnected && gamepad.Buttons.X == ButtonState.Pressed;
						break;
					case InputNames.ControllerY:
						down = gamepad.IsConnected && gamepad.Buttons.Y == ButtonState.Pressed;
						break;
					case InputNames.ControllerStart:
						down = gamepad.IsConnected && gamepad.Buttons.Start == ButtonState.Pressed;
						break;
					case InputNames.ControllerBack:
						down = gamepad.IsConnected && gamepad.Buttons.B == ButtonState.Pressed;
						break;
					case InputNames.ControllerLeftStick:
						down = gamepad.IsConnected && gamepad.Buttons.LeftStick == ButtonState.Pressed;
						break;
					case InputNames.ControllerRightStick:
						down = gamepad.IsConnected && gamepad.Buttons.RightStick == ButtonState.Pressed;
						break;
					case InputNames.ControllerLeftShoulder:
						down = gamepad.IsConnected && gamepad.Buttons.LeftShoulder == ButtonState.Pressed;
						break;
					case InputNames.ControllerRightShoulder:
						down = gamepad.IsConnected && gamepad.Buttons.RightShoulder == ButtonState.Pressed;
						break;
					case InputNames.ControllerDLeft:
						down = gamepad.IsConnected && gamepad.DPad.Left == ButtonState.Pressed;
						break;
					case InputNames.ControllerDRight:
						down = gamepad.IsConnected && gamepad.DPad.Right == ButtonState.Pressed;
						break;
					case InputNames.ControllerDUp:
						down = gamepad.IsConnected && gamepad.DPad.Up == ButtonState.Pressed;
						break;
					case InputNames.ControllerDDown:
						down = gamepad.IsConnected && gamepad.DPad.Down == ButtonState.Pressed;
						break;
					case InputNames.ControllerLeftTrigger:
						// down = gamepad.IsConnected && gamepad.Triggers.Left ???? float ????;
						break;
					case InputNames.ControllerRightTrigger:
						// down = false;
						break;
					case InputNames.ControllerAxisLeft:
						if (gamepad.IsConnected)
						{
							leftStick = gamepad.ThumbSticks.Left;
						}
						break;
					case InputNames.ControllerAxisRight: 
						if (gamepad.IsConnected)
						{
							rightStick = gamepad.ThumbSticks.Right;
						}
						break;
					default: 
						down = state.IsKeyDown((Keys) pair.Key);
						break;
				}

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