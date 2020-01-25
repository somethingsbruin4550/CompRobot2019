package frc.robot;

import frc.parent.*;
import frc.sensors.LemonLight;
// import edu.wpi.first.wpilibj.Encoder;
// import com.kauailabs.navx.frc.AHRS;
// import edu.wpi.first.wpilibj.SPI;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.networktables.NetworkTableType;
// import edu.wpi.first.networktables.NetworkTableValue;

public class Chassis implements RobotMap{

    //Talon objects for the wheels
    //These control the main 4 motors on the robot
    public static CCTalon fLeft = new CCTalon(RobotMap.FORWARD_LEFT, true);
    public static CCTalon fRight = new CCTalon(RobotMap.FORWARD_RIGHT, false);
    public static CCTalon bLeft = new CCTalon(RobotMap.BACK_LEFT, true);
    public static CCTalon bRight = new CCTalon(RobotMap.BACK_RIGHT, false);

    //Talon objects for the Sensors 
    //Ecoders measure rotations of the wheel
    //AHRS gyro measures the angle of the bot
    // public static Encoder eLeft = new Encoder(RobotMap.ENCODER_A_LEFT, RobotMap.ENCODER_B_LEFT);
    // public static Encoder eRight = new Encoder(RobotMap.ENCODER_A_RIGHT, RobotMap.ENCODER_B_RIGHT);
    // public static AHRS gyro = new AHRS(SPI.Port.kMXP);

    //To be used in TeleOP
    //Takes in two axises, most likely the controller axises
    //Optimized for a west coast or standard chassis
    //DO NOT USE THIS FOR SWERV DRIVE 
    public static void axisDrive(double yAxis, double xAxis){
        fLeft.set(OI.normalize((yAxis + xAxis), -1.0, 1.0));
        fRight.set(OI.normalize((yAxis - xAxis), -1.0, 1.0));
        bLeft.set(OI.normalize((yAxis + xAxis), -1.0, 1.0));
        bRight.set(OI.normalize((yAxis - xAxis), -1.0, 1.0));
    }

    //To be used on Auto/PIDs
    //Simply sets the motor controllers to a certain percent output
    public static void driveSpd(double lSpeed, double rSpeed){
        fLeft.set(OI.normalize(lSpeed, -1.0, 1.0));
        fRight.set(OI.normalize(rSpeed, -1.0, 1.0));
        bLeft.set(OI.normalize(lSpeed, -1.0, 1.0));
        bRight.set(OI.normalize(rSpeed, -1.0, 1.0));
    }

    public static void lockOn(boolean follow, double holdDist){
        double x = LemonLight.getYaw();
        double error = Math.abs(x);
        double input = OI.normalize((error*2)/100, 0.4, 0.7);
        double leftSpd, rightSpd;
        // System.out.println(input);

        if(LemonLight.hasTarget()){
            if(x > 2.0){
                leftSpd = -input;
                rightSpd = input;
            }
            else if(x < -2.0){
                leftSpd = input;
                rightSpd = -input;
            }
            else{
                leftSpd = 0.0;
                rightSpd = 0.0;
            }

            if(follow){
                double maxTurn = 0.35;
                leftSpd = OI.normalize(leftSpd, -maxTurn, maxTurn);
                rightSpd = OI.normalize(rightSpd, -maxTurn, maxTurn);


                double maxFollow = -0.2;
                double distTo = LemonLight.distToTarget() - holdDist;
                System.out.println(distTo);
                if(distTo > 0.15){
                    leftSpd += maxFollow;
                    rightSpd += maxFollow;
                }
                else if(distTo < -0.15){
                    leftSpd -= maxFollow;
                    rightSpd -= maxFollow;
                }
                // leftSpd += OI.normalize((distToTarget() - holdDist) / 3.0, -maxFollow, maxFollow);
                // rightSpd += OI.normalize((distToTarget() - holdDist) / 3.0, -maxFollow, maxFollow);
            }

        } else {
            leftSpd = 0.0;
            rightSpd = 0.0;
            
        }

        driveSpd(leftSpd,rightSpd);

    }
    //Sets the gyro and encoders to zero
    public static void reset(){
        // eLeft.reset();
        // eRight.reset();
        // gyro.reset();
    }

    //Returns the average distance of the encoders(arthimetic mean)
    // public static double getDist(){
        // return (eLeft.getDistance() + eRight.getDistance())/2;
    // }

    /*
        "Whosever holds these PiDs, if he be worthy, shall posses the power of AJ"
    */

    //Drives the robot to a certain distance
    //Kinda complex -> DO NOT TOUCH
    // public static void driveDist(double goal, double kp, double max, boolean debug){
    //     double pos = getDist();
    //     double error = goal-pos;
    //     double aError = goal*0.05;
    //     double input = 0;

    //     while(true){
    //         pos = getDist();
    //         error = goal-pos;
    //         input = error*kp;
    //         input = OI.normalize(input, -max, max);

    //         driveSpd(input, input);

    //         if(debug){
    //             System.out.println("Input: " + input);
    //             System.out.println("Error: " + error);
    //             System.out.println("Position: " + pos);
    //             Timer.delay(0.05);
    //         }

    //         if(error <= aError){
    //             driveSpd(0.0, 0.0);
    //             System.out.println("YOINK, ya made it");
    //             break;
    //         }
    //     }
    // }

    //Turns the robot to a certain angle
    //Kinda complex -> DO NOT TOUCH
    // public static void turnToAngle(double goal, double kp, double max, boolean debug){
    //     double angl = gyro.getAngle();
    //     double error = goal-angl;
    //     double aError = goal*0.07;
    //     double input = 0;

    //     while(true){
    //         angl = getDist();
    //         error = goal-angl;
    //         input = error*kp;
    //         input = OI.normalize(input, -max, max);

    //         driveSpd(-input, input);

    //         if(debug){
    //             System.out.println("Input: " + input);
    //             System.out.println("Error: " + error);
    //             System.out.println("Angle: " + angl);
    //             Timer.delay(0.05);
    //         }

    //         if(error <= aError){
    //             driveSpd(0.0, 0.0);
    //             System.out.println("YOINK, ya made it");
    //             break;
    //         }
    //     }
    // }


    
    
}