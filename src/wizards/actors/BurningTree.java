package wizards.actors;

import wizards.WizardApplication;
import wizards.maps.Light;
import wizards.maps.WizardMap;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *  When a tree burns this does the damage, it also lights up.
 *
 * User: mbs207
 * Date: 5/28/11
 * Time: 2:16 PM
 */
public class BurningTree extends Spell{
    Tree burned;
    double radius = 22.5;
    double[] velocity = new double[]{0,0};
    final Light l;
    BurningTree(Tree b){
        burned = b;
        l = new Light(b.pos, 40);
    }
    @Override
    public void interact(Wizard w) {
        w.health -= 1;
    }

    @Override
    public void interact(Creature c) {
        c.health -= 25;
        if(c.health<0) WizardApplication.getApplication().removeCreature(c);
    }

    @Override
    public void interact(Spell s) {

    }

    public void paint(Graphics g) {

    }

    public void update() {
    }

    public double[] getVelocity() {
        return velocity;
    }

    public Point2D getPosition() {
        return burned.pos;
    }

    public double getRadius() {
        return radius;
    }

    public void setPosition(Point2D pt) {
    }

    public void obstructed() {

    }

    public int getMoveType() {
        return 0;
    }

    public int getImpact() {
        return WizardMap.IMPACT_FIRE;
    }

    public Light getLight(){
        return l;
    }
}
