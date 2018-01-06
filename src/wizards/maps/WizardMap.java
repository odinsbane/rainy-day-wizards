package wizards.maps;

import wizards.actors.Obstacle;
import wizards.actors.Spell;
import wizards.actors.Wizard;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A base map, needs to be changed into an abstract class, so that new levels can be created.
 *
 * User: mbs207
 * Date: 4/7/11
 * Time: 8:39 AM
 */
public abstract class WizardMap {

    public final static int MOVE_FLY = 1<<1;
    public final static int MOVE_WALK = 1;

    public final static int IMPACT_FIRE = 1;

    int visibleWidth=600;
    int visibleHeight=600;

    double DX = 0;
    double DY = 0;
    AffineTransform displaced = AffineTransform.getTranslateInstance(DX,DY);

    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    ArrayList<Obstacle> high_obstacles = new ArrayList<Obstacle>();
    ArrayList<Obstacle> low_obstacles = new ArrayList<Obstacle>();

    final Object obstacle_lock = new Object();

    ConcurrentLinkedQueue<Obstacle> pending_obstacles = new ConcurrentLinkedQueue<Obstacle>();

    abstract public void paintBackGround(Graphics g);
    abstract public boolean checkOutOfBounds(Point2D p, double radius, int move_type);

    /**
     * Happens on the event queue, so only modify 'obstacles' on the event queue.
     * @param g
     */
    public void paint(Graphics g){
        updateObstacles();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,visibleWidth, visibleHeight);
        
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform at = g2d.getTransform();
        at.concatenate(displaced);
        g2d.setTransform(at);

        paintBackGround(g);

        for(Obstacle o: low_obstacles){
            o.paint(g);
        }

    }


    public void paintHigh(Graphics g){

        for(Obstacle o: high_obstacles){
            o.paintHigh(g);
        }

    }

    /**
     *  For moving actors around the map.
     *
     * @param p possible object position
     * @param radius size of object
     * @param move_type obstuctions will work differently for different things
     * @param impact environmental impact. Fire will burn trees, etc.
     * @return
     */
    public boolean obstructed(Point2D p, double radius, int move_type, int impact){
        if(checkOutOfBounds(p,radius,move_type)){
            return true;
        }

        boolean obs = false;
        for(Obstacle o: obstacles){

            Shape s = o.getRegion();
            if(s==null) continue;
            Rectangle2D bounds = s.getBounds2D();

            double cx = bounds.getCenterX();
            double cy = bounds.getCenterY();

            double max = Math.pow(bounds.getWidth()/2 + radius,2);

            double mag = Math.pow(p.getX() - cx, 2) + Math.pow(p.getY() - cy, 2);


            if(max>mag){
                o.impact(impact);
                obs = true;
            }
        }

        return obs;
    }


    /**
     *  Line of site obstructions
     * @return if there is an obstruction between two points.
     */
    public boolean obstructed(Point2D a, Point2D b){
        Point2D ray = new Point2D.Double(b.getX() - a.getX(), b.getY() - a.getY());
        double mag = Math.sqrt(Math.pow(ray.getX(), 2) + Math.pow(ray.getY(), 2));
        for(Obstacle o: obstacles){
            Shape s = o.getRegion();
            if(s==null) continue;
            Rectangle2D bounds = s.getBounds2D();

            double cx = bounds.getCenterX();
            double cy = bounds.getCenterY();
            double radius = bounds.getWidth()/2;

            Point2D ray2 = new Point2D.Double(cx - a.getX(), cy - a.getY());

            double cos_theta = (ray.getX() * ray2.getX() + ray.getY() + ray2.getY())/mag;

            if(cos_theta<0||cos_theta>mag)
                continue;

            double sin_theta = (ray.getX()*ray2.getY() - ray.getY()*ray2.getX())/mag;
            if(sin_theta<-radius||sin_theta>radius){
                continue;
            }
            return true;

        }

        return false;
    }

    public int getVisibleWidth(){
        return visibleWidth;
    }

    public int getVisibleHeight(){
        return visibleHeight;
    }

    public void translate(double dx, double dy){
        DX += dx;
        DY += dy;
        
        displaced = AffineTransform.getTranslateInstance(-DX, -DY);

    }

    /**
     * Places new obstacles into an concurrent linked queue so that the objects
     * can be added on the eventqueue.
     * 
     * @param o
     */
    public void addObstacle(Obstacle o){
        obstacles.add(o);
        pending_obstacles.add(o);
    }

    /**
     * called on the event queue during a repaint to prevent concurrent modifications.
     */
    private void updateObstacles(){

        while(pending_obstacles.size()>0){
            Obstacle o = pending_obstacles.poll();
            low_obstacles.add(o);

            if(o.paintsHigh())
                high_obstacles.add(o);
        }

    }



    public void updatePlayer(Wizard w){
        Point2D pt = w.getPosition();
        double dx = 0;
        double dy = 0;

        if(pt.getX() - DX - 200<0){

            dx = pt.getX() - DX - 200;

        } else if(pt.getX() - DX - 400>0){

            dx = pt.getX() - DX - 400;

        }

        if(pt.getY() - DY - 200<0){

            dy = pt.getY() - DY - 200;

        } else if(pt.getY() - DY - 400>0){
            dy = pt.getY() - DY - 400;
        }
        translate(dx,dy);



    }

    public void damageMap(Point2D pt, double radius, int type){


    }

    public double getRealX(double x){
        return x + DX;
    }

    public double getRealY(double y){
        return y + DY;
    }

    public void addRegion(MapRegion r){

    }

    /**
         *  Needs to be done on the event queue.
         * @param l 
        */
    public void addLight(Light l){

    }
    public void removeLight(Light l){

    }
}
