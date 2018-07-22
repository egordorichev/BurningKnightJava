package org.rexcellentgames.burningknight.game.input;

import org.rexcellentgames.burningknight.OS;

public class GamepadMap {
	public enum Type {
		Xbox, PlayStation3, PlayStation4, Unknown
	}

	public final int AXIS_LEFT_TRIGGER;
	public final int AXIS_LEFT_X;
	public final int AXIS_LEFT_Y;
	public final int AXIS_RIGHT_TRIGGER;
	public final int AXIS_RIGHT_X;
	public final int AXIS_RIGHT_Y;
	public final int BUTTON_B;
	public final int BUTTON_BACK;
	public final int BUTTON_DPAD_DOWN;
	public final int BUTTON_DPAD_LEFT;
	public final int BUTTON_DPAD_RIGHT;
	public final int BUTTON_DPAD_UP;
	public final int BUTTON_L1 /* bumper */;
	public final int BUTTON_L2 /* trigger */;
	public final int BUTTON_L3 /* joystick */;
	public final int BUTTON_A;
	public final int BUTTON_R1 /* bumper */;
	public final int BUTTON_R2 /* trigger */;
	public final int BUTTON_R3 /* joystick */;
	public final int BUTTON_START;
	public final int BUTTON_X;
	public final int BUTTON_Y;

	public int getId(String name) {
		switch (name) {
			case "ControllerX": return BUTTON_X;
			case "ControllerY": return BUTTON_Y;
			case "ControllerB": return BUTTON_B;
			case "ControllerA": return BUTTON_A;
			case "ControllerLT": return AXIS_LEFT_TRIGGER;
			case "ControllerLX": return AXIS_LEFT_X;
			case "ControllerLY": return AXIS_LEFT_Y;
			case "ControllerRX": return AXIS_RIGHT_X;
			case "ControllerRY": return AXIS_RIGHT_Y;
			case "ControllerBack": return BUTTON_BACK;
			case "ControllerDPadDown": return BUTTON_DPAD_DOWN;
			case "ControllerDPadUp": return BUTTON_DPAD_UP;
			case "ControllerDPadLeft": return BUTTON_DPAD_LEFT;
			case "ControllerDPadRight": return BUTTON_DPAD_RIGHT;
			case "ControllerL1": return BUTTON_L1;
			case "ControllerL2": return BUTTON_L2;
			case "ControllerL3": return BUTTON_L3;
			case "ControllerR1": return BUTTON_R1;
			case "ControllerR2": return BUTTON_R2;
			case "ControllerR3": return BUTTON_R3;
			case "ControllerStart": return BUTTON_START;
		}

		return -1;
	}

	public final Type type;

	public GamepadMap(Type type) {
		this.type = type;
		switch (type) {
			case Xbox:
				xbox: { // TODO: DPad
					if (OS.linux) {
						BUTTON_A = 0;
						BUTTON_X = 2;
						BUTTON_Y = 3;
						BUTTON_B = 1;
						BUTTON_DPAD_UP = -1;
						BUTTON_DPAD_DOWN = -1;
						BUTTON_DPAD_RIGHT = -1;
						BUTTON_DPAD_LEFT = -1;
						BUTTON_L1 /* bumper */ = 4;
						BUTTON_L2 /* trigger */ = -1;
						BUTTON_L3 /* joystick */ = 9;
						BUTTON_R1 /* bumper */ = 5;
						BUTTON_R2 /* trigger */ = -1;
						BUTTON_R3 /* joystick */ = 10;
						AXIS_LEFT_X = 0;
						AXIS_LEFT_Y = 1;
						AXIS_LEFT_TRIGGER = 2;
						AXIS_RIGHT_X = 3;
						AXIS_RIGHT_Y = 4;
						AXIS_RIGHT_TRIGGER = 5;
						BUTTON_BACK = 6;
						BUTTON_START = 7;
						break xbox;
					}
					if (OS.windows) {
						BUTTON_A = 0;
						BUTTON_X = 2;
						BUTTON_Y = 3;
						BUTTON_B = 1;
						BUTTON_DPAD_UP = -1;
						BUTTON_DPAD_DOWN = -1;
						BUTTON_DPAD_RIGHT = -1;
						BUTTON_DPAD_LEFT = -1;
						BUTTON_L1 /* bumper */ = 4;
						BUTTON_L2 /* trigger */ = -1;
						BUTTON_L3 /* joystick */ = 3;
						BUTTON_R1 /* bumper */ = 5;
						BUTTON_R2 /* trigger */ = -1;
						BUTTON_R3 /* joystick */ = 9;
						AXIS_LEFT_X = 1;
						AXIS_LEFT_Y = 0;
						AXIS_LEFT_TRIGGER = 4;
						AXIS_RIGHT_X = 3;
						AXIS_RIGHT_Y = 2;
						AXIS_RIGHT_TRIGGER = 4;// This is for real, same axis # as
						// for LEFT trigger!
						BUTTON_BACK = 6;
						BUTTON_START = 7;
						break xbox;
					}
					/* fallback values */
					BUTTON_A = 0;
					BUTTON_X = 2;
					BUTTON_Y = 3;
					BUTTON_B = 1;
					BUTTON_DPAD_UP = -1;
					BUTTON_DPAD_DOWN = -1;
					BUTTON_DPAD_RIGHT = -1;
					BUTTON_DPAD_LEFT = -1;
					BUTTON_L1 /* bumper */ = 4;
					BUTTON_L2 /* trigger */ = -1;
					BUTTON_L3 /* joystick */ = 9;
					BUTTON_R1 /* bumper */ = 5;
					BUTTON_R2 /* trigger */ = -1;
					BUTTON_R3 /* joystick */ = 10;
					AXIS_LEFT_X = 0;
					AXIS_LEFT_Y = 1;
					AXIS_LEFT_TRIGGER = 2;
					AXIS_RIGHT_X = 3;
					AXIS_RIGHT_Y = 4;
					AXIS_RIGHT_TRIGGER = 5;
					BUTTON_BACK = 6;
					BUTTON_START = 7;
				}
				break;
      case PlayStation4:
      case PlayStation3: // TODO: Might be wrong, need to test
        BUTTON_X = 0;
        BUTTON_A = 1;
        BUTTON_B = 2;
        BUTTON_Y = 3;
        BUTTON_L1 /* bumper */ = 4;
        BUTTON_L2 /* trigger */ = 6;
        BUTTON_L3 /* joystick */ = 10;
        BUTTON_R1 /* bumper */ = 5;
        BUTTON_R2 /* trigger */ = 7;
        BUTTON_R3 /* joystick */ = 11;
        BUTTON_BACK = 8;
        BUTTON_START = 9;        
        AXIS_LEFT_X = 0;
        AXIS_LEFT_Y = 1;
        AXIS_RIGHT_X = 2;
        AXIS_RIGHT_Y = 5;
        AXIS_LEFT_TRIGGER = 4;
        AXIS_RIGHT_TRIGGER = 5;
        BUTTON_DPAD_UP = 0;
        BUTTON_DPAD_DOWN = 1;
        BUTTON_DPAD_LEFT = 2;
        BUTTON_DPAD_RIGHT = 3;
        break;
      default:
        BUTTON_A = 2;
        BUTTON_X = 3;
        BUTTON_Y = 0;
        BUTTON_B = 1;
        BUTTON_DPAD_UP = 11;
        BUTTON_DPAD_DOWN = 12;
        BUTTON_DPAD_RIGHT = 14;
        BUTTON_DPAD_LEFT = 13;
        BUTTON_L1 /* bumper */ = 6;
        BUTTON_L2 /* trigger */ = 4;
        BUTTON_L3 /* joystick */ = 10;
        BUTTON_R1 /* bumper */ = 7;
        BUTTON_R2 /* trigger */ = 5;
        BUTTON_R3 /* joystick */ = 11;
        AXIS_LEFT_X = 0;
        AXIS_LEFT_Y = 1;
        AXIS_LEFT_TRIGGER = 4;
        AXIS_RIGHT_X = 2;
        AXIS_RIGHT_Y = 3;
        AXIS_RIGHT_TRIGGER = 5;
        BUTTON_BACK = -1;
        BUTTON_START = 18;
        break;
		}
	}
}