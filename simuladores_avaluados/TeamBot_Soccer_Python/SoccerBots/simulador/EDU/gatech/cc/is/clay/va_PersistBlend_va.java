/*
 * va_PersistBlend_va.java
 */

package EDU.gatech.cc.is.clay;

import EDU.gatech.cc.is.util.Vec2;

/**
 * Remembers Vec2s for a given period of time.
 * If a new vector arrives, close in value to one of the existing
 * Vec2s, they are merged according to a "blending".
 * <p>
 * For detailed information on how to configure behaviors, see the
 * <A HREF="../clay/docs/index.html">Clay page</A>.
 * <p>
 * <A HREF="../COPYRIGHT.html">Copyright</A>
 * (c)1997, 1998 Tucker Balch
 *
 * @author Tucker Balch
 * @version $Revision: 1.1 $
 */


public class va_PersistBlend_va extends NodeVec2Array {
    /**
     * Turn debug printing on or off.
     */
    public static final boolean DEBUG = Node.DEBUG;

    /**
     * Maximum number of Vec2s to be remembered.
     */
    public static final int MAX_VECS = 50;

    private final NodeVec2Array embedded1;
    private final Vec2[] vecmem = new Vec2[MAX_VECS];
    private final long[] expiration = new long[MAX_VECS];
    Vec2[] last_val = new Vec2[0];
    long lasttime = 0;
    private long timeout = 1000;
    private double mergerange = 0;
    private double alpha = 0.75;

    /**
     * Instantiate a va_PersistBlend_va schema.
     *
     * @param p     double, the persistence in seconds.
     * @param mr    double, how close things must be to be considered the same.
     * @param alpha double, how much to weight old objects (must be 0=>alpha<=1).
     * @param im1   NodeVec2Array, the node that generates a list
     *              of items to filter.
     */
    public va_PersistBlend_va(double p, double mr, double a, NodeVec2Array im1) {
        if (DEBUG) System.out.println("va_PersistBlend_va: instantiated.");
        embedded1 = im1;
        alpha = a;
        timeout = (long) (p * 1000);
        mergerange = mr;
        for (int i = 0; i < MAX_VECS; i++) expiration[i] = -1;
    }

    /**
     * Return a Vec2Array that is the persistance.
     *
     * @param timestamp long, only get new information if timestamp > than last call
     *                  or timestamp == -1.
     * @return the array.
     */
    public Vec2[] Value(long timestamp) {
        if ((timestamp > lasttime) || (timestamp == -1)) {
            /*--- reset the timestamp ---*/
            if (timestamp > 0) lasttime = timestamp;

            /*--- get info from imbedded schema ---*/
            Vec2[] im1 = embedded1.Value(timestamp);

            /*--- merge ---*/
            for (int i = 0; i < im1.length; i++) {
                boolean match_old = false;
                int empty = -1;
                long oldstamp = 99999999;
                int oldest = 0;
                // for each vector in memory
                for (int j = 0; j < MAX_VECS; j++) {
                    // check if new reading is
                    // close to an old one
                    Vec2 tmp = new Vec2(im1[i].x,
                            im1[i].y);
                    if (vecmem[j] != null)
                        tmp.sub(vecmem[j]);
                    if ((tmp.r < mergerange)
                            && (timestamp < expiration[j])
                            && (vecmem[j] != null)) {
                        // have a match
                        empty = j;
                        match_old = true;
                        break;
                    }
                    // check if it's expired
                    else if (timestamp >= expiration[j]) {
                        empty = j;
                    }
                    // check if it's the oldest.
                    else if (expiration[j] < oldstamp) {
                        oldstamp = expiration[j];
                        oldest = j;
                    }
                }
                if (empty == -1) empty = oldest;
                if (match_old) {
                    //blend
                    vecmem[empty].setx(
                            alpha * vecmem[empty].x
                                    + (1 - alpha) * im1[i].x);
                    vecmem[empty].sety(
                            alpha * vecmem[empty].y
                                    + (1 - alpha) * im1[i].y);
                } else vecmem[empty] = im1[i];
                expiration[empty] = timestamp + timeout;
            }

            /*--- scan to see how many non-expired ---*/
            int count = 0;
            for (int i = 0; i < MAX_VECS; i++)
                if (timestamp < expiration[i]) count++;
            last_val = new Vec2[count];

            /*--- fill in the return array ---*/
            int pos = 0;
            for (int i = 0; i < MAX_VECS; i++)
                if (timestamp < expiration[i])
                    last_val[pos++] = vecmem[i];
        }
        Vec2[] retval = new Vec2[last_val.length];
        for (int i = 0; i < last_val.length; i++)
            retval[i] = new Vec2(last_val[i].x,
                    last_val[i].y);
        return (retval);
    }
}
