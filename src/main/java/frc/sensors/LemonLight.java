package frc.sensors;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DigitalOutput;

public class LemonLight{
    public static NetworkTableInstance table = NetworkTableInstance.getDefault();
    public static NetworkTable cam = table.getTable("chameleon-vision").getSubTable("USB Camera-B4.09.24.1");
    public static NetworkTableEntry yaw = cam.getEntry("yaw");
    public static NetworkTableEntry is_valid = cam.getEntry("is_valid");
    public static NetworkTableEntry pose = cam.getEntry("pose");
    
    private static DigitalOutput relay;

    /**
     * @return Whether the camera is tracking a target
     */
    public static boolean hasTarget(){
        return is_valid.getBoolean(false);
    }

    /**
     * @return The Yaw of target from the center of the camera
     */
    public static double getYaw(){
        return yaw.getDouble(0);
    }

    /**
     * @return The estimated distance to the target in meters
     */
    public static double distToTarget(){
        double[] pos = pose.getDoubleArray(new double[] {0,0,0});
        return Math.sqrt(Math.pow(pos[0], 2) + Math.pow(pos[1], 2)); //Dist in meters
    }

    /**
     * Sets the output channel for the light relay
     * @param relayChannel The output channel foe the light relay
     */
    public static void setLightChannel(int relayChannel){
        relay = new DigitalOutput(relayChannel);
    }

    /**
     * Sets the light
     * @param status Enables/Disables the camera light
     */
    public static void setLight(boolean status){
        relay.set(!status);
    }
}