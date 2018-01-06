package wizards.actors;

import wizards.WizardApplication;
import wizards.maps.Light;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 5/24/11
 * Time: 7:43 AM
 */

public class ForceField extends Spell{
    double radius = 40;
    double power_remaining = 200;
    final Light l;
    public ForceField(Wizard o){

        owner = o;
        l = new Light(o.getPosition(), 2*radius);

    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawOval((int)(owner.pos.getX() - radius), (int)(owner.pos.getY()-radius), 2*(int)radius,2*(int)radius);
    }

    public void update() {
    }

    public double[] getVelocity() {
        return null;
    }

    public Point2D getPosition() {
        return owner.pos;
    }

    public double getRadius() {
        return radius;
    }

    public void setPosition(Point2D pt) {

    }

    public void obstructed() {

    }

    public int getMoveType() {
        return -1;
    }

    public int getImpact() {
        return 0;
    }

    @Override
    public void interact(Wizard w) {

    }

    @Override
    public void interact(Creature c) {
        if(power_remaining>c.health){
            power_remaining -= c.health;
            c.health = 0;
        } else{
            power_remaining = 0;
            c.health -= power_remaining;
        }
        if(c.health<=0) WizardApplication.getApplication().removeCreature(c);
        if(power_remaining<=0){
            owner.shield = null;
            WizardApplication.getApplication().removeSpell(this);
        }
    }


    @Override
    public void interact(Spell s) {
    }

    @Override
    public Light getLight() {
        return l;
    }
}
