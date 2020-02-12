package frc.robot;

import frc.parent.*;

public class Intake {

	//Creats THREE TALONS BECAUSE MECHANICAL IS THE WORST 
	private static CCTalon talon1 = new CCTalon(RobotMap.INTAKE_L, false);
	private static CCTalon talon2 = new CCTalon(RobotMap.INTAKE_R, true);


	//3 methods to set the speeds for the talons 
	public static void setIntake(double speed){
		talon1.set(-speed*0.35);
		talon2.set(speed*0.35);
	}

	public static void setOuttake(double speed){
		talon1.set(speed*0.60);
		talon2.set(-speed*0.60);
	}
}