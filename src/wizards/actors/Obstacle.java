package wizards.actors;

import java.awt.*;

/**
 *
 *
 * User: mbs207
 * Date: 4/30/11
 * Time: 8:57 AM
 */
public interface Obstacle {

    public void paint(Graphics g);
    public void paintHigh(Graphics g);
    public boolean paintsHigh();
    public Shape getRegion();
    public void impact(int type);

}
