package frc.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.lang.model.util.ElementScanner6;
import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import frc.limelight.LimeCam;
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
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
// Class

public class Robot extends TimedRobot {
	boolean debugOn = false;

	boolean autoRun = false;
	public String switLocal;
	final String DEFAULT_AUTO = "Default"; // should capatalize
	final String DISABLE_AUTO = "Disabled Auto"; // should capatalize

	String autoSelected;
	private SendableChooser<String> _chooser;

	Driver _driver;
	double spdMltWheel;
	boolean isBall;
	boolean isStartPressed;
	boolean isPadPressed;
	double eleHeight;
	boolean limeCamPid = false;
	LimeCam lime = new LimeCam();
	int camera = 0; 
	boolean teleOPInit = false; 
	boolean fPID = false; 
	double spdMlt = 1.0;
	double target;
	double speed = 0;
	double lowestSpeed = 0.2;
	double highestSpeed = 0.4;
	//boolean running = true;
	double bounds = 0.3;
	double[] TAs = new double[1000];
	boolean newTA = true;
	int lastUsedIndex = -1;
	ReleasableButton dPadButtonUp = new ReleasableButton();
	ReleasableButton dPadButtonDown = new ReleasableButton();


	private NetworkTable table = NetworkTableInstance.getDefault().getTable("ElementDashboard");
	NetworkTableEntry RobotStatus = table.getEntry("RobotStatus");
	NetworkTableEntry RobotActive = table.getEntry("RobotActive");

	// RevLED led = new RevLED();

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	// Runs at Startup
	public void robotInit() {
		SmartDashboard.putNumber("rstatus", Math.random());
		_chooser = new SendableChooser<String>();
		_chooser.addDefault("Default Auto", DEFAULT_AUTO);
		_chooser.addOption("Disable Auto", DISABLE_AUTO);
		SmartDashboard.putData("Auto choices", _chooser);
		
		// SmartDashboard.putBoolean("Disable Elevator PID", );

		_driver = new Driver();
		_driver.reset();
		// led.initLED();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	// Initializes Autonomous

	public void autonomousInit() {
		// switLocal = "R";//DriverStation.getInstance().getGameSpecificMessage();
		autoSelected = _chooser.getSelected();
		autoRun = true;
		_driver.reset();
		//System.out.println("Auto selected: " + autoSelected);
		RobotStatus.setString("Running Autonomous");
		// led.setDutyCycle(1435); //Sets Gray Breathe
		RobotActive.setBoolean(true);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	// Runs Autonomous
	public void autonomousPeriodic() {
		double drvSpd = 0.3;
		double trnSpd = 0.8;
		double delay = 0;
		if (autoRun) {
			switch (autoSelected) {
			case DEFAULT_AUTO:
				_driver.reset();
				_driver.elevator.reset();
				spdMltWheel = 0.5;
				isPadPressed = false;
				eleHeight = 0;
				/// led.setDutyCycle(1425); //Sets Blue Breathe
				// _driver.elevator.returnToZero(3.0);
				_driver.elevator.setDriverTarget(0);
				_driver.elevator.initPID();
				_driver.chassis.initTurnPID(-5, .02);
				RobotStatus.setString("Running Teleop");
				// _driver.chassis.initTurnPID(180, 0.001);
				RobotActive.setBoolean(true);
				_driver.elevator.setElevator(.75);
				Timer.delay(.6);
				_driver.elevator.setElevator(0);
				Timer.delay(.25);
				_driver.elevator.setElevator(-.25);
				Timer.delay(.4);
				_driver.chassis.driveSpd(-.25, -.25);
				Timer.delay(1);
				_driver.chassis.driveSpd(0,0);
				SmartDashboard.putBoolean("Elevator Pid Enabled", true);
				teleOPInit = true; 
				//_driver.chassis.simpleLimeTurn();
				break;
			case DISABLE_AUTO:
			_driver.reset();
			_driver.elevator.reset();
			spdMltWheel = 0.5;
			isPadPressed = false;
			eleHeight = 0;
			/// led.setDutyCycle(1425); //Sets Blue Breathe
			// _driver.elevator.returnToZero(3.0);
			_driver.elevator.setDriverTarget(0);
			_driver.elevator.initPID();
			_driver.chassis.initTurnPID(-5, .02);
			RobotStatus.setString("Running Teleop");
			// _driver.chassis.initTurnPID(180, 0.001);
			RobotActive.setBoolean(true);
			SmartDashboard.putBoolean("Elevator Pid Enabled", true);
			teleOPInit = true; 
			break;
			default:
				// Put default auto code here
				break;
			}
		}
		// teleopInit();
		autoRun = false;
		teleopPeriodic();

	}

	public void teleopInit() {
		if(!teleOPInit){
			_driver.reset();
			_driver.elevator.reset();
			_driver.elevator.target = 0;
			spdMltWheel = 0.5;
			isPadPressed = false;
			eleHeight = 0;
			// led.setDutyCycle(1425); //Sets Blue Breathe
			// _driver.elevator.returnToZero(3.0);
			_driver.elevator.setDriverTarget(0);
			_driver.elevator.initPID();
			_driver.chassis.initTurnPID(-5, .02);
			RobotStatus.setString("Running Teleop");
			// _driver.chassis.initTurnPID(180, 0.001);
			RobotActive.setBoolean(true);
			SmartDashboard.putBoolean("Elevator Pid Enabled", true);
		}
		//System.out.println("Starting: ****************************************m ");
		TAs = new double[1000];
		newTA = true;
		lastUsedIndex = -1;
		_driver.chassis.setLimelightLED(true);
	}

	// public void disabledPeriodic(){
	// 	_driver.chassis.limelight.setLED(false);
	// }
	
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	// Runs Teleop
	public void teleopPeriodic() {
		//_driver.chassis.limelight.setLED(false);
		//System.out.println("TX value: " + _driver.chassis.getTX());

		// ElementalDashboard:
		// if(_driver.oi.getXButton()){
		// 	SmartDashboard.putNumber("rstatus", Math.random());
		// } 

		// SmartDashboard.putNumber("ElevatorLevel", _driver.elevator.target);		
		// DRIVER ONE
		// Speed control

		// for(int i=0; i<TAs.length; i++){
		// 	double TA = TAs[i];
		// 	if(Math.round(_driver.chassis.getTA() * 10.0) / 10.0 == Math.round(TA * 10.0) / 10.0){
		// 		newTA = false;
		// 	}
		// 	if(TA!=0){
		// 		lastUsedIndex = i;
		// 	}
		// }
		// if(newTA){
		// 	TAs[lastUsedIndex+1] = _driver.chassis.getTA();
		// 	System.out.println(_driver.chassis.getTA() + "," + _driver.chassis.getTX());
		// }
		// newTA = true;
		
		if (_driver.oi.getLT() > .25)
			spdMltWheel = 0.5;
		else if (_driver.oi.getRT() > .25)
			spdMltWheel = 0.75; 
		
		// Limelight Align:
		if(_driver.oi.getAButton()) {
			//_driver.chassis.simpleLimeTurn();
			_driver.chassis.setLimelightLED(true);
			//Timer.delay(0.5);
			target = _driver.chassis.limelight.estimateTargetAngle();//getFinalLimelightAngle();
			speed = OI.normalize(target/20/*32*/, -highestSpeed, 0, highestSpeed);
			//System.out.println("Target S: " + target);
			//System.out.println("Speed: " + speed);
			//System.out.println("Target: " + target);

			if(_driver.chassis.limelight.targetExists() && !(target >= -bounds && target <= bounds)){
			//	System.out.println("Within bounds!");
				if(Math.abs(speed)<lowestSpeed){
			//		System.out.println("Adjusting speed!");
					if(speed<0){
						_driver.chassis.driveSpd(-lowestSpeed/2 + (OI.normalize(_driver.oi.getRJoystickXAxis(), -1, 0, 1)/2)
						,lowestSpeed/2 + (OI.normalize(_driver.oi.getLJoystickYAxis(), -1, 0, 1))/2);
					}else if(speed>0){
						_driver.chassis.driveSpd(lowestSpeed/2 + (OI.normalize(_driver.oi.getRJoystickXAxis(), -1, 0, 1)/2)
						,-lowestSpeed/2 +(OI.normalize(_driver.oi.getLJoystickYAxis(), -1, 0, 1))/2);
					}
				}else{
					_driver.chassis.driveSpd(speed/2 + (OI.normalize(_driver.oi.getRJoystickXAxis(), -1, 0, 1)/2),
					-speed/2 + (OI.normalize(_driver.oi.getLJoystickYAxis(), -1, 0, 1))/2);
				}
			}
			else{
				//System.out.println("Target is perfect or a there was an error accessing the target.");
				_driver.chassis.driveSpd(OI.normalize(_driver.oi.getRJoystickXAxis(), -1, 0, 1),
				OI.normalize(_driver.oi.getLJoystickYAxis(), -1, 0, 1));
			}
		} else {
			//_driver.chassis.setLimelightLED(false);
		}

		// Wheel Stuff
		if(!_driver.oi.getAButton()) {
			_driver.chassis.drive(OI.normalize(_driver.oi.getRJoystickXAxis(), -spdMltWheel, 0, spdMltWheel),
				OI.normalize(_driver.oi.getLJoystickYAxis(), -spdMltWheel, 0, spdMltWheel));
		}

				//System.out.println("Speed: " + _driver.chassis._backLeft.getMotorOutputPercent());
		//Switch
		// if(_driver.oi.getStart()){
		// 	camera++;
		// 	if(camera > 1)
		// 		camera = 0;
		// 	table.getEntry("cameraToggle").setNumber(camera);
		// }

		
		// MECHANISMS
		/**
		 * To program mechanisms, look for t0/-he specific methods within the OI class
		 * use the input accordingly
		 * for input Then look with the class of the mechanism you want to program, and
		 */
		// NOTE: The conditional for the reverse (if(_driver.oi.getLB)) must be in the
		// form of else if
		// other wise, the motor will constantly switch between spdMlt and 0.0 in
		// voltage when postive power is applied
		// This goes for ALL mechansims
		// What not to do below:
		/**
		 * DON'T: if(RB) set(+speed) else set(0.0);
		 * 
		 * if(LB) set(-speed); else set(0.0); <====This statement right here will cancel
		 * out the previous set(+speed) when RB is held
		 */
		// DRIVER TWO
		
		// Climber Stuff: Checks the bumpers and stuff
		
		if (_driver.oi.getLB()){
			_driver.climber.setClimberFront(-spdMlt * 0.75);
		}else if (_driver.oi.getRB()){
				_driver.climber.setClimberFront(spdMlt * 0.75);
		}else{
			_driver.climber.setClimberFront(0);
		}

		if (_driver.oi.getRBC2())
			_driver.climber.setClimberBack(spdMlt * 0.75);
		else if (_driver.oi.getLBC2())
			_driver.climber.setClimberBack(-spdMlt * 0.75);
		else
			_driver.climber.setClimberBack(0);

		// // Elevator stuff
		//  if (_driver.oi.getRTC2() > 0.1) {
		//  	_driver.elevator.setElevator(OI.normalize(_driver.oi.getRTC2(), -1.0, 0.0, 1.0));
		// // // 	//_driver.elevator.addInches(true);
		//  } else if (_driver.oi.getLTC2() > 0.1) {
		//  	_driver.elevator.setElevator(OI.normalize(_driver.oi.getLTC2(), -1.00, 0.0, 0.85)*-1.0);
		// // // 	//_driver.elevator.addInches(false);
		//  } else{
		// 	 _driver.elevator.setElevator(0.08);
		//  }
		 	_driver.elevator.runPID(0.02 ,false);
		// 	//System.out.println(_driver.elevator.getDistance());
		//  //}

		// if(_driver.oi.getAButton()){
		// //	System.out.println("Holding Lime Turn");
		// 	_driver.chassis.runTurnPID(false);
		// }
		// System.out.println("Elevator Pos: " + _driver.elevator.getDistance());

		// System.out.println("Elevator Encoder: " + _driver.elevator.getDistance());

		//Switch
		// if (_driver.oi.getDownPadC2() || _driver.oi.getUpPadC2()) {
		// 	if (!isPadPressed) {
		// 		if(_driver.oi.getUpPadC2())
		// 			_driver.elevator.intTarget(true);
		// 		else if(_driver.oi.getDownPadC2())
		// 			_driver.elevator.intTarget(false);
		// 		isPadPressed = true;
		// 	}
		// } else {
		// 	isPadPressed = false;
		// }

		if(dPadButtonUp.updateButton(_driver.oi.getUpPadC2())){
			_driver.elevator.intTarget(true);
			//System.out.println("success");
		}

		if(dPadButtonDown.updateButton(_driver.oi.getDownPadC2())){
			_driver.elevator.intTarget(false);
		}


		// Intake stuff(Ball)

		// if (isBall) {
			if (_driver.oi.getAButtonC2()){
				_driver.intake.setIntake(spdMlt);
			//	_driver.climber.setClimberBack(spdMlt * 0.75);
			}else if (_driver.oi.getYButtonC2()){
				_driver.intake.setOuttake(spdMlt);
			//	_driver.climber.setClimberBack(spdMlt * -0.75);
			}
			else
			{
				_driver.intake.setIntake(0.0);
				//_driver.climber.setClimberBack(0.0);
			}

		// }
		//  else {
		// 	// Intake stuff(Hatch);
		// 	if (_driver.oi.getAButtonC2())
		// 		_driver.intake.setIntake(-spdMlt);
		// 	else if (_driver.oi.getYButtonC2())
		// 		_driver.intake.setIntake(spdMlt);
		// 	else
		// 		_driver.intake.setIntake(0.0);
		// }

	if(_driver.oi.getXButtonC2()){
		_driver.elevator.mod = -5;
	}

	if(_driver.oi.getX2Released()){
		_driver.elevator.mod = 0;
	}

		// if (_driver.oi.getAButton()) {
		// 	limeCamPid = true;
		// or

		

	}


	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		// For testing purposes only, this is an infinte loop, similar to teleOP
		// perodic, and autonomousPerodic
		 _driver.elevator.returnToZero(2.0);
		// _driver.elevator.setElevatorHeight(1.0, 0.01, false, 0.07);

	}
}