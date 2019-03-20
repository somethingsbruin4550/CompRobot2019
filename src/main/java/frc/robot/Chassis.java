package frc.robot;

import frc.robot.*;
import frc.limelight.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SPI;

public class Chassis {
	// Something's Bruin, Something's Bruin, do ya smell coffee? Cuz Something's
	// Bruin!
	// Declares the encoders in the memory
	public Encoder _leftEncoder;
	public Encoder _rightEncoder;

	// Creates the left and right motors and builds the gyro
	CCTalon _backLeft;
	CCTalon _backRight;
	CCTalon _frontLeft;
	CCTalon _frontRight;
	LimeCam limelight;
	AHRS _gyro;

	// Creates the chassis within the chassis
	private static Chassis _instance;

	private static final double encoderPerFeet = 118.647;
	private static final double encoderPerVolt = 787.222;

	// PID vars for teleop
	private double feed_forward;
	private double max;
	private double previous_error;
	private double integral;
	private double derivative;
	private double Kp;
	private double Ki;
	private double Kd;
	private double goal;
	private double dt;
	private double position;
	private double error;
	private double error_check;

	// Creates the Chassis
	private Chassis() {

		// Creates the talons
		// motor = new CCTalon(firstPort, polarity)
		// if polarity is true, the motor goes in reverse
		_backLeft = new CCTalon(RobotMap.BACK_LEFT, RobotMap.BACK_LEFT_REVERSE);
		_backRight = new CCTalon(RobotMap.BACK_RIGHT, RobotMap.BACK_RIGHT_REVERSE);
		_frontLeft = new CCTalon(RobotMap.FRONT_LEFT, RobotMap.FRONT_LEFT_REVERSE);
		_frontRight = new CCTalon(RobotMap.FRONT_RIGHT, RobotMap.FRONT_RIGHT_REVERSE);
		limelight = new LimeCam();

		// Creates the Encoders
		// encoder = new Encoder(RobotMap.firstPort, RobotMap.secondPort)
		//_leftEncoder = new Encoder(RobotMap.ENCODER_A_LEFT, RobotMap.ENCODER_B_LEFT);
		//_rightEncoder = new Encoder(RobotMap.ENCODER_A_RIGHT, RobotMap.ENCODER_B_RIGHT);

		try {
			// Tries to build and gyro
			_gyro = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException e) {
			DriverStation.reportError("Error instantiating navX-MXP:  " + e.getMessage(), true);
		}
	}

	// Tank Drive Method(Drives Robot, but better?)
	public void tankDrive(double xAxis, double yAxis) {
		_backLeft.set(OI.deadBand((yAxis + xAxis), -1.0, 0, 1.0));
		_frontLeft.set(OI.deadBand((yAxis + xAxis), -1.0, 0, 1.0));
		_backRight.set(OI.deadBand((yAxis - xAxis), -1.0, 0, 1.0));
		_frontRight.set(OI.deadBand((yAxis - xAxis), -1.0, 0, 1.0));
	}

	public void drive(double xAxis, double yAxis) {
		// System.out.println("speedCheck: " + speedCheck(xAxis));
		xAxis += speedCheck(xAxis);
		// System.out.println("speedCheck(Y): " + speedCheck(yAxis));
		yAxis += speedCheck(yAxis);
		_frontLeft.set(OI.normalize((yAxis + xAxis), -1.0, 0, 1.0));
		_frontRight.set(OI.normalize((yAxis - xAxis), -1.0, 0, 1.0));
		_backLeft.set(OI.normalize((yAxis + xAxis), -1.0, 0, 1.0));
		_backRight.set(OI.normalize((yAxis - xAxis), -1.0, 0, 1.0));
		// System.out.println("xAxis: "+xAxis);
		// System.out.println("yAxis: "+yAxis);
	}

	public double speedCheck(double Spd) {
		if (Spd > 1.0) {
			return -((Spd - 1.0) * 0.5);
		} else if (Spd < -1.0) {
			return -((Spd + 1.0) * 0.5);
		}
		return 0.0;
	}

	// public void driveDist(double dist, double delay, boolean debug) {

	// 	double feed_forward = 0.0234375; // forward input to reduce the steady state error
	// 	double maxD = 0.275; // used to clamp the max speed, to slow down the robot
	// 	double previous_errorD = 0; // used to calculate the derivative value
	// 	double integralD = 0; // used to carry the sum of the error
	// 	double derivativeD = 0; // used to calculate the change between our goal and position
	// 	double KpD = 0.00419; // proportional constant
	// 	double KiD = 0.0; // integral constant
	// 	double KdD = 0;// 0.002; // derivative constant
	// 	double goal = dist * encoderPerFeet;
	// 	// this is what we want the robot to do: go forward,
	// 	// turn, elevate, etc to a new position)
	// 	double dt = delay;
	// 	// this is the wait period for the loop e.g. 1/100s)
	// 	double position = (this.getLeftEncoder() + this.getRightEncoder()) / 2;// current position in inches/feed,
	// 																			// degrees, etc.)
	// 	double offsetL = (position - this.getLeftEncoder()) / encoderPerVolt;
	// 	double offsetR = (position - this.getRightEncoder()) / encoderPerVolt;
	// 	double error = 0; // our goal is to make the error from the current position zero)
	// 	double error_check = goal / 100;

	// 	while (true) {
	// 		// Reset the Position
	// 		position = this.getLeftEncoder();
	// 		// Calculates the error based on how far the robot is from the dist
	// 		error = goal - position;
	// 		// Calculates the Integral based on the error, delay, and the previous integral
	// 		integralD = integralD + error * dt;
	// 		// Calculates the derivative based on the error and the delay
	// 		derivativeD = (error - previous_errorD) / dt;
	// 		// MATH
	// 		double output = KpD * error + KiD * integralD + KdD * derivativeD + feed_forward;
	// 		// Passes on the error to the previous error
	// 		previous_errorD = error;

	// 		// NORMALIZE: If the spd is bigger than we want, set it to the max, if its less
	// 		// than the -max makes it the negitive max
	// 		if (output > maxD)
	// 			output = maxD;
	// 		else if (output < -maxD)
	// 			output = -maxD;

	// 		// After the spd has been fixed, set the speed to the output
	// 		this.driveSpd(output + offsetL, output + offsetR);

	// 		// If it's close enough, just break and end the loop
	// 		if (error <= error_check) {
	// 			System.out.println("break");
	// 			break;
	// 		}

	// 		// Delay(Uses dt)
	// 		Timer.delay(dt);
	// 		if (debug) {
	// 			System.out.println("Position: " + position);
	// 			System.out.println("Error: " + error);
	// 			System.out.println("Output: " + output);
	// 			System.out.println("Integral: " + integralD);

	// 		}
	// 	}
	// }

	// Turns to a specific angle(For Autonomous)
	public void turnToAngle(double angl, double delay, boolean debug) {
		double feed_forward = 0.0234375; // forward input to reduce the steady state error
		double max = 0.275; // used to clamp the max speed, to slow down the robot
		double previous_error = 0; // used to calculate the derivative value
		double integral = 0; // used to carry the sum of the error
		double derivative = 0; // used to calculate the change between our goal and position
		double Kp = 0.005; // proportional constant
		double Ki = 0; // integral constant
		double Kd = 0;// 0.002; // derivative constant
		double goal = angl;
		// this is what we want the robot to do: go forward,
		// turn, elevate, etc to a new position)
		double dt = delay;
		// this is the wait period for the loop e.g. 1/100s)
		double position = this.getAngle(); // current position in inches/feed, degrees, etc.)
		double error = 0; // our goal is to make the error from the current position zero)
		double error_check = goal / 100;

		while (true) {
			if (error >= error_check) {
				// Reset the Position
				position = this.getAngle();
				// Calculates the error based on how far the robot is from the dist
				error = goal - position;
				// Calculates the Integral based on the error, delay, and the previous integral
				integral = integral + error * dt;
				// Calculates the derivative based on the error and the delay
				derivative = (error - previous_error) / dt;
				// MATH
				double output = Kp * error + Ki * integral + Kd * derivative + feed_forward;
				// Passes on the error to the previous error
				previous_error = error;

				// NORMALIZE: If the spd is bigger than we want, set it to the max, if its less
				// than the -max makes it the negitive max
				if (output > max)
					output = max;
				else if (output < -max)
					output = -max;

				// After the spd has been fixed, set the speed to the output
				this.driveSpd(output, -output);

				// If it's close enough, just break and end the loop
				if (error <= error_check) {
					// System.out.println("break");
					// break;
				}

				// Delay(Uses dt)
				Timer.delay(dt);
				if (debug) {
					System.out.println("Position: " + position);
					System.out.println("Error: " + error);
					System.out.println("Output: " + output);
					System.out.println("Integral: " + integral);

				}
			}
		}
	}

	public void initTurnPID(double angle, double delay) {
		feed_forward = 0.0234375; // forward input to reduce the steady state error
		max = 0.75; // used to clamp the max speed, to slow down the robot
		previous_error = 0; // used to calculate the derivative value
		integral = 0; // used to carry the sum of the error
		derivative = 0; // used to calculate the change between our goal and position
		Kp = 0.05; // proportional constant
		Ki = 0; // integral constant
		Kd = 0;// 0.002; // derivative constant
		goal = angle;
		// this is what we want the robot to do: go forward,
		// turn, elevate, etc to a new position)
		dt = delay;
		// this is the wait period for the loop e.g. 1/100s)
		position = this.getTX(); // current position in inches/feed, degrees, etc.)
		error = 0; // our goal is to make the error from the current position zero)
		error_check = goal / 100;
	}

	public void runTurnPID(boolean debug) {
		// Reset the Position
		position = this.getTX();
		
		// Calculates the error based on how far the robot is from the dist
		error = goal - position;
		// Calculates the Integral based on the error, delay, and the previous integral
		//integral = integral + error * dt;
		// Calculates the derivative based on the error and the delay
		//derivative = (error - previous_error) / dt;
		// MATH
		double output = OI.normalize((Kp * error + feed_forward), -max, 0, max);
		// Passes on the error to the previous error
		previous_error = error;

		// NORMALIZE: If the spd is bigger than we want, set it to the max, if its less
		// than the -max makes it the negitive max
		// if (output > max)
		// 	output = max;
		// else if (output < -max)
		// 	output = -max;

		// After the spd has been fixed, set the speed to the output
		this.driveSpd(-output, output);

		// If it's close enough, just break and end the loop
		// if (error <= error_check) {
		// 	System.out.println("break");
		// 	// break;
		// }

		// Delay(Uses dt)
	//	Timer.delay(dt);
		// if (debug) {
		// 	System.out.println("Position: " + position);
		// 	System.out.println("Error: " + error);
		// 	System.out.println("Output: " + output);
		// 	System.out.println("Integral: " + integral);

		// }
	}

	// Drives both motors a certain speed
	public void driveSpd(double lSpeed, double rSpeed) {
		_frontRight.set(rSpeed);
		_frontLeft.set(lSpeed);
		_backRight.set(rSpeed);
		_backLeft.set(lSpeed);
	}

	// Resets the encoder values
	public void reset() {
		//_leftEncoder.reset();
		//_rightEncoder.reset();
		_gyro.reset();
	}

	// Checks and returns angle
	public double getAngle() {
		return _gyro.getAngle();
	}

	// Gets the chassis instnace
	public static Chassis getInstance() {
		if (_instance == null) {
			_instance = new Chassis();
		}
		return _instance;
	}

	// Checks Encoder, and returns it
	// public double getLeftEncoder() {
	// 	return _leftEncoder.getDistance();
	// }

	// public double getRightEncoder() {
	// 	return _rightEncoder.getDistance();
	// }

	// turns to the angle of the limelight target using the PID loop
	public void turnToAngleLimePID() {
		turnToAngle(limelight.getTX(), 0.001, false);
	}

	public void simpleLimeTurn(double minAngleError){
		System.out.println("Turning with TX of: " + getTX());
		int count = 0;
		while(count<1000){
			Timer.delay(0.01);

			double absTX = (limelight.getTX())/30;
			//driveSpd(limelight.getTX()<0?-absTX:absTX,limelight.getTX()>0?-absTX:absTX);
			driveSpd(absTX,-absTX);
			count++;
			System.out.println("absTx: " + absTX + ", tx:" + limelight.getTX());
		}
		//driveSpd(0,0);
	}

	public void holdLimeTurn(){
		// double absTX = (limelight.getTX()+5.0)/30;
		// if(Math.abs(absTX)<0.01 && Math.abs(absTX)<0.025){
		// 	if(absTX<0){
		// 		absTX = -0.25;
		// 	}else{
		// 		absTX = 0.25;
		// 	}
		// }
		// System.out.println(absTX);
		// driveSpd(absTX, -absTX);
		double absTX = (limelight.getTX() - 6)/30;
			//driveSpd(limelight.getTX()<0?-absTX:absTX,limelight.getTX()>0?-absTX:absTX);
			driveSpd(OI.normalize(absTX + .237,-.5,0,.5) ,OI.normalize(-absTX - .237,-.5,0,.5));
	}

	public double getTX(){
		return limelight.getTX();
	}

}
