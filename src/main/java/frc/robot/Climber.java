package frc.robot;

import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

import edu.wpi.first.wpilibj.DigitalInput;

public class Climber {

	//Creates the climber talon
	CCTalon talon1;
	CCTalon talon2;
	AnalogPotentiometer potent;
	DigitalInput frontLimit;

	//Encoder encoder1;
	
	//Creates the talon
	public Climber() {
		talon1 = new CCTalon(RobotMap.CLIMBER_A, false);
		talon2 = new CCTalon(RobotMap.CLIMBER_B, true);
		//potent = new AnalogPotentiometer(RobotMap.POTENT_CLIMBER_FRONT);
		frontLimit = new DigitalInput(RobotMap.BACK_CLIMBER_LIMITSWITCH);
		//encoder1 = new Encoder(RobotMap.ENCODER_ELEVATOR_A, RobotMap.ENCODER_ELEVATOR_B);	
	}
	
	//Sets the speed
	public void setClimberFront(double speed) {
		talon1.set(speed);
	
	}
	public void setClimberBack(double speed){
		talon2.set(speed);
	}

	public boolean getLimit(){
		return frontLimit.get();
		//return false;
	}

}
