package frc.robot;
//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());

	// The Joystick that the driver is using

import edu.wpi.first.wpilibj.Joystick;
import static frc.robot.RobotMap.*;
import edu.wpi.first.wpilibj.buttons.Button;
import frc.robot.DPadButton.Direction;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private Joystick _controller;
	private Joystick _controller2;
	private DPadButton left;
	private DPadButton down;
	private DPadButton up;
	private DPadButton right;
	private DPadButton left2;
	private DPadButton down2;
	private DPadButton up2;
	private DPadButton right2;

	public OI()
	{
		//Sets _controller to to the correct input
		_controller = new Joystick ( 0 );
		_controller2 = new Joystick ( 1 );
		left = new DPadButton (_controller, Direction.LEFT);
		down = new DPadButton (_controller, Direction.DOWN);
		up = new DPadButton (_controller, Direction.UP);
		right = new DPadButton (_controller, Direction.RIGHT);
		left2 = new DPadButton (_controller2, Direction.LEFT);
		down2 = new DPadButton (_controller2, Direction.DOWN);
		up2 = new DPadButton (_controller2, Direction.UP);
		right2 = new DPadButton (_controller2, Direction.RIGHT);
	}

	/**
	 * Prints the axises in the console
	 */
	public void printAxis()
	{
		//Prints all of the axises on the controller
		for( int i = 0; i < _controller.getAxisCount(); i++ )
		{
			System.out.printf( "%10s", i + " " + _controller.getRawAxis( i ) + "\t" );

		}
		System.out.println();
	}

	/**
	 * Prints the value assigned to the pressed button
	 */
	public void printButtons()
	{
		// Prints all of the buttons on the controller
		for( int i = 1; i <= _controller.getButtonCount(); i++ )
		{
			System.out.printf( "%10s", i + "   " + _controller.getRawButton( i ) + "\t" );
		}
		System.out.println();
	}

	/**
	 * Get the value of the selected axis
	 * @param axis
	 * @return
	 */
	public double getAxis( int axis )
	{
		// Returns the value of the specified axis.
		return _controller.getRawAxis( axis );
	}

	public double getLJoystickXAxis()
	{
		// Returns the left _controller's horizontal value.
		return _controller.getRawAxis( L_JOYSTICK_HORIZONTAL );
	}

	public double getLJoystickYAxis()
	{
		// Returns the left _controller's vertical value, which is inverted.
		return _controller.getRawAxis( L_JOYSTICK_VERTICAL ) * -1;
	}

	public double getRJoystickXAxis()
	{
		// Returns the right _controller's horizontal value.
		return _controller.getRawAxis( R_JOYSTICK_HORIZONTAL );
	}

	public double getRJoystickYAxis()
	{
		// Returns the right _controller's vertical value, which is inverted.
		return _controller.getRawAxis( R_JOYSTICK_VERTICAL ) * -1;
	}

	public double getRJoystickXAxisC2()
	{
		// Returns the right _controller's horizontal value.
		return _controller2.getRawAxis( R_JOYSTICK_HORIZONTAL );
	}

	public double getRJoystickYAxisC2()
	{
		// Returns the right _controller's vertical value, which is inverted.
		return _controller2.getRawAxis( R_JOYSTICK_VERTICAL ) * -1;
	}

	public double getLT( )
	{
		return _controller.getRawAxis(  LT );
	}

	public double getRT( )
	{
		return _controller.getRawAxis( RT );
	}

	public double getLTC2( )
	{
		return _controller2.getRawAxis(  LT );
	}

	public double getRTC2( )
	{
		return _controller2.getRawAxis( RT );
	}

	public boolean getAButton( )
	{
		//Returns whether or not the x button is being pressed
		return _controller.getRawButton( A_BUTTON );
	}

	public boolean getBButton( )
	{
		return _controller.getRawButton( B_BUTTON );
	}

	public boolean getYButton( )
	{	
		return _controller.getRawButton( Y_BUTTON );
	}

	public boolean getXButton( )
	{
		return _controller.getRawButton( X_BUTTON );
	}

	public boolean getAButtonC2( )
	{
		//Returns whether or not the x button is being pressed
		return _controller2.getRawButton( A_BUTTON );
	}

	public boolean getBButtonC2( )
	{
		return _controller2.getRawButton( B_BUTTON );
	}

	public boolean getYButtonC2( )
	{
		return _controller2.getRawButton( Y_BUTTON );
	}

	public boolean getXButtonC2( )
	{
		return _controller2.getRawButton( X_BUTTON );
	}

	public boolean getLB( ){
		return _controller.getRawButton( LB_BUTTON );
	}

	public boolean getRB( ){
		return _controller.getRawButton( RB_BUTTON );
	}

	public boolean getLBC2( ){
		return _controller2.getRawButton( LB_BUTTON );
	}

	public boolean getRBC2( ){
		return _controller2.getRawButton( RB_BUTTON );
	}

	public boolean getStart( ){
		return _controller.getRawButtonReleased( START_BUTTON );
	}

	public boolean getSelect( ){
		return _controller.getRawButtonReleased( SELECT_BUTTON );
	}

	
	public boolean getStartC2( ){
		return _controller2.getRawButtonReleased( START_BUTTON );
	}

	public boolean getSelectC2( ){
		return _controller2.getRawButtonReleased( SELECT_BUTTON );
	}

	public boolean getLeftPad(){
		return left.get();
	}

	public boolean getDownPad(){
		return down.get();
	}

	public boolean getUpPad(){
		return up.get();
	}

	public boolean getRightPad(){
		return left.get();
	}

	public boolean getLeftPadC2(){
		return left2.get();
	}

	public boolean getDownPadC2(){
		return down2.get();
	}

	public boolean getUpPadC2(){
		return up2.get();
	}

	public boolean getRightPadC2(){
		return left2.get();
	}

	/**
	 * Normalizes the a given value
	 * @param max
	 * @param min
	 * @param value
	 * @return
	 */
	public static double normalize( double value, double min, double zero, double max )
	{
		//If the value exceeds the maximum, set it back to the max
		if( value > max )
		{
			return max;
		}
		//If the value is lower than the minimum, set it back to the min
		else if( value < min )
		{
			return min;
		}
		//If the controller is at neutral position, set the speed to zero
		else if ( value == zero )
		{
			return 0;
		}
		//Otherwise the value is fine
		return value;
	}
	public static double deadBand( double value, double min, double deadBand, double max ){
		if(value > -deadBand && value < deadBand){
			return 0;
		} else if(value < -deadBand && value>min){
			return ((value * (10/9)) + (1/9));
		} else if (value < max){
			return ((value * (10/9)) - (1/9));
		} else {
			return 0;
		}
	}
}
