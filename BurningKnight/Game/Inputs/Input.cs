using System.Collections.Generic;
using BurningKnight.Util.Animations;
using BurningKnight.Util.Files;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;
using Newtonsoft.Json;

namespace BurningKnight.Game.Inputs
{
	public static class Input
	{
		public enum State
		{
			Held, Released, Pressed, Up
		}
		
		private static Dictionary<string, List<InputNames>> bindings = new Dictionary<string, List<InputNames>>();
		private static Dictionary<InputNames, State> states = new Dictionary<InputNames, State>();
		private static Dictionary<InputNames, State> next = new Dictionary<InputNames, State>();

		public static void Init()
		{
			FileHandle handle = FileHandle.FromRoot("keys.json");

			if (!handle.Exists())
			{
				Log.Error("keys.json does not exist!");
				return;
			}

			InputHelper root = JsonConvert.DeserializeObject<InputHelper>(handle.ReadAll());

			foreach (var entry in root)
			{
				foreach (var key in entry.Value)
				{
					if (InputNames.TryParse(key, false, out InputNames res))
					{
						Bind(entry.Key, res);						
					}
				}
			}
		}

		public static void Bind(string id, InputNames key)
		{
			if (!bindings.ContainsKey(id))
			{
				bindings[id] = new List<InputNames>();
			}
			
			bindings[id].Add(key);
			states[key] = State.Released;
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
					if (pair.Value == State.Pressed)
					{
						next[pair.Key] = State.Held;
					} else if (pair.Value != State.Held)
					{
						next[pair.Key] = State.Pressed;
					}
				}
				else
				{
					if (pair.Value == State.Released)
					{
						next[pair.Key] = State.Up;
					} else if (pair.Value != State.Up)
					{
						next[pair.Key] = State.Released;
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
			return CheckState(id, State.Released);
		}

		public static bool WasPressed(string id)
		{
			return CheckState(id, State.Pressed);
		}

		public static bool IsDown(string id)
		{
			return CheckState(id, State.Held);
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