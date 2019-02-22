package frc.robot;

import edu.wpi.first.wpilibj.DigitalOutput;

public class RevLED{
    DigitalOutput ledController = new DigitalOutput(0);

    /**
     * Enables the PWM control
     */
    public void initLED(){
        ledController.enablePWM(1965);
    }

    /**
     * Sets the Duty Cycle/Pulse Width of the PWM
     * @param dutyCycle LED Pattern Table can be found at: http://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
     * Examples:
     * Red: 1805
     * Blue: 1935
     * Green: 1885
     * White: 1965
     */
    public void setDutyCycle(int dutyCycle){
        ledController.setPWMRate(dutyCycle);
    }
}