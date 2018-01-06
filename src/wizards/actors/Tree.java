package wizards.actors;

import wizards.WizardApplication;
import wizards.maps.WizardMap;
import wizards.images.WizardImages;
import wizards.util.WrapAroundArray;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * A destroyable obstacle, that has 3-D appearance.
 *
 * User: mbs207
 * Date: 5/28/11
 * Time: 9:49 AM
 */
public class Tree implements Obstacle {
    Shape region;
    Image tree_top;
    Image tree_trunk;
    Image tree_burned;
    WrapAroundArray<Image> burning;
    final Point2D pos;
    boolean BURNING = false;
    boolean BURNED = false;
    boolean GONE = false;
    BurningTree fire;
    double offset = 300/30;
    int health = 100;
    double radius = 14;

    public Tree(Point2D position){
        
        region = new Ellipse2D.Double(position.getX() - radius, position.getY() - radius,2*radius, 2*radius);
        tree_top = WizardImages.tree_top;
        tree_trunk = WizardImages.tree_trunk;
        tree_burned = WizardImages.tree_burned;
        burning = new WrapAroundArray<Image>(WizardImages.tree_burning);
        pos = position;

    }

    public void paint(Graphics g) {
        if(region!=null){
            Rectangle bounds = region.getBounds();
            g.drawImage(tree_trunk,bounds.x - 5, bounds.y - 5,null);
        }else{
            g.drawImage(tree_burned,(int)(pos.getX()-radius),(int)(pos.getY()-radius),null);
        }
    }

    public void paintHigh(Graphics g) {
        if(BURNED) return;

        Rectangle bounds = region.getBounds();
        Point2D translate = new Point2D.Double(0,0);

        AffineTransform at = ((Graphics2D)g).getTransform();
        at.transform(translate,translate);
        double scale= 1/at.getScaleX();

        double delta_x  = ((pos.getX() + translate.getX()*scale)/30 - offset);
        double delta_y  = ((pos.getY() + translate.getY()*scale)/30 - offset);
        if(!BURNING){
            g.drawImage(tree_top, bounds.x + (int)delta_x - 5, bounds.y + (int)delta_y -5 ,null );
        }else{
            g.drawImage(burning.next(), bounds.x + (int)delta_x - 5, bounds.y + (int)delta_y -5 ,null );

        }
    }

    public boolean paintsHigh() {
        return true;
    }

    public Shape getRegion() {
        return region;
    }

    public void impact(int type){
        if(type== WizardMap.IMPACT_FIRE){
            health -= 1;
            if(!BURNING&&!BURNED){
                BURNING=true;
                fire = new BurningTree(this);
                WizardApplication.getApplication().addSpell(fire);
            }
        }

        if(health<20){
            if(!BURNED) WizardApplication.getApplication().removeSpell(fire);

            BURNED=true;

        }
        if(health<0){

            GONE=true;
            region = null;


        }

    }

}
