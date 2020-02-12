package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

//This controllers are user input
//It also has the normalize method cuz IDK where else to put it
//To add another controller:
//  1. Copy the line that includes "Joystick contOne = new Joystick(0)"
//  2. Change "contOne" to "contTwo"
//  3. Change "Joystick(0)" to "Joystick(1)"
public class OI {

    private static Joystick[] cont = new Joystick[3];
    

    public static void createJoystick(){
        for(int i = 0; i < cont.length; i++){
            cont[i] = new Joystick(i);
        }
    }
    //Returns whether or not the button is being pressed
    //The method takes in the RobotMap button
    //i.e "RobotMap.A_BUTTON"
    public static boolean button(int button, int i){
        return cont[i].getRawButton(button);
    }

    //Returns how much the axises is being pushed or pulled down
    //The method takes in RobotMap axis
    //i.e. "RobotMap.LT"
    public static double axis(int axis, int i){
        return cont[i].getRawAxis(axis);
    }

    //Takes in a value and some bounds and forces it within those bounds
    //Used pretty much everywhere, so make sure you don't change anything to drastic.
    public static double normalize(double value, double min, double max){
        if(value > max)
            return max;
        else if(value < min)
            return min;
        else 
            return value;

       //return value > max ? max : value < min ? min : value;
    }


}