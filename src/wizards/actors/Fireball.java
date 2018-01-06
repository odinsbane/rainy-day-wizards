package wizards.actors;

import wizards.WizardApplication;
import wizards.maps.Light;
import wizards.maps.WizardMap;
import wizards.images.WizardImages;
import wizards.util.WrapAroundArray;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 5/24/11
 * Time: 7:47 AM
 */
public class Fireball extends Spell{
    final Point2D pos,destination;
    final Light light;
    int radius = 5;
    Dimension size = new Dimension(2*radius, 2*radius);
    double scalar_velocity = 10;
    double[] velocity;
    final static int FLYING = 0;
    final static int EXPLODING = 1;
    int state = 0;

    Ellipse2D bounds;

    WrapAroundArray<Image> frames;
    WrapAroundArray<Image> exploding_frames;

    /**
     * Creates a fire ball, that travels in a strait light and then explodes.
     * @param x
     * @param y
     * @param d_x
     * @param d_y
     * @param w
     */
    public Fireball(double x, double y, double d_x, double d_y, Wizard w){
        frames = new WrapAroundArray<Image>(WizardImages.spell_flying);
        exploding_frames = new WrapAroundArray<Image>(WizardImages.spell_exploding);
        pos = new Point2D.Double(x,y);
        light = new Light(pos,10*radius);

        destination = new Point2D.Double(d_x,d_y);
        bounds = new Ellipse2D.Double(x, y, 2*radius, 2*radius);
        owner = w;
        calculateVelocity();
    }

    public void calculateVelocity(){


        double delta_x = destination.getX() - pos.getX();
        double delta_y = destination.getY() - pos.getY();

        double mag = pos.distance(destination);
        if(mag==0){
            state=EXPLODING;
            return;
        }
        delta_x = scalar_velocity * delta_x/mag;
        delta_y = scalar_velocity * delta_y/mag;


        velocity = new double[]{delta_x, delta_y};

    }


    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        //g.drawOval((int)(pos.getX() - radius), (int)(pos.getY() - radius), (int)(2*radius), (int)(2*radius));

        if(state==FLYING){
            g.drawImage(frames.next(),(int)pos.getX()-10, (int)pos.getY()-10,null);
        }else{
           g.drawImage(exploding_frames.next(),(int)pos.getX()-20, (int)pos.getY()-20,null);
        }
    }


    public void update(){
        switch (state){
            case EXPLODING:
                explode();
                break;
            default:
                //nadda

        }
    }

    public double[] getVelocity() {
       return velocity;
    }

    public Point2D getPosition() {
        return pos;
    }

    public double getRadius() {
        return 0;
    }

    public void setPosition(Point2D pt) {
        pos.setLocation(pt);
        Point2D corner = new Point2D.Double(pos.getX() - radius,pos.getY() - radius);
        bounds.setFrameFromCenter(pos, corner);
    }

    public void explode(){
        radius++;
        size.setSize(radius*2, radius*2);
        Point2D pt2 = new Point2D.Double(pos.getX() - size.getWidth()*0.5 , pos.getY() - size.getHeight()*0.5);
        bounds.setFrameFromCenter(pos, pt2);
        if(radius>13) WizardApplication.getApplication().removeSpell(this);


    }


    public void interact(Wizard w) {


    }

    public void interact(Creature c) {
        switch(state){
            case FLYING:
                state = EXPLODING;
                velocity = null;
            case EXPLODING:
                c.health -= 25;
                if(c.health<0) WizardApplication.getApplication().removeCreature(c);

        }

    }

    public void interact(Spell o) {
    }

    @Override
    public Light getLight() {
        return light;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void obstructed(){
        state=EXPLODING;
        velocity = null;
    }

    public int getMoveType() {
        return 1<<1;
    }

    public int getImpact() {
        return WizardMap.IMPACT_FIRE;
    }
}
