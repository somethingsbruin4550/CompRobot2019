package frc.robot;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Driver {

	Chassis chassis;
	OI oi;
	Elevator elevator;
	Intake intake;
	Climber climber;
	String print;
	Port port = Port.kOnboard;
	
	public Driver() {
		chassis = Chassis.getInstance();
		oi = new OI();
		elevator = new Elevator();
		intake = new Intake();
		climber = new Climber();
	}
	
	public void reset() {
		chassis.reset();
		elevator.reset();
	}
    
}