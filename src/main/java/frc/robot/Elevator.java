package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Elevator {
	//Creates the elevator talon
	CCTalon talon1;
	Encoder encoder;
	OI oi = new OI();

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
	final double Tu = 0.002; //Oscillation period
	final double Ku = 0.005; //Maximum P value before oscillation
	final boolean calcConstants = false;
	double Kp;// proportional constant
	double Ki; // integral constant
	double Kd;//0.002; // derivative constant
	double goal;
		

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

	public void addDriverTarget(boolean add){
		if(add){
			driverTarget++;
		}
		else{
			driverTarget--;
		}
		if(driverTarget < 0){
			driverTarget = 0;
		}
		else if (driverTarget > 5){
			driverTarget = 5;
		}
	}

	/**
	 * Converts from level of target to height of target in FEET
	 * @return Target height in FEET
	 */
	public double levelToFeet(){
		String status = "";
		double inches = 0;
		if(driverTarget == 0){
			status = "Hatch Port 1";
			inches = 19;
		}
		else if(driverTarget == 1){
			status = "Cargo Port 1";
			inches = 32.5;
		}
		else if(driverTarget == 2){
			status = "Hatch Port 2";
			inches = 45;
		}
		else if(driverTarget == 3){
			status = "Cargo Port 2";
			inches = 60.5;
		}
		else if(driverTarget == 4){
			status = "Hatch Port 3";
			inches = 74;
		}
		else if(driverTarget == 5){
			status = "Cargo Port 3";
			inches = 88.5;
		}
		SmartDashboard.putString("Elevator Level", status);
		ElevatorLevel.setString(status);
		return inches/12;
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
	 * Initializes the PID values for the elevator
	 * For calculating Constants, they can be found here: https://frc-pdr.readthedocs.io/en/latest/control/pid_control.html
	 */
	public void initPID(){
		elevatorAdjusting = true;
		withinTarget = false;
		
		ticsPerFeet = 526.322891;

		baseHeight = 19/12; //Base elevator height in FEET

		feed_forward = 0.240698; // forward input to reduce the steady state error
		max = 1.0; // used to clamp the max speed, to slow down the robot
		previous_error = 0; // used to calculate the derivative value
		integral = 0; // used to carry the sum of the error
		derivative = 0; // used to calculate the change between our goal and position

		if(calcConstants){ //constants derived from calculations
			Kp = .6 * Ku;
			Ki = 1.2 * Ku/Tu;
			Kd = 3 * Ku * Tu / 40;
		}
		else{ //Constants derived from testing
			Kp = 0.005; // proportional constant
			Ki = 0; // integral constant
			Kd = 0; // derivative constant
		}
		
		goal = (levelToFeet()-baseHeight) * ticsPerFeet;
		
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
		goal = (levelToFeet()-baseHeight) * ticsPerFeet;

		error_check = goal/150; //Sets the error_check
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
		this.setElevator(OI.normalize(output, -max, 0, max));
		
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
