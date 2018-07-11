package org.rexcellentgames.burningknight.game.input;

import org.rexcellentgames.burningknight.OS;

public class GamepadMap {
	public enum Model {
		AndroidTv, Ouya, Xbox, SNES, Unknown
	}

	public final int AXIS_DPAD_X;
	public final int AXIS_DPAD_Y;
	public final int AXIS_LEFT_TRIGGER;
	public final int AXIS_BRAKE;
	public final int AXIS_LEFT_X;
	public final int AXIS_LEFT_Y;
	public final int AXIS_RIGHT_TRIGGER;
	public final int AXIS_GAS;
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
	public final int BUTTON_MENU;
	public final int BUTTON_A;
	public final int BUTTON_R1 /* bumper */;
	public final int BUTTON_R2 /* trigger */;
	public final int BUTTON_R3 /* joystick */;
	public final int BUTTON_START;
	public final int BUTTON_X;
	public final int BUTTON_Y;
	public final int BUTTON_SEARCH; /* android tv */
	public final boolean DPAD_IS_AXIS;
	public final boolean DPAD_IS_BUTTON;
	public final boolean DPAD_IS_POV;

	public int getId(String name) {
		switch (name) {
			case "ControllerX": return BUTTON_X;
			case "ControllerY": return BUTTON_Y;
			case "ControllerB": return BUTTON_B;
			case "ControllerA": return BUTTON_A;
			case "ControllerDPadX": return AXIS_DPAD_X;
			case "ControllerDPadY": return AXIS_DPAD_Y;
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
			case "ControllerMenu": return BUTTON_MENU;
			case "ControllerStart": return BUTTON_START;
		}

		return -1;
	}

	public final Model model;

	public GamepadMap(Model model) {
		this.model = model;
		switch (model) {
			case AndroidTv:
				androidTv: {
					BUTTON_A = 96;
					BUTTON_X = 99;
					BUTTON_Y = 100;
					BUTTON_B = 97;
					BUTTON_MENU = -1;
					DPAD_IS_POV = false;
					DPAD_IS_BUTTON = false;
					BUTTON_DPAD_UP = -1;
					BUTTON_DPAD_DOWN = -1;
					BUTTON_DPAD_RIGHT = -1;
					BUTTON_DPAD_LEFT = -1;
					DPAD_IS_AXIS = true;
					AXIS_DPAD_X = 15;
					AXIS_DPAD_Y = 16;
					BUTTON_L1 /* bumper */ = 102;
					BUTTON_L2 /* trigger */ = -1;
					BUTTON_L3 /* joystick */ = 106;
					BUTTON_R1 /* bumper */ = 103;
					BUTTON_R2 /* trigger */ = -1;
					BUTTON_R3 /* joystick */ = 107;
					AXIS_LEFT_X = 0;
					AXIS_LEFT_Y = 1;
					AXIS_LEFT_TRIGGER = 17;
					AXIS_RIGHT_X = 11;
					AXIS_RIGHT_Y = 14;
					AXIS_RIGHT_TRIGGER = 107;
					BUTTON_BACK = -1;
					BUTTON_START = 108;
					BUTTON_SEARCH = 84;
					AXIS_BRAKE = 23;
					AXIS_GAS = 22;
					break androidTv;
				}
				break;
			case Ouya:
			default:
				ouya: {
					if (OS.linux) {
						BUTTON_A = 3;
						BUTTON_X = 4;
						BUTTON_Y = 5;
						BUTTON_B = 6;
						BUTTON_MENU = 17;
						DPAD_IS_POV = false;
						DPAD_IS_BUTTON = true;
						BUTTON_DPAD_UP = 11;
						BUTTON_DPAD_DOWN = 12;
						BUTTON_DPAD_RIGHT = 14;
						BUTTON_DPAD_LEFT = 13;
						DPAD_IS_AXIS = false;
						AXIS_DPAD_X = -1;
						AXIS_DPAD_Y = -1;
						BUTTON_L1 /* bumper */ = 7;
						BUTTON_L2 /* trigger */ = 15;
						BUTTON_L3 /* joystick */ = 9;
						BUTTON_R1 /* bumper */ = 8;
						BUTTON_R2 /* trigger */ = 16;
						BUTTON_R3 /* joystick */ = 10;
						AXIS_LEFT_X = 0;
						AXIS_LEFT_Y = 1;
						AXIS_LEFT_TRIGGER = 2;
						AXIS_RIGHT_X = 3;
						AXIS_RIGHT_Y = 4;
						AXIS_RIGHT_TRIGGER = 5;
						BUTTON_BACK = -1;
						BUTTON_START = 18;
						BUTTON_SEARCH = -1;
						AXIS_BRAKE = -1;
						AXIS_GAS = -1;
						break ouya;
					}
					if (OS.windows) {
						BUTTON_A = 0;
						BUTTON_X = 1;
						BUTTON_Y = 2;
						BUTTON_B = 3;
						BUTTON_MENU = 14;
						DPAD_IS_POV = false;
						DPAD_IS_BUTTON = true;
						BUTTON_DPAD_UP = 8;
						BUTTON_DPAD_DOWN = 9;
						BUTTON_DPAD_RIGHT = 11;
						BUTTON_DPAD_LEFT = 10;
						DPAD_IS_AXIS = false;
						AXIS_DPAD_X = -1;
						AXIS_DPAD_Y = -1;
						BUTTON_L1 /* bumper */ = 4;
						BUTTON_L2 /* trigger */ = -1;
						BUTTON_L3 /* joystick */ = 6;
						BUTTON_R1 /* bumper */ = 5;
						BUTTON_R2 /* trigger */ = -1;
						BUTTON_R3 /* joystick */ = 7;
						AXIS_LEFT_X = 1;
						AXIS_LEFT_Y = 0;
						AXIS_LEFT_TRIGGER = 4;
						AXIS_RIGHT_X = 3;
						AXIS_RIGHT_Y = 2;
						AXIS_RIGHT_TRIGGER = 5;
						BUTTON_BACK = -1;
						BUTTON_START = 15;
						BUTTON_SEARCH = -1;
						AXIS_BRAKE = -1;
						AXIS_GAS = -1;
						break ouya;
					}
					/* fallback values */
					BUTTON_A = 3;
					BUTTON_X = 4;
					BUTTON_Y = 5;
					BUTTON_B = 6;
					BUTTON_MENU = 17;
					DPAD_IS_POV = false;
					DPAD_IS_BUTTON = true;
					BUTTON_DPAD_UP = 11;
					BUTTON_DPAD_DOWN = 12;
					BUTTON_DPAD_RIGHT = 14;
					BUTTON_DPAD_LEFT = 13;
					DPAD_IS_AXIS = false;
					AXIS_DPAD_X = -1;
					AXIS_DPAD_Y = -1;
					BUTTON_L1 /* bumper */ = 7;
					BUTTON_L2 /* trigger */ = 15;
					BUTTON_L3 /* joystick */ = 9;
					BUTTON_R1 /* bumper */ = 8;
					BUTTON_R2 /* trigger */ = 16;
					BUTTON_R3 /* joystick */ = 10;
					AXIS_LEFT_X = 0;
					AXIS_LEFT_Y = 1;
					AXIS_LEFT_TRIGGER = 2;
					AXIS_RIGHT_X = 3;
					AXIS_RIGHT_Y = 4;
					AXIS_RIGHT_TRIGGER = 5;
					BUTTON_BACK = -1;
					BUTTON_START = 18;
					BUTTON_SEARCH = -1;
					AXIS_BRAKE = -1;
					AXIS_GAS = -1;
				}
				break;
			case Xbox:
				xbox: {
					if (OS.linux) {
						BUTTON_A = 0;
						BUTTON_X = 2;
						BUTTON_Y = 3;
						BUTTON_B = 1;
						BUTTON_MENU = 8;
						DPAD_IS_POV = true;
						DPAD_IS_BUTTON = false;
						BUTTON_DPAD_UP = -1;
						BUTTON_DPAD_DOWN = -1;
						BUTTON_DPAD_RIGHT = -1;
						BUTTON_DPAD_LEFT = -1;
						DPAD_IS_AXIS = false;
						AXIS_DPAD_X = -1;
						AXIS_DPAD_Y = -1;
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
						BUTTON_SEARCH = -1;
						AXIS_BRAKE = -1;
						AXIS_GAS = -1;
						break xbox;
					}
					if (OS.windows) {
						BUTTON_A = 0;
						BUTTON_X = 2;
						BUTTON_Y = 3;
						BUTTON_B = 1;
						BUTTON_MENU = 7;
						DPAD_IS_POV = true;
						DPAD_IS_BUTTON = false;
						BUTTON_DPAD_UP = -1;
						BUTTON_DPAD_DOWN = -1;
						BUTTON_DPAD_RIGHT = -1;
						BUTTON_DPAD_LEFT = -1;
						DPAD_IS_AXIS = false;
						AXIS_DPAD_X = -1;
						AXIS_DPAD_Y = -1;
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
						BUTTON_SEARCH = -1;
						AXIS_BRAKE = -1;
						AXIS_GAS = -1;
						break xbox;
					}
					/* fallback values */
					BUTTON_A = 0;
					BUTTON_X = 2;
					BUTTON_Y = 3;
					BUTTON_B = 1;
					BUTTON_MENU = 8;
					DPAD_IS_POV = true;
					DPAD_IS_BUTTON = false;
					BUTTON_DPAD_UP = -1;
					BUTTON_DPAD_DOWN = -1;
					BUTTON_DPAD_RIGHT = -1;
					BUTTON_DPAD_LEFT = -1;
					DPAD_IS_AXIS = false;
					AXIS_DPAD_X = -1;
					AXIS_DPAD_Y = -1;
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
					BUTTON_SEARCH = -1;
					AXIS_BRAKE = -1;
					AXIS_GAS = -1;
				}
				break;
			case Unknown:
				BUTTON_A = 2;
				BUTTON_X = 3;
				BUTTON_Y = 0;
				BUTTON_B = 1;
				BUTTON_MENU = 17;
				DPAD_IS_POV = false;
				DPAD_IS_BUTTON = true;
				BUTTON_DPAD_UP = 11;
				BUTTON_DPAD_DOWN = 12;
				BUTTON_DPAD_RIGHT = 14;
				BUTTON_DPAD_LEFT = 13;
				DPAD_IS_AXIS = false;
				AXIS_DPAD_X = -1;
				AXIS_DPAD_Y = -1;
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
				BUTTON_SEARCH = -1;
				AXIS_BRAKE = -1;
				AXIS_GAS = -1;
				break;
			case SNES:
				snes: {
					if (OS.linux) {
						/* by AngelusWeb » Fri Sep 06, 2013 11:33 pm */
						BUTTON_A = 0;
						BUTTON_X = 2;
						BUTTON_Y = 3;
						BUTTON_B = 1;
						BUTTON_MENU = -1;
						DPAD_IS_POV = false;
						DPAD_IS_BUTTON = false;
						BUTTON_DPAD_UP = -1;
						BUTTON_DPAD_DOWN = -1;
						BUTTON_DPAD_RIGHT = -1;
						BUTTON_DPAD_LEFT = -1;
						DPAD_IS_AXIS = true;
						AXIS_DPAD_X = 0;
						AXIS_DPAD_Y = 1;
						BUTTON_L1 /* bumper */ = 5;
						BUTTON_L2 /* trigger */ = -1;
						BUTTON_L3 /* joystick */ = -1;
						BUTTON_R1 /* bumper */ = 6;
						BUTTON_R2 /* trigger */ = -1;
						BUTTON_R3 /* joystick */ = -1;
						AXIS_LEFT_X = -1;
						AXIS_LEFT_Y = -1;
						AXIS_LEFT_TRIGGER = -1;
						AXIS_RIGHT_X = -1;
						AXIS_RIGHT_Y = -1;
						AXIS_RIGHT_TRIGGER = -1;
						BUTTON_BACK = -1;
						BUTTON_START = -1;
						BUTTON_SEARCH = -1;
						AXIS_BRAKE = -1;
						AXIS_GAS = -1;
						break snes;
					}
					/* default SNES mapping */
					BUTTON_A = 0;
					BUTTON_X = 2;
					BUTTON_Y = 3;
					BUTTON_B = 1;
					BUTTON_MENU = -1;
					DPAD_IS_POV = false;
					DPAD_IS_BUTTON = false;
					BUTTON_DPAD_UP = -1;
					BUTTON_DPAD_DOWN = -1;
					BUTTON_DPAD_RIGHT = -1;
					BUTTON_DPAD_LEFT = -1;
					DPAD_IS_AXIS = true;
					AXIS_DPAD_X = 0;
					AXIS_DPAD_Y = 1;
					BUTTON_L1 /* bumper */ = 5;
					BUTTON_L2 /* trigger */ = -1;
					BUTTON_L3 /* joystick */ = -1;
					BUTTON_R1 /* bumper */ = 6;
					BUTTON_R2 /* trigger */ = -1;
					BUTTON_R3 /* joystick */ = -1;
					AXIS_LEFT_X = -1;
					AXIS_LEFT_Y = -1;
					AXIS_LEFT_TRIGGER = -1;
					AXIS_RIGHT_X = -1;
					AXIS_RIGHT_Y = -1;
					AXIS_RIGHT_TRIGGER = -1;
					BUTTON_BACK = -1;
					BUTTON_START = -1;
					BUTTON_SEARCH = -1;
					AXIS_BRAKE = -1;
					AXIS_GAS = -1;
				}
				break;
		}
	}
}