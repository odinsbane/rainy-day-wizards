package wizards.actors;

import wizards.WizardApplication;
import wizards.images.WizardImages;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/18/11
 * Time: 7:29 AM
 */
public class Cavern extends Creature {

    int counter=0;
    public void update() {
        counter++;
        if(counter>60){
            //create a new creature
            Creature a = new Creature();
            a.setPosition(new Point2D.Double(pos.getX(), pos.getY()));
            WizardApplication.getApplication().addCreature(a);
            counter=0;
        }
    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;
        g.drawImage(WizardImages.cavern,(int)pos.getX()-27, (int)pos.getY()-27, null);
        //g2d.draw(bounds);

    }



    public void interact(Wizard w){

    }

    public double[] getVelocity(){
        return null;
    }

    public Point2D getPosition() {
        return pos;
    }

    public double getRadius() {
        return radius;
    }


}
