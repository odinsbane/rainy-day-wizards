package wizards.actors;

import wizards.WizardApplication;
import wizards.WizardArena;
import wizards.images.WizardImages;
import wizards.maps.Light;
import wizards.util.WrapAroundArray;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Creature base class this one will search for a wizard and then attack it.
 *
 * User: mbs207
 * Date: 4/7/11
 * Time: 9:32 AM
 */
public class Creature implements Actor {
    Wizard owner;
    int state = 0;
    final static int SEARCHING = 0;
    final static int PATROL = 1;
    final static int RUNNING = 2;
    final static int ENGAGING = 3;


    double radius = 17;
    Ellipse2D bounds= new Ellipse2D.Double(400 - 5 ,400 - 5,10,10);
    Point2D view = new Point2D.Double(1,0);
    Wizard victim;

    volatile int engage_counter=0;

    Point2D attack_location;
    double attack_radius = radius*0.5;
    final Point2D pos =  new Point2D.Double(400,400);
    Point2D target;



    double SIGHT_LIMIT = 150;
    double FIELD_OF_VIEW = Math.sin(60*Math.PI/180);

    double velocity = 4;

    WrapAroundArray<Image> frames = new WrapAroundArray<Image>(WizardImages.creature_walking);
    
    int count = 0;

    AffineTransform rotator;

    public double health = 50;
    public Creature(){
        int angle = (int)(System.currentTimeMillis()&7) - 4;
        angle = angle==0?1:angle;
        rotator = AffineTransform.getRotateInstance(angle*Math.PI/180);
        state = PATROL;
        target = new Point2D.Double(pos.getX() + view.getX()*200, pos.getY() + view.getY()*200);
    }
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        //g.drawOval((int)(pos.getX() - radius), (int)(pos.getY() - radius), (int)(2*radius), (int)(2*radius));

        
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform facing = new AffineTransform();

        facing.setToRotation(-view.getY(),view.getX(),pos.getX(),pos.getY());
        AffineTransform old = g2d.getTransform();
        g2d.transform(facing);

        g.drawImage(frames.next(),(int)pos.getX()-22,(int)pos.getY()-22,null);

        if(engage_counter>0){

            int ar = (int) attack_radius;
            g2d.setColor(Color.RED);
            g2d.drawOval((int)(pos.getX()) - ar,(int)(-0.75*radius + pos.getY()) - ar, 2*ar, 2*ar );

        }
        
        g2d.setTransform(old);




    }

    public void update() {
        double angle = FIELD_OF_VIEW;

        switch(state){
            case SEARCHING:
                count++;
                rotator.transform(view,view);
                if(count>10){
                    count=0;
                    target = new Point2D.Double(pos.getX() + view.getX()*200, pos.getY() + view.getY()*200);
                    state=PATROL;
                }
                break;
            case PATROL:
                angle = angle*0.5;
                break;
            case ENGAGING:
                engaging();
            default:
                return;

        }
        java.util.Set<Wizard> found = WizardApplication.getApplication().lookForWizards(pos,view,angle,SIGHT_LIMIT);
        for(Wizard w: found){
            if(lookAt(w)){
                break;
            }
        }
    }

    public void engaging(){
        
        if(engage_counter==0){
            //look at the wizard.
            target = new Point2D.Double(victim.pos.getX(), victim.pos.getY());
            double delta_x = target.getX() - pos.getX();
            double delta_y = target.getY() - pos.getY();
            double mag = Math.sqrt(Math.pow(delta_x,2) + Math.pow(delta_y,2));

            view.setLocation(delta_x/mag, delta_y/mag);




        }

        if(engage_counter>=5){

            //attack
            target = new Point2D.Double(victim.pos.getX(), victim.pos.getY());
            double delta_x = target.getX() - pos.getX();
            double delta_y = target.getY() - pos.getY();
            double mag = Math.sqrt(Math.pow(delta_x,2) + Math.pow(delta_y,2));

            double f = radius/mag*0.75;
            attack_location = new Point2D.Double(pos.getX() + f*delta_x,pos.getY() + f*delta_y);

            WizardArena.attack(this, victim, attack_location, attack_radius);

            engage_counter=0;

            //too far away start looking again.
            if(mag>victim.getRadius() + radius){

                state=SEARCHING;
                return;

            }



        }

        engage_counter++;





    }
    public double[] getVelocity() {
        double vel = velocity;
        switch(state){
            case PATROL:
                vel = velocity * 0.5;
                break;
            case RUNNING:
                break;
            default:
                return null;

        }

        if(target==null) return null;

        if(bounds.contains(target)){
            target=null;
            state = SEARCHING;
            return null;
        }

        double delta_x = target.getX() - pos.getX();
        double delta_y = target.getY() - pos.getY();
        double mag = Math.sqrt(Math.pow(delta_x,2) + Math.pow(delta_y,2));

        view.setLocation(delta_x/mag, delta_y/mag);
        if(mag>vel){
            delta_x = vel * delta_x/mag;
            delta_y = vel * delta_y/mag;
        }

        return new double[]{delta_x, delta_y};
    }

    public Point2D getPosition() {
        return pos;
    }

    public double getRadius() {
        return radius;  
    }

    public void setPosition(Point2D pt){
        pos.setLocation(pt);
        Point2D corner = new Point2D.Double(pos.getX() - radius,pos.getY() - radius);
        bounds.setFrameFromCenter(pos, corner);

    }

    public void interact(Wizard w){

        if(w==owner)
            return;
        state = ENGAGING;
        victim = w;


    }

    boolean lookAt(Wizard w){
        if(w==owner) return false;
        target = new Point2D.Double(w.pos.getX(), w.pos.getY());
        state = RUNNING;
        return true;
    }

    /**
     * Does nothing to spells. They do to it.
     * @param s
     */
    public void interact(Spell s){

    }

    /**
     *
     * @param c
     */
    public void interact(Creature c){

    }

    public void obstructed(){

        state=SEARCHING;
        target=null;
        count=0;

    }

    public int getMoveType() {
        return 1;
    }

    public int getImpact() {
        return 0;
    }

    public Light getLight() {
        return null;
    }

}
