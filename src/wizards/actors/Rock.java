package wizards.actors;

import wizards.images.WizardImages;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * This is the first and most basic obstacle/obstruction.
 *
 * User: mbs207
 * Date: 4/30/11
 * Time: 8:56 AM
 */
public class Rock implements Obstacle {
    public final static int SMALL=0;
    public final static int MEDIUM=1;
    public final static int LARGE=2;
    
    Shape region;
    Image frame;

    /**
     * Creates an isolated rock.
     * @param position center of rock.
     * @param size one of three sizes.
     */
    public Rock(Point2D position, int size){

        region = new Ellipse2D.Double(position.getX() - 12, position.getY() - 12, 25, 25);
        frame = WizardImages.rocks[0];



    }

    public void paint(Graphics g) {
        Rectangle bounds = region.getBounds();
        g.drawImage(frame, bounds.x, bounds.y,null );
    }

    public void paintHigh(Graphics g) {
        //pass
    }

    public boolean paintsHigh() {
        return false;
    }

    public Shape getRegion() {
        return region;
    }

    public void impact(int type) {
        
    }


}
