package frc.parent;

/*
    RobotMap holds all the ports involved in the robot.
    This ranges from talon ports, all the way to the ports
    on the controllers. This also contains the polarity for the wheels
    and the various ports assoiated with sensors

    If you wish to create your own port, here is the syntax:
        public static final returnType name = value;
    Notes on creating ports:
        1. Ports must be integers or booleans
        2. they MUST be public static final;
        3. If the port is not plugged in, make int values -1, and boolean values false


*/
public interface RobotMap {

    // Wheel Talons
    public static final int FORWARD_LEFT = 3;
    public static final int FORWARD_RIGHT = 4;
    public static final int BACK_LEFT = 6;
    public static final int BACK_RIGHT = 5;

    // // Wheel Encoder Ports
    // public static final int ENCODER_A_LEFT = 0;// 0
    // public static final int ENCODER_B_LEFT = 1; // 1
    // public static final int ENCODER_A_RIGHT = 2; // 2
    // public static final int ENCODER_B_RIGHT = 3; // 3
       
    // Talon Ports for Intake
    public static final int INTAKE_L = 8;
    public static final int INTAKE_R = 7;

    // Talon ports for Climber
    public static final int CLIMBER_F = 1;
    public static final int CLIMBER_R = 2;

    // Talon ports for Elevator
    public static final int ELEVATOR = 0;
}