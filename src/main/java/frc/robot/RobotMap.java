package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static final intleftMotor = 1;
	// public static final int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;

	//	COMPETITION ROBOT CONSTANTS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// // Wheel Talons
	// public static final int BACK_LEFT = 0;
	// public static final int BACK_RIGHT = 7;
	// public static final int FRONT_RIGHT = 8;
	// public static final int FRONT_LEFT = 2;

	// //Talon Ports for Intake
	// public static final int INTAKE_LEFT = 6;
	// public static final int INTAKE_RIGHT = 4;
			
	// //Talon ports for Elevator
	// public static final int ELEVATOR = 5;
		
	// //Talon ports for Climber 
	// public static final int CLIMBER_A = 1;
	// public static final int CLIMBER_B = 3;
	// Wheel Talons
	public static final int BACK_LEFT = 6;
	public static final int BACK_RIGHT = 5;
	public static final int FRONT_RIGHT = 4;
	public static final int FRONT_LEFT = 3;

	//Talon Ports for Intake
	public static final int INTAKE_LEFT = 8;
	public static final int INTAKE_RIGHT = 7;
			
	//Talon ports for Elevator
	public static final int ELEVATOR = 0;
		
	//Talon ports for Climber 
	public static final int CLIMBER_A = 1;
	public static final int CLIMBER_B = 2;

	// Wheel Talon Polarity
	public static final boolean FRONT_LEFT_REVERSE = false;
	public static final boolean BACK_LEFT_REVERSE = false;		
	public static final boolean FRONT_RIGHT_REVERSE = true;
	public static final boolean BACK_RIGHT_REVERSE = true; 

	// Controller Zeroes
	public static final double LEFT_Y_ZERO = -0.0078125;
	public static final double RIGHT_Y_ZERO = -0.0078125;

	// Wheel Encoder Ports
	public static final int ENCODER_A_RIGHT = 0;
	public static final int ENCODER_B_RIGHT = 1;
	public static final int ENCODER_A_LEFT = 2;
	public static final int ENCODER_B_LEFT = 3;
	public static final int ENCODER_ELEVATOR_A = 4;
	public static final int ENCODER_ELEVATOR_B = 5; 


	// Joystick Axises
	public static final int L_JOYSTICK_HORIZONTAL = 0;
	public static final int L_JOYSTICK_VERTICAL = 1;
	public static final int LT = 2;
	public static final int RT = 3;
	public static final int R_JOYSTICK_HORIZONTAL = 4;
	public static final int R_JOYSTICK_VERTICAL = 5;

	// Controller Buttons
	public static final int A_BUTTON = 1;
	public static final int B_BUTTON = 2;
	public static final int X_BUTTON = 3;
	public static final int Y_BUTTON = 4;
	public static final int LB_BUTTON = 5;
	public static final int RB_BUTTON = 6;
	public static final int SELECT_BUTTON = 7;
	public static final int START_BUTTON = 8;
	//These buttons are when you push down the left and right circle pad
	public static final int L_JOYSTICK_BUTTON = 9;
	public static final int R_JOYSTICK_BUTTON = 10;

	
	
}

