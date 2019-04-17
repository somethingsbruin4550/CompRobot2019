package frc.robot;
//package frc.limelight;
import edu.wpi.first.wpilibj.Timer;
import frc.limelight.LimeCam;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//8===>~~~~~~~~~
//Visual representation of the usefulness of Adam's dumb class


public class Driver {

	Chassis chassis;
	OI oi;
	Elevator elevator;
	Intake intake;
	Climber climber;
	String print;
	Port port = Port.kOnboard;
	LimeCam cam;
	
	public Driver() {
		chassis = Chassis.getInstance();
		oi = new OI();
		elevator = new Elevator();
		intake = new Intake();
		climber = new Climber();
		cam = new LimeCam();
	}
	
	public void reset() {
		chassis.reset();
		elevator.reset();
	}
    
}