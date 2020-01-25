package frc.robot;


import frc.parent.*;
import frc.robot.CCTalon;

//Extends the Mechanisms class
//Nothing to special
//Note: it only uses one talon, so using tTwo in anyway is cause an error
public class Elevator{

    static private CCTalon one = new CCTalon(RobotMap.ELEVATOR, false);

    public static void set(double speed){
        one.set(speed);
    }
    
}