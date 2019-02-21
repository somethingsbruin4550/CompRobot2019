package frc.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.lang.model.util.ElementScanner6;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import frc.pixycam.PixyI2C;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
//Class

public class Robot extends TimedRobot {
	boolean debugOn = true;

	boolean autoRun = false;
	public String switLocal;
	final String DEFAULT_AUTO = "Default"; //should capatalize
	final String PIXY_TEST = "Pixy Test Code";

	String autoSelected;
	private SendableChooser<String> _chooser;

	Driver _driver;
	double spdMltWheel;
	boolean isBall;
	boolean isPressed; 
	double eleHeight; 

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	//Runs at Startup
	public void robotInit() {
		_chooser = new SendableChooser<String>();
		_chooser.addDefault("Default Auto", DEFAULT_AUTO);
		_chooser.addObject("Pixy Camera Test (!WIP!)", PIXY_TEST);
		SmartDashboard.putData("Auto choices", _chooser);

		_driver = new Driver();
		_driver.reset();
		_driver.elevator.initPID(0);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	//Initializes Autonomous
	
	public void autonomousInit() {
		//switLocal = "R";//DriverStation.getInstance().getGameSpecificMessage();
		autoSelected = _chooser.getSelected();
		autoRun = true;
		_driver.reset();
		System.out.println("Auto selected: " + autoSelected);

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	//Runs Autonomous
	public void autonomousPeriodic() {
		double drvSpd = 0.3;
		double trnSpd = 0.8;
		double delay = 0;
		if(autoRun){
			switch (autoSelected) {
				case PIXY_TEST:
				
				System.out.println("PIXY_TEST Running");
				
				PixyI2C cam = new PixyI2C((byte) 0x54);
				System.out.println("setLamp:");
				cam.setLamp(true, true);
				System.out.println("Free Memory: " + ((double)Runtime.getRuntime().freeMemory()/1000000));
				break;
				

			case DEFAULT_AUTO:
				//_driver.elevator.setElevatorHeight(2.0, 0.020, false);
				break;
			default:
				// Put default auto code here
				break;
			}
		}
		//teleopInit();
		autoRun = false;
		teleopPeriodic();
		
	}

	public void teleopInit() {
		_driver.reset();
		spdMltWheel = 0.5; 
		isBall = false; 
		isPressed = false;
		eleHeight = 0;  
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	//Runs Teleop
	public void teleopPeriodic(){
		//SmartDashboard Tests:
		SmartDashboard.putNumber("Elevator Height in Tics", _driver.elevator.getTargetHeight());


		//DRIVER ONE
		//Speed control
		if(_driver.oi.getAButton())
			spdMltWheel = 0.50;
		else if(_driver.oi.getYButton())
			spdMltWheel = 1.0;


		//Wheel Stuff
		_driver.chassis.drive(OI.normalize(_driver.oi.getRJoystickXAxis(), -1.0, 0, 1.0) * spdMltWheel, 
		OI.normalize(_driver.oi.getLJoystickYAxis(), -1.0, 0, 1.0) * spdMltWheel);

		
		//MECHANISMS
		/**
		 * To program mechanisms, look for t0/-he specific methods within the OI class for input
		 * Then look with the class of the mechanism you want to program, and use the input accordingly 
		 */
		//NOTE: The conditional for the reverse (if(_driver.oi.getLB)) must be in the form of else if
		//other wise, the motor will constantly switch between spdMlt and 0.0 in voltage when postive power is applied 
		//This goes for ALL mechansims 
		//What not to do below:
		/** 
		 * DON'T: 
		 * if(RB)
		 * 	set(+speed)
		 * else 
		 * 	set(0.0);
		 * 
		 * if(LB)
		 * 	set(-speed);
		 * else 
		 * 	set(0.0); <====This statement right here will cancel out the previous set(+speed) when RB is held 
		 */
		//DRIVER TWO
		double spdMlt = 1.0;
		//Climber Stuff: Checks the bumpers and stuff 
		if(_driver.oi.getRBC2())
			_driver.climber.setClimberFront(spdMlt);
		else if(_driver.oi.getLBC2())
			_driver.climber.setClimberFront(-spdMlt);
		else
			_driver.climber.setClimberFront(0);

		if(_driver.oi.getRB())
			_driver.climber.setClimberBack(spdMlt);
		else if(_driver.oi.getLB())
			_driver.climber.setClimberBack(-spdMlt);
		else
			_driver.climber.setClimberBack(0);

		//Elevator stuff 
		if(_driver.oi.getRTC2() > 0.00){
			_driver.elevator.setElevator((OI.normalize(Math.pow(_driver.oi.getRTC2(), 3), -1.0, 0, 1.0) * spdMlt));
		}else if(_driver.oi.getLTC2() > 0.00){
			_driver.elevator.setElevator((OI.normalize(Math.pow(_driver.oi.getLTC2(), 3), -1.0, 0, 1.0) * -spdMlt * 0.7));
		}else{
			_driver.elevator.setElevator(0);
			//0.240698
		}


		//Switch 
		if(_driver.oi.getStartC2()){
			if(!isPressed){ 
				isBall = !isBall; 
				isPressed = true;
			}
		} else {
			isPressed = false; 
		}
		
		//Intake stuff(Ball)
		if(isBall){
			if(_driver.oi.getAButtonC2())
				_driver.intake.setIntake(spdMlt);
			else if(_driver.oi.getYButtonC2())
				_driver.intake.setIntake(-spdMlt);
			else
				_driver.intake.setIntake(0.0);
		} else {
			//Intake stuff(Hatch);
			if(_driver.oi.getAButtonC2())
				_driver.intake.setIntake(-spdMlt);
			else if(_driver.oi.getYButtonC2())
				_driver.intake.setIntake(spdMlt);
			else
				_driver.intake.setIntake(0.0);
		}

	}


	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		//For testing purposes only, this is an infinte loop, similar to teleOP perodic, and autonomousPerodic
		_driver.elevator.setElevatorHeight(2.0, 0.01, false );
		
	}
}