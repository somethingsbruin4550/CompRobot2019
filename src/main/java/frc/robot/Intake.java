package frc.robot;

public class Intake {

	//Creats THREE TALONS BECAUSE MECHANICAL IS THE WORST 
	CCTalon talon1;
	CCTalon talon2;

	//Builds the 3 talons
	public Intake(){
		talon1 = new CCTalon(RobotMap.INTAKE_LEFT, false);
		talon2 = new CCTalon(RobotMap.INTAKE_RIGHT, true);
	}

	//3 methods to set the speeds for the talons 
	public void setIntake(double speed){
		talon1.set(-speed*0.35);
		talon2.set(speed*0.35);
	}

	public void setOuttake(double speed){
		talon1.set(speed*0.60);
		talon2.set(-speed*0.60);
	}
}
