package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Elevator {
	//Creates the elevator talon
	CCTalon talon1;
	Encoder encoder;

	boolean elevatorAdjusting;
	int driverTarget = 0;
	boolean withinTarget;
		
	double ticsPerFeet;

	double baseHeight = 1;

	double feed_forward; // forward input to reduce the steady state error
	double max; // used to clamp the max speed, to slow down the robot
	double previous_error; // used to calculate the derivative value
	double integral; // used to carry the sum of the error
	double derivative; // used to calculate the change between our goal and position
	double Kp;// proportional constant
	double Ki; // integral constant
	double Kd;//0.002; // derivative constant
	double goal;
		

	double position; // current position in inches/feed, degrees, etc.)
	double error; // our goal is to make the error from the current position zero)
	double error_check;
	
	//Builds the elevator talon
	public Elevator(){
		talon1 = new CCTalon(RobotMap.ELEVATOR, false);
		encoder  = new Encoder(RobotMap.ENCODER_ELEVATOR_A, RobotMap.ENCODER_ELEVATOR_B);
	}
	
	/**
	 * Sets the speed of the elevator
	 * @param speed
	 */
	public void setElevator(double speed){
		talon1.set(speed);
	}

	/**
	 * Gets the current encoder value in tics
	 * @return Encoder Value in tics
	 */
	public double getDistance() {
		return -encoder.getDistance();
	}

	/**
	 * Gets the current speed of the talon
	 * @return Current speed of talon (From -1.0 to 1.0)
	 */
	public double getElevator(){
		return talon1.get();
	}

	/**
	 * Resets the encoder value to 0
	 */
	public void reset(){
		encoder.reset();
	}

	/**
	 * Sets the driver target (for PIDs)
	 * @param level Desired level for the rocket/cargo ship
	 */
	public void setDriverTarget(int level){
		driverTarget = level;
	}

	/**
	 * Converts from level of target to height of target in FEET
	 * @return Target height in FEET
	 */
	public double levelToFeet(){
		if(driverTarget == 0){
			return 0;
		}
		if(driverTarget == 1){
			return (47/12); //Center of PORT 1
		}
		else if(driverTarget == 2){
			return (75/12); //Center of PORT 2
		}
		else if(driverTarget == 3){
			return (103/12); //Center of PORT 3
		}
		return 0;
	}
	
	// /**
	//  * Sets the height of the elevator using a PID loop
	//  * @param height Target height in FEET
	//  * @param dt Delay between each run of the while loop CAN NOT BE 0
	//  * @param debugOn Toggles debug print statements
	//  */
	// public void setElevatorHeight(double height, double dt, boolean debugOn){
	// 	elevatorAdjusting = true;
	// 	boolean withinTarget = false;
		
	// 	double ticsPerFeet = 423.475;

	// 	double baseHeight = 0;

	// 	double feed_forward = 0.240698; // forward input to reduce the steady state error
	// 	double max = 0.5; // used to clamp the max speed, to slow down the robot
	// 	double previous_error = 0; // used to calculate the derivative value
	// 	double integral = 0; // used to carry the sum of the error
	// 	double derivative = 0; // used to calculate the change between our goal and position
	// 	double Kp = 0.005; // proportional constant
	// 	double Ki = 0; // integral constant
	// 	double Kd = 0;//0.002; // derivative constant
	// 	double goal = (height-baseHeight) * ticsPerFeet;
		

	// 	double position = this.getDistance(); // current position in inches/feed, degrees, etc.)
	// 	double error = 0; // our goal is to make the error from the current position zero)
	// 	double error_check = goal/150;
		
	// 	while(!withinTarget) {
	// 		//Reset the Position
	// 		position = this.getDistance();
	// 		//Calculates the error based on how far the robot is from the dist
	// 		error = goal - position;
	// 		//Calculates the Integral based on the error, delay, and the previous integral 
	// 		integral = integral + error * dt; 
	// 		//Calculates the derivative based on the error and the delay 
	// 		derivative = (error - previous_error) / dt;
	// 		//MATH 
	// 		//System.out.println("Output calcs1: " +  Kp * error + Ki * integral + Kd * derivative + feed_forward);
	// 		double output = Kp * error + Ki * integral + Kd * derivative + feed_forward;
	// 		//System.out.println("Output calcs2: " +  Kp * error + Ki * integral + Kd * derivative + feed_forward);
	// 		//Passes on the error to the previous error
	// 		previous_error = error;
			
	// 		//NORMALIZE: If the spd is bigger than we want, set it to the max, if its less than the -max makes it the negitive max
	// 		if(output > max)
	// 			output = max;
	// 		else if(output < -(max/2)); //Max is /2 because elevator drops faster than it rises
	// 			output = -(max/2); 
			
	// 		//After the spd has been fixed, set the speed to the output
	// 		this.setElevator(-output);
			
	// 		//If it's close enough, just break and end the loop 
	// 		if(error <= error_check) {
	// 			System.out.println("Elevator PID within target. Exiting...");
	// 			withinTarget = true;
	// 		}
			
	// 		//Delay(Uses dt)
	// 		//Timer.delay(dt);
	// 		if(debugOn) {
				
	// 			System.out.println("Position: " + position);
	// 			System.out.println("Error: " + error);
	// 			System.out.println("Output: " + output);
	// 			//System.out.println("Integral: " + integral);
				
	// 		}
	// 	}
	// }

	/**
	 * Initializes the PID values for the elevator
	 */
	public void initPID(){
		elevatorAdjusting = true;
		withinTarget = false;
		
		ticsPerFeet = 423.475;

		baseHeight = 0; //Base elevator height in FEET

		feed_forward = 0.240698; // forward input to reduce the steady state error
		max = 0.5; // used to clamp the max speed, to slow down the robot
		previous_error = 0; // used to calculate the derivative value
		integral = 0; // used to carry the sum of the error
		derivative = 0; // used to calculate the change between our goal and position
		Kp = 0.005; // proportional constant
		Ki = 0; // integral constant
		Kd = 0;//0.002; // derivative constant
		goal = (levelToFeet()-baseHeight) * ticsPerFeet;
		
		position = this.getDistance(); // current position in inches/feed, degrees, etc.)
		error = 0; // our goal is to make the error from the current position zero)
		error_check = goal/150;
	}

	/**
	 * This is the elevator PID itself, and should be run in a loop
	 * Primarially designed to be used in teleopPeriodic
	 */
	public void runPID( double dt, boolean debugOn){
		goal = (levelToFeet()-baseHeight) * ticsPerFeet;
		//Reset the Position
		position = this.getDistance();
		//Calculates the error based on how far the robot is from the dist
		error = goal - position;
		//Calculates the Integral based on the error, delay, and the previous integral 
		integral = integral + error * dt; 
		//Calculates the derivative based on the error and the delay 
		derivative = (error - previous_error) / dt;
		//MATH 
		//System.out.println("Output calcs1: " +  Kp * error + Ki * integral + Kd * derivative + feed_forward);
		double output = Kp * error + Ki * integral + Kd * derivative + feed_forward;
		//System.out.println("Output calcs2: " +  Kp * error + Ki * integral + Kd * derivative + feed_forward);
		//Passes on the error to the previous error
		previous_error = error;
		
		//After the spd has been fixed, set the speed to the output
		this.setElevator(OI.normalize(output, -0.5, 0, .5));
		
		//If it's close enough, just break and end the loop 
		// if(error <= error_check) {
		// 	System.out.println("Elevator PID within target. Exiting...");
		// 	withinTarget = true;
		// }
		
		//Delay(Uses dt)
		//Timer.delay(dt);
		if(debugOn) {
			
			System.out.println("Position: " + position);
			System.out.println("Error: " + error);
			System.out.println("Output: " + output);
			//System.out.println("Integral: " + integral);
			
		}
	}
}
