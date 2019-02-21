package frc.robot;

import edu.wpi.first.wpilibj.DigitalOutput;

public class VexLED{
    //PWM ledController = new PWM(0);
    DigitalOutput ledController = new DigitalOutput(0);

    public void setPulse(){
        ledController.enablePWM(1935);
    }
}