package wizards.actors;

import wizards.maps.Light;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/8/11
 * Time: 9:17 AM
 */
public interface Actor {
    public void paint(Graphics g);
    public void update();
    public double[] getVelocity();
    public Point2D getPosition();
    public double getRadius();
    public void setPosition(Point2D pt);
    public void obstructed();
    public int getMoveType();
    public int getImpact();
    public Light getLight();
}
