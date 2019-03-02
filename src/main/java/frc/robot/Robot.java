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
	boolean debugOn = true;

	boolean autoRun = false;
	public String switLocal;
	final String DEFAULT_AUTO = "Default"; // should capatalize

	String autoSelected;
	private SendableChooser<String> _chooser;

	Driver _driver;
	double spdMltWheel;
	boolean isBall;
	boolean isStartPressed;
	boolean isPadPressed;
	double eleHeight;
	boolean fuckPid = false;
	boolean limeCamPid = false;
	LimeCam lime = new LimeCam();

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
		_chooser = new SendableChooser<String>();
		_chooser.addDefault("Default Auto", DEFAULT_AUTO);
		SmartDashboard.putData("Auto choices", _chooser);

		//SmartDashboard.putBoolean("Disable Elevator PID", fuckPid);

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
		System.out.println("Auto selected: " + autoSelected);
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
				// _driver.elevator.setElevatorHeight(2, 0.01, false);
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
		_driver.reset();
		spdMltWheel = 0.5;
		isBall = false;
		isStartPressed = false;
		isPadPressed = false;
		eleHeight = 0;
		/// led.setDutyCycle(1425); //Sets Blue Breathe
		if (!fuckPid) {
			// _driver.elevator.returnToZero(3.0);
			_driver.elevator.setDriverTarget(0);
			_driver.elevator.initPID();
		}
		RobotStatus.setString("Running Teleop");
		// _driver.chassis.initTurnPID(180, 0.001);
		RobotActive.setBoolean(true);
		SmartDashboard.putBoolean("Elevator Pid Enabled", !fuckPid);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	// Runs Teleop
	public void teleopPeriodic() {
		// SmartDashboard:

		// DRIVER ONE
		// Speed control
		if (_driver.oi.getAButton())
			spdMltWheel = 0.50;
		else if (_driver.oi.getYButton())
			spdMltWheel = 1.0;

		// Wheel Stuff
		_driver.chassis.drive(OI.normalize(_driver.oi.getRJoystickXAxis(), -1.0, 0, 1.0) * spdMltWheel,
				OI.normalize(_driver.oi.getLJoystickYAxis(), -1.0, 0, 1.0) * spdMltWheel);

		// MECHANISMS
		/**
		 * To program mechanisms, look for t0/-he specific methods within the OI class
		 * for input Then look with the class of the mechanism you want to program, and
		 * use the input accordingly
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
		if (_driver.cam.getTargets() > 0.0) {
			double eDVT = _driver.cam.estimateDistanceViaTrig();
			System.out.println(eDVT);
		}
		// DRIVER TWO
		double spdMlt = 1.0;
		// Climber Stuff: Checks the bumpers and stuff
		if (_driver.oi.getRB())
			_driver.climber.setClimberFront(spdMlt * 0.75);
		else if (_driver.oi.getLB())
			_driver.climber.setClimberFront(-spdMlt * 0.75);
		else
			_driver.climber.setClimberFront(0);

		if (_driver.oi.getRBC2())
			_driver.climber.setClimberBack(spdMlt * 0.75);
		else if (_driver.oi.getLBC2())
			_driver.climber.setClimberBack(-spdMlt * 0.75);
		else
			_driver.climber.setClimberBack(0);

		// Elevator stuff
		if (_driver.oi.getRTC2() > 0.00) {
			_driver.elevator.setElevator((OI.normalize(Math.pow(_driver.oi.getRTC2(), 3), -1.0, 0, 1.0) * spdMlt));
		} else if (_driver.oi.getLTC2() > 0.00) {
			_driver.elevator
					.setElevator((OI.normalize(Math.pow(_driver.oi.getLTC2(), 3), -1.0, 0, 1.0) * -spdMlt * 0.7));
		} else if (!fuckPid) {
			_driver.elevator.runPID(0.02, true);
		} else {
			_driver.elevator.setElevator(0.240698);
		}
		// System.out.println("Elevator Pos: " + _driver.elevator.getDistance());

		if (_driver.oi.getUpPadC2() || _driver.oi.getDownPadC2()) {
			if (!isPadPressed) {
				if (_driver.oi.getUpPadC2()) {
					_driver.elevator.addDriverTarget(true);
				} else if (_driver.oi.getDownPadC2()) {
					_driver.elevator.addDriverTarget(false);
				}
				Timer.delay(0.15);
				isPadPressed = true;
			} else {
				isPadPressed = false;
			}
		}
	
		// System.out.println("Elevator Encoder: " + _driver.elevator.getDistance());

		// Switch
		// if (_driver.oi.getStartC2()) {
		// 	if (!isStartPressed) {
		// 		isBall = !isBall;
		// 		isStartPressed = true;
		// 	}
		// } else {
		// 	isStartPressed = false;
		// }

		// Intake stuff(Ball)
		if (isBall) {
			if (_driver.oi.getAButtonC2())
				_driver.intake.setIntake(spdMlt);
			else if (_driver.oi.getYButtonC2())
				_driver.intake.setIntake(-spdMlt);
			else
				_driver.intake.setIntake(0.0);
		} else {
			// Intake stuff(Hatch);
			if (_driver.oi.getAButtonC2())
				_driver.intake.setIntake(-spdMlt);
			else if (_driver.oi.getYButtonC2())
				_driver.intake.setIntake(spdMlt);
			else
				_driver.intake.setIntake(0.0);
		}

		if (limeCamPid) {
			_driver.chassis.initTurnPID(lime.getHortAngle(), 0.01);
			_driver.chassis.runTurnPID(true);
		} 

		if(_driver.oi.getAButton()){
			limeCamPid = true; 
		}

		if(_driver.oi.getYButton()){
			limeCamPid = false; 
		}

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		// For testing purposes only, this is an infinte loop, similar to teleOP
		// perodic, and autonomousPerodic
		// _driver.elevator.returnToZero(2.0);
		// _driver.elevator.setElevatorHeight(1.0, 0.01, false, 0.07);

	}
}