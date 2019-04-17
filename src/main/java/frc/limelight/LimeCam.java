
package frc.limelight;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeCam {
    //Creates a new network table
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-bruin");
    //Creates the x, y, and area entries 
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableEntry ledMode = table.getEntry("ledMode");

    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double led = ledMode.getDouble(-1);
    double numTarg = tv.getDouble(0.0);
    
    
    
    //Posts the x, y, and the area to the smart dashboard
    // SmartDashboard.putNumber("LimelightX", x);
    // SmartDashboard.putNumber("LimelightY", y);
    // SmartDashboard.putNumber("LimelightArea", area);

    //Estimates the distance using MATH:
    //Uses the area of the recongiized object to calculate the distance
    //Sees the percentage of the screen that the object is taking up, and returns the distance 
    public double estimateDistanceViaArea(){
        //final double PROPCONST = 9.75; //need to measure
        double dist = 73.7 - 21.6 * Math.log(ta.getDouble(0.0));//*PROPCONST;
        //System.out.println("Distance from limelight: " + dist);
        return dist;
    }

    public double estimateTargetAngle(){
        double offset = 0.0907*Math.pow(ta.getDouble(0.0), 2)-2.27*ta.getDouble(0.0)-5.29;
        return tx.getDouble(0) - offset;
    }

    //Elitimates the distance using DIFFERENT MATH:
    //Uses angle between the object and the camera to calculate the horizontal distance
    //NOTE: estimateDistanceViaTrig() is MORE accurate than estimateDistanceViaArea()

/*

    public double estimateDistanceViaTrig(){
        //equation constants, NEED TO MEASURE HEIGHT OF THE LIME LIGHT 
        final double h1 = 0.0; //height of camera off ground
        final double h2 = 0.0; //height of target off ground
        final double a1 = 0.0; //angle of camera mount off ground

        //off of limelight docs at http://docs.limelightvision.io/en/latest/theory.html#from-pixels-to-angles      
        //x angle is not used but may be used later                                  
        double a2 = 0.0; //Angle of the camera to the object 
        double pixX = px.getDouble(0.0);
        double pixY = py.getDouble(0.0);
        double nx = (1/160) * (pixX - 159.5); //Calculates the noramlized pixel x
        double ny = (1/120) * (119.5 - pixY); //Calculates the normalized pixel y
        double vpw = 2.0 * Math.tan(54/2); //gets the vertical and horizontal field of view size
        double vph = 2.0 * Math.tan(41/2);
        double x = vpw/2 * nx; //calculates the x pixel location
        double y = vph/2 * ny; //calculatrs the y pixel location
        double ax = Math.atan2(1, x);
        double ay = Math.atan2(1, y);
        a2 = ay;
        
        double dist = (h2-h1) / Math.tan(a1+a2);
        return dist;                                                                                                                            
    }

    public double getVertAngle(){
        double pixY = py.getDouble(0.0); //gets y coordinante
        double ny = (1/120) * (119.5 - pixY); //Calculates the normalized pixel y
        double vpw = 2.0 * Math.tan(54/2); //gets the vertical and horizontal field of view size
        double vph = 2.0 * Math.tan(41/2); 
        double y = vph/2 * ny; //calculatrs the y pixel location
        double ay = Math.atan2(1, y); //gets the y angle
        return ay; //y angle is vertical angle
    }

    //returns the horizontal angle of the target from the camera center        
    public double getHortAngle(){
        double pixX = px.getDouble(0.0); //gets the x pixel position
        double nx = (1/160) * (pixX - 159.5); //Calculates the noramlized pixel x
        double vpw = 2.0 * Math.tan(54/2); //gets the vertical and horizontal field of view size
        double x = vpw/2 * nx; //calculates the x pixel location
        double ax = Math.atan2(1, x); //calculates the x angle
        return ax;
    }
    */

    public double getTX(){
        x = tx.getDouble(0.0);
        return x;
    }

    public double getTA(){
        double a = ta.getDouble(0.0);
        return a;
    }

    public boolean targetExists() {
        numTarg = tv.getDouble(0.0);
		if(numTarg > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public double getTargetAngle() {
        double targetAngle = 0;
        double distFromCenter = 0;
        double limelightToCenter = 7.75; //need to measure this value
        distFromCenter = Math.sqrt(Math.pow(estimateDistanceViaArea(), 2) + Math.pow(limelightToCenter, 2) - 2 * limelightToCenter * estimateDistanceViaArea() * Math.cos(getTX()));
      //  System.out.println("Distance from center: " +distFromCenter);
        targetAngle = getTX() * estimateDistanceViaArea() /  distFromCenter; 
        return targetAngle;
    }

    /**
     * 
     * @param lightOn
     */
    public void setLED(boolean lightOn){
        if(!lightOn){
            ledMode.setNumber(1);
        }
        else{
            ledMode.setNumber(3);
        }
    }
}