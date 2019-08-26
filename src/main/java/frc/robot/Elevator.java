package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


public class Elevator {
	//Creates the elevator talon
	CCTalon talon1;
	Encoder encoder;
	OI oi = new OI();

	int mod = 0;

	double inches = 0; 

//	boolean elevatorAdjusting;
	int driverTarget = 0;
//	boolean withinTarget;
		
	double ticsPerInch = 43.8602409;//526.322891; //encoder ticks per inch

	double baseHeight = 5.5;

	double feed_forward; // forward input to reduce the steady state error
	double max; // used to clamp the max speed, to slow down the robot
	double previous_error; // used to calculate the derivative value
	double integral; // used to carry the sum of the error
	double derivative; // used to calculate the change between our goal and position
	final double Tu = 0.002; //Oscillation period
	final double Ku = 0.005; //Maximum P value before oscillation
	final boolean calcConstants = false;
	double Kp;// proportional constant
	double Ki; // integral constant
	double Kd;//0.002; // derivative constant
	double goal;
	double[] targets = {		
		 -5.5,
		 0.0 + 3 , //Hatch 1
		 16.0, //Cargo 1
		 26.50 + 4, //Hatch 2
		 44.0 + 4, //Cargo 2
		 55 + 4, //Hatch 3
		 71.0 + 4, //Cargo 3
	};
	int target = 0; 
	double position; // current position in inches/feed, degrees, etc.)
	double error; // our goal is to make the error from the current position zero)
	double error_check;

	private NetworkTable table = NetworkTableInstance.getDefault().getTable("ElementDashboard");
	NetworkTableEntry ElevatorLevel = table.getEntry("ElevatorLevel");
	
	//Builds the elevator talon
	public Elevator(){
		talon1 = new CCTalon(RobotMap.ELEVATOR, false);
		
		encoder  = new Encoder(RobotMap.ENCODER_ELEVATOR_A, RobotMap.ENCODER_ELEVATOR_B);
		ElevatorLevel.setString("VOID");
	}
	
	/**
	 * Sets the speed of the elevator
	 * @param speed
	 */
	public void setElevator(double speed){
		talon1.set(speed);
		if(getDistance() / ticsPerInch > 30 && speed > 0){
			talon1.set(0);
		}
		//System.out.println(speed);
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
	 * Sets the driver target (for PIDs)
	 * @param level Desired level for the rocket/cargo ship
	 */
	public void setDriverTarget(int level){
		driverTarget = level;
	}

	/**
	 * Runs the elevator down for a set number of seconds
	 * Resets encoder value at the end
	 * @param seconds Number of seconds
	 */
	public void returnToZero(double seconds){
		setElevator(-0.5);
		Timer.delay(seconds);
		setElevator(0.0);
		this.reset();
	}

	/**
	 * Initializes the PICameraToggleD values for the elevator
	 * For calculating Constants, they can be found here: https://frc-pdr.readthedocs.io/en/latest/control/pid_control.html
	 */
	public void initPID(){
		//elevatorAdjusting = true;
		//withinTarget = false;

		baseHeight = 5.5; //Base elevator height in Inches

		feed_forward = 0.11; // forward input to reduce the steady state error
		max = 0.9; // used to clamp the max speed, to slow down the robot
		previous_error = 0; // used to calculate the derivative value
		integral = 0; // used to carry the sum of the error
		derivative = 0; // used to calculate the change between our goal and position

		if(calcConstants){ //constants derived from calculations
			Kp = .6 * Ku;
			Ki = 1.2 * Ku/Tu;
			Kd = 3 * Ku * Tu / 40;
		}
		else{ //Constants derived from testing
			Kp = 0.006; // proportional constant
			Ki = 0.00; // integral constant
			Kd = 0.0; // derivative constant
		}
		
		goal = (inches-baseHeight) * ticsPerInch;
		
		position = this.getDistance(); // current position in inches/feed, degrees, etc.)
		error = 0; // our goal is to make the error from the current position zero)
		error_check = goal/150;
	}

	/**
	 * This is the elevator PID itself, and should be run in a loop
	 * Primarially designed to be used in teleopPeriodic
	 * @param dt Delay value, can not be 0
	 * @param debugOn If true, will print out pid values
	 */
	public void runPID( double dt, boolean debugOn){
		inches = targets[target];
		goal = (inches+baseHeight + mod) * ticsPerInch;

		//error_check = goal/150; //Sets the error_check
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
		this.setElevator(OI.normalize(output, -.75, 0, max));
		//System.out.println(OI.normalize(output, -.75, 0, max));
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

	public void addInches(boolean pos) {
		if((this.getElevator() <= 88.5*ticsPerInch) && pos) {
			inches += 1.0;
		} else if((this.getElevator() >= 0.0) && (!pos)) {
			inches -= 1.0;
		}
	}

	public void snapToHeight(boolean up) {
		if(up)
			target++;
		else
			target--;

		if(target > targets.length - 1)
			target = targets.length -1;
		else if(target < 0)
			target = 0;

		System.out.println("Snap to target on");
		System.out.println("Inches: " + inches);
	}

	public int findClosest(){
		for(int i = 0; i < targets.length; i++){
			if(targets[i] >= inches)
				return i;
		}
		return targets.length-1;
	}

	public void intTarget(boolean up) {
		if(up)
			target++;
		else 
			target--;

		if(target < 0){
			target = 0; 
		} else if(target >= targets.length){
			target = targets.length-1;
		}
		//System.out.println(target);
	}
}
