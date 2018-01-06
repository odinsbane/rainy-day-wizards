package wizards.actors;

import wizards.WizardApplication;
import wizards.images.WizardImages;
import wizards.maps.Light;
import wizards.util.WrapAroundArray;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Wizards will be a little bit like a dnd character.  Some stats a health
 * and magic.  P.O.C. one spell fireball moves around kills other wizards
 * and creatures.  Maybe some NPC but lets just get the engine down.
 *
 * User: mbs207
 * Date: 4/7/11
 * Time: 8:33 AM
 */
public class Wizard implements Actor {
    final static int STANDING = 0;
    final static int WALKING =  1;
    final static int CASTING =  2;
    final static int ENGAGED = 3;
    int radius = 10;
    int state = 0;

    final Point2D pos = new Point2D.Double(300,300);

    double velocity = 3;
    Dimension size = new Dimension(2*radius,2*radius);
    Ellipse2D bounds = new Ellipse2D.Double(pos.getX()-radius, pos.getY()-radius, size.getWidth(), size.getHeight());

    ArrayList<Point2D> destinations = new ArrayList<Point2D>();
    Point2D target;
    
    double strength = 10;
    double quickness = 10;
    double constitution = 10;

    public static double MAX_HEALTH = 100;
    public double health = 100;

    WrapAroundArray<Image> walking;
    WrapAroundArray<Image> standing;
    double[] theta = new double[]{0,1};
    AffineTransform facing = new AffineTransform();

    Spell shield;


    public Wizard(){
        walking = new WrapAroundArray<Image>(WizardImages.wizard_walking);
        standing = new WrapAroundArray<Image>(WizardImages.wizard_standing);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        //g2d.drawOval((int)(pos.getX() - radius), (int)(pos.getY() - radius), (int)(2*radius), (int)(2*radius));
        if(state==WALKING){

            facing.setToRotation(theta[1],-theta[0],pos.getX(),pos.getY());
            AffineTransform old = g2d.getTransform();
            g2d.transform(facing);

            g.drawImage(walking.next(),(int)pos.getX()-10,(int)pos.getY()-10,null);

            g2d.setTransform(old);

            return;
            
        }
        facing.setToRotation(theta[1],-theta[0],pos.getX(),pos.getY());
        AffineTransform old = g2d.getTransform();
        g2d.transform(facing);
        g.drawImage(standing.next(), (int) pos.getX() - 10, (int) pos.getY() - 10, null);
        g2d.setTransform(old);
    }

    /**
     * This is for 'thinking' type actions, the engine performs moves.
     *
     */
    public void update() {

        switch(state){
            case STANDING:
                rest();
                break;
            case ENGAGED:
                state=STANDING;
                break;
            default:
                //do nothing
                ;
        }

    }

    public double[] getVelocity() {
        if(destinations.size()==0){
            state = STANDING;
            return null;
        }
        if(target!=destinations.get(0)){
            target=destinations.get(0);
        }
        //Point2D pt = destinations.get(0);
        if(bounds.contains(target)){

            destinations.remove(0);

            return null;

        }

        double delta_x = target.getX() - pos.getX();
        double delta_y = target.getY() - pos.getY();

        double mag = Math.sqrt(Math.pow(delta_x,2) + Math.pow(delta_y,2));

        if(mag>velocity){
            delta_x = velocity * delta_x/mag;
            delta_y = velocity * delta_y/mag;
        }

        theta = new double[]{delta_x/mag, delta_y/mag};

        return new double[]{delta_x, delta_y};
    }

    public Point2D getPosition() {
        return pos;
    }

    public double getRadius() {
        return radius;
    }

    public void setPosition(Point2D pt) {
        pos.setLocation(pt);
        Point2D corner = new Point2D.Double(pos.getX() - radius,pos.getY() - radius);
        bounds.setFrameFromCenter(pos, corner);
    }

    public void rest(){

    }

    public void engage(){
        state = ENGAGED;
    }

    /**
     * Checks the state of the wizard, sets the destination and moves there.
     *
     * @param x
     * @param y
     */
    public void moveTo(double x, double y){
        if(state==ENGAGED) return;
        destinations.clear();
        destinations.add(new Point2D.Double(x, y));
        state = WALKING;

    }

    /**
     * For creating more complicated paths.
     *
     * @param x
     * @param y
     */
    public void appendMoveTo(int x, int y){
        destinations.add(new Point2D.Double(x,y));
        state = WALKING;
    }

    /**
     * Checks the state of the wizard.  Stops and casts a spell.
     *
     * @param x
     * @param y
     */
    public void castTo(double x, double y, int type){
        switch(type){
            case 1:
                ringOfFire();
                break;
            case 2:
                forceField();
                break;
            default:
                Spell s = new Fireball(pos.getX(),pos.getY(),x,y,this);
                WizardApplication.getApplication().addSpell(s);
        }
    }
    public void ringOfFire(){
        double tx = pos.getX();
        double ty = pos.getY();

        for(int i = -5; i <= 5; i++){
            for(int j = -5; j<=5; j++){


                Spell s = new Fireball(tx,ty,tx + i,ty + j,this);
                WizardApplication.getApplication().addSpell(s);

            }
        }
    }

    public void forceField(){
        if(shield==null){
            Spell s = new ForceField(this);
            WizardApplication.getApplication().addSpell(s);
        }
    }


    public void interact(Creature c){
        if(bounds.contains(c.pos)) defend(c);
        
    }



    void defend(Creature c){

        c.health = c.health - 5;
        if(c.health<0)
            WizardApplication.getApplication().removeCreature(c);
        state = ENGAGED;
        
    }

    public void interact(Spell s){

    }

    public void interact(Wizard w){

    }

    /**
     * To tell object has been obstructed.
     */
    public void obstructed(){

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


