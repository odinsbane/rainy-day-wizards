package wizards.actors;

import wizards.images.WizardImages;
import wizards.maps.Light;
import wizards.maps.WizardMap;
import wizards.util.WrapAroundArray;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: melkor
 * Date: 6/18/11
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Flame extends Spell {
    final Point2D position = new Point2D.Double(0,0);
    Light light = new Light(position, 50);
    WrapAroundArray<Image> frames;
    public Flame(){
        frames = new WrapAroundArray<Image>(WizardImages.spell_flying);

    }

    @Override
    public void interact(Wizard w) {

    }

    @Override
    public void interact(Creature c) {

    }

    @Override
    public void interact(Spell s) {

    }

    public void paint(Graphics g) {
        g.drawImage(frames.next(),(int)position.getX()-10, (int)position.getY()-10,null);
    }

    public void update() {

    }

    public double[] getVelocity() {
        return null;
    }

    public Point2D getPosition() {
        return position;
    }

    public double getRadius() {
        return 0;
    }

    public void setPosition(Point2D pt) {
        position.setLocation(pt);
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
        return light;
    }
}
