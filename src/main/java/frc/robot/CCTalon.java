package frc.robot;
//Imports talon class for the motors
//import edu.wpi.first.wpilibj.Talon;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class CCTalon extends WPI_TalonSRX
{
	//Controls the polarity of the motor controllers
	private boolean _reverse;

	//@param channel used in superclass constructor 
	//boolean reverse used to set the polarity of the controller
	public CCTalon(int channel, boolean reverse) 
	{
		super(channel);
		_reverse = reverse;
	}
	
	//Sets the speed for the talon
	//If the _reverse == true, then the power the motor in reverse
	//ControlMode.PercentOutput tells the method to set voltage of motorcontroller to the speed
	// [-100%, 100%] power only 
	public void set(double speed) 
	{
		if(_reverse)  
			super.set(-speed);
		else 
			super.set(speed);
		
	}
}
