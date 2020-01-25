package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/*  
    CCTalon allows us to control the Talon motor controllers
    ALL motor controllers are controlled through CCTalon
    Its like Talon, but better
*/
public class CCTalon extends TalonSRX{

    //Constructs the talon object
    public CCTalon(int port, boolean reverse){
        super(port);
        super.setInverted(reverse);
    }

    //Sets the motor controller to a PercentOutput based on speed and polarity
    public void set(double speed){
        super.set(ControlMode.PercentOutput, speed);
    }

}

