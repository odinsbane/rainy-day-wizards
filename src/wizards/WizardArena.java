package wizards;

import wizards.actors.Actor;
import wizards.actors.Creature;
import wizards.actors.Wizard;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 *
 * User: mbs207
 * Date: 5/29/11
 * Time: 9:04 AM
 */
public class WizardArena {

    static public void attack(Creature c, Wizard w, Point2D loc, double radius){
        double max = w.getRadius() + radius;
        max = max*max;

        double mag = loc.distanceSq(w.getPosition());
        if(mag<max){
            w.health -= 5;
        }


    }


}
