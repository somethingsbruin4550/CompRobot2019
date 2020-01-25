/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.parent.*;
// import frc.sensors.*;

// import com.revrobotics.ControlType;
// import com.revrobotics.CANSparkMax.IdleMode;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import frc.robot.CCSparkMax;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.trajectory.*;
import java.nio.file.Paths;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot implements RobotMap, ControMap {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static int alliance;
  
  Trajectory trajectory;

  // Intake intake;
  // Climber climber;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    try{
      trajectory = TrajectoryUtil.fromPathweaverJson(Paths.get("/home/lvuser/deploy/Unnamed.wpilib.json"));
    }catch(Exception e){
      //
    } 

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // intake = new Intake();
    // climber = new Climber();
    
  
    switch(DriverStation.getInstance().getAlliance()){
      case Blue:
        alliance = 1;
      break;

      case Red:
        alliance = 0; 
      break;
      
      case Invalid:
        alliance = -1;
      break;
    }
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    




    switch (m_autoSelected) {
      case kCustomAuto:
        break;
      case kDefaultAuto:
        break;
      default:
        break;
    }

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    // LemonLight.setLight(true);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    Chassis.axisDrive(OI.normalize(OI.axis(PilotMap.Y_AXIS), -1.0, 1.0), 
                          -OI.normalize(OI.axis(PilotMap.X_AXIS), -1.0, 1.0));
        


    final double elevatorSpd = 0.5;
    if(OI.button(PilotMap.STICK_MID))
    {
      Elevator.set(elevatorSpd);
    }
    else if(OI.button(PilotMap.STICK_BACK))
    {
      Elevator.set(-elevatorSpd);
    }
    else
    {
      Elevator.set(0.0);
    }

  }

  @Override
  public void disabledInit() {
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    

  }
}
