/*
 * Capture.java
 */

package EDU.gatech.cc.is.abstractrobot;

import EDU.gatech.cc.is.communication.Transceiver;
import EDU.gatech.cc.is.util.Units;


/**
 * Provides an abstract interface to the hardware of
 * a foraging Nomad 150 robot.
 *
 * <B>Introduction</B><BR>
 * If you write a control system using this interface to the hardware,
 * you can test it in simulation and on mobile robots.
 * <p>
 * Capture robots are multiforaging robots with long-range vision
 * based on the Nomad 150 platform.
 * "Multi Foraging" means the robot can sense several
 * different classes of objects to collect, along with different bins in
 * which to to deposit them.
 * A SimpleN150 robot has sonar range finders, bumper
 * switches, and odometry and steering, translation and turret motors.
 * These capabilities are extended on a MultiForageN150 through the
 * addition of color vision and a gripper.
 * <p>
 * <B>Vision sensing</B><BR>
 * Vision hardware provides six "channels" that each track a different
 * type of object.  Each call to one of the vision routines requires a reference
 * to which channel is being accessed.
 * Bins and objects to collect are sensed by the same vision hardware.
 * <p>
 * <B>Frames of reference</B><BR>
 * We use a standard cartesian coordinate system in
 * meters and radians.  Pretend you are looking down
 * on the robot: +x
 * goes out to your right (East), +y goes up (North).
 * When the robot is initialized, it is facing the +x direction.
 * Headings are given in radians, with East=0, North=PI/2 and so on CCW
 * around to 2*PI.
 * Some methods return "egocentric" vectors.
 * An egocentric vector is given relative to the center of
 * the robot in the same heading reference frame as global coordinates.
 * An object one meter east of the robot is at (1,0) egocentrically.
 * <p>
 * <B>Implementations</B><BR>
 * This class is extended by a simulation class (CaptureSim).
 * The subclasse
 * handles details of interaction with the simulated
 * world.
 * <p>
 * <B>Timestamps</B><BR>
 * Many of the sensor and motor command
 * methods (e.g. get* and set*) require a timestamp as a parameter.
 * This is to help reduce the amount of I/O to the physical robot.
 * If the timestamp is less than or equal to the value sent on the
 * last call to one of these methods, old data is returned.
 * If the timestamp is -1 or greater than the last timestamp, the
 * robot is queried, and new data is returned.  The idea is
 * that during each control cycle the higher level software will
 * increment the timestamp and use it for all calls to these methods.
 *
 * <p>
 * <A HREF="../COPYRIGHT.html">Copyright</A>
 * (c)1997, 1998 Tucker Balch
 *
 * @author Tucker Balch
 * @version $Revision: 1.1 $
 * @see CaptureSim
 */

public interface Capture extends SimpleN150,
        VisualObjectSensor, GripperActuator, KinSensor, Transceiver {
    // some useful numbers
    double VISION_RANGE = 30.0;
    int VISION_FOV_DEG = 100;
    double VISION_FOV_RAD = Units.DegToRad(100);
    //public  static final double  GRIPPER_CAPTURE_RADIUS = 0.10; //10 cm
    double GRIPPER_CAPTURE_RADIUS = 0.06; //6 cm
    double GRIPPER_POSITION = 0.35; //from center
}
