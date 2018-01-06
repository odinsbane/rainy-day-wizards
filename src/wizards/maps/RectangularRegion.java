package wizards.maps;

import wizards.actors.Obstacle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: melkor
 * Date: 6/18/11
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class RectangularRegion extends MapRegion{
    Shape shape;
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    RectangularRegion(int x, int y, int w, int h){
        shape = new Rectangle2D.Double(x,y,w,h);
    }
    RectangularRegion(Rectangle2D r){
        shape = new Rectangle2D.Double((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
    }
    @Override
    boolean containsCircle(Point2D center, double radius) {

        return shape.contains(center);

    }

    @Override
    boolean contains(Point2D p){
        return shape.contains(p);
    }

    @Override
    ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    @Override
    void addObstacle(Obstacle o) {
        obstacles.add(o);
    }

    Shape getShape(){
        return shape;
    }
}