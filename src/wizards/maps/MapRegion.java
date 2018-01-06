package wizards.maps;

import wizards.actors.Obstacle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Map region for use with loading maps.
 *
 * User: melkor
 * Date: 6/11/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MapRegion{
    boolean ACTIVE=false;
    abstract boolean containsCircle(Point2D center,double radius);
    abstract boolean contains(Point2D p);
    abstract ArrayList<Obstacle> getObstacles();
    abstract void addObstacle(Obstacle o);
    abstract Shape getShape();
}