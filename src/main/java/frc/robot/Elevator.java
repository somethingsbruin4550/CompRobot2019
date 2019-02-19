package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Elevator {
	//Creates the elevator talon
	CCTalon talon1;
	Encoder encoder;

	boolean elevatorAdjusting;
	int driverTarget = 0;
	
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
		return encoder.getDistance();
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
	 * Converts from level of target to height of target in inches
	 * @return Target height in inches
	 */
	public double getTargetHeight(){
		double inches = 0;
		// if(driverTarget == 0)
		// 	inches = 0;
		if(driverTarget == 1){
			inches = 27.5; //Center of PORT 1
		}
		if(driverTarget == 2){
			inches = 55.5; //Center of PORT 2
		}
		if(driverTarget == 3){
			inches = 83.5; //Center of PORT 3
		}
		return inches;
	}
	
	/**
	 * Sets the height of the elevator using a PID loop
	 * @param height Target height in INCHES
	 * @param dt Delay between each run of the while loop
	 * @param debugOn Toggles debug print statements
	 */
	public void setElevatorHeight(double height, double dt, boolean debugOn){
		elevatorAdjusting = true;
		boolean withinTarget = false;
		
		double ticsPerFeet = 115.116;

		double baseHeight = 0;

		double feed_forward = 0.240698; // forward input to reduce the steady state error
		double max = 0.5; // used to clamp the max speed, to slow down the robot
		double previous_error = 0; // used to calculate the derivative value
		double integral = 0; // used to carry the sum of the error
		double derivative = 0; // used to calculate the change between our goal and position
		double Kp = 0.005; // proportional constant
		double Ki = 0; // integral constant
		double Kd = 0;//0.002; // derivative constant
		double goal = (height-baseHeight) * ticsPerFeet;
		

		double position = this.getDistance(); // current position in inches/feed, degrees, etc.)
		double error = 0; // our goal is to make the error from the current position zero)
		double error_check = goal/150;
		
		while(!withinTarget) {
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
			
			//NORMALIZE: If the spd is bigger than we want, set it to the max, if its less than the -max makes it the negitive max
			if(output > max)
				output = max;
			else if(output < -(max/2)); //Max is /2 because elevator drops faster than it rises
				output = -(max/2); 
			
			//After the spd has been fixed, set the speed to the output
			this.setElevator(output);
			
			//If it's close enough, just break and end the loop 
			if(error <= error_check) {
				System.out.println("Elevator PID within target. Exiting...");
				withinTarget = true;
			}
			
			//Delay(Uses dt)
			Timer.delay(dt);
			if(debugOn) {
				
				System.out.println("Position: " + position);
				System.out.println("Error: " + error);
				System.out.println("Output: " + output);
				//System.out.println("Integral: " + integral);
				
			}
		}
	}
}
