package wizards.actors;

import wizards.WizardApplication;
import wizards.images.WizardImages;
import wizards.maps.WizardMap;
import wizards.util.WrapAroundArray;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * A pedastal that glow with fire when struck by a fire ball.
 *
 * User: melkor
 * Date: 6/18/11
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Beacon implements Obstacle{
    public boolean LIT;
    Point2D position;
    Shape region;
    Image img = WizardImages.beacon;

    int ox = img.getWidth(null)/2;
    int oy = img.getHeight(null)/2;
    int x,y;


    public Beacon(double x, double y){
        this.x = (int)x;
        this.y = (int)y;

        position = new Point2D.Double(x,y);
        LIT = false;
        region = new Ellipse2D.Double(x-20, y - 20, 40, 40);

    }
    public void paint(Graphics g) {
        g.drawImage(img, (int) x - ox, (int) y - oy, null);
    }

    public void paintHigh(Graphics g) {
    }

    public boolean paintsHigh() {
        return false;
    }

    public Shape getRegion() {
        return region;  //To change body of implemented methods use File | Settings | File Templates.
    }
    public void light(){
        LIT=true;
        Flame f = new Flame();
        f.setPosition(position);
        WizardApplication.getApplication().addSpell(f);
    }
    public void impact(int type) {
        if(LIT) return;
        if(type== WizardMap.IMPACT_FIRE){
            light();
        }
    }
}
