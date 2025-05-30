/*
 * PositionMessage.java
 */

package EDU.gatech.cc.is.communication;

import EDU.gatech.cc.is.util.Vec2;

import java.io.Serializable;


/**
 * Position messages communicated to/from a robot.  Sends a Vec2.
 * <p>
 * <A HREF="../COPYRIGHT.html">Copyright</A>
 * (c)1998 Tucker Balch
 *
 * @author Tucker Balch
 * @version $Revision: 1.1 $
 */

public class PositionMessage extends Message
		implements Cloneable, Serializable {
	/**
	 * the position of the sender in global coordinates.
	 */
	public Vec2 val = new Vec2();


	/**
	 * create a position message.
	 *
	 * @param p Vec2, a position.
	 */
	public PositionMessage(Vec2 p) {
		val = p;
	}
}
