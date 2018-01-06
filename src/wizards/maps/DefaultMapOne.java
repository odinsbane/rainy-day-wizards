package wizards.maps;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * First implementation of a WizardMap.
 *
 * User: mbs207
 * Date: 5/29/11
 * Time: 10:04 AM
 */
public class DefaultMapOne extends WizardMap{
    BufferedImage background;
    int BOUNDS = 600;


    public DefaultMapOne() {

        background = new BufferedImage(BOUNDS,BOUNDS,BufferedImage.TYPE_INT_ARGB );
        Graphics g = background.getGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0,0,600,600);
        g.dispose();
    }


    @Override
    public void paintBackGround(Graphics g) {

        g.drawImage(background, 0, 0, null);
    }

    @Override
    public boolean checkOutOfBounds(Point2D p, double radius, int move_type) {
        //checks if inside of map.
        boolean test_x = p.getX() - radius < 0 || p.getX() + radius >= BOUNDS;
        boolean test_y = p.getY() - radius < 0 || p.getY() + radius >= BOUNDS;
        if(test_x||test_y)
            return true;
        return false;


    }
}
