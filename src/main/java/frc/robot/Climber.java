package frc.robot;

import edu.wpi.first.wpilibj.Encoder;

public class Climber {

	//Creates the climber talon
	CCTalon talon1;
	CCTalon talon2;
	Encoder encoder1;
	
	//Creates the talon
	public Climber() {
		talon1 = new CCTalon(RobotMap.CLIMBER_A, false);
		talon2 = new CCTalon(RobotMap.CLIMBER_B, false);
//		encoder1 = new Encoder(RobotMap.ENCODER_ELEVATOR_A, RobotMap.ENCODER_ELEVATOR_B);	
	}
	
	//Sets the speed
	public void setClimber(double speed) {
		talon1.set(speed);
		talon2.set(speed);
	}

	public void reset(){
//		encoder1.reset();
	}
	
	
}
