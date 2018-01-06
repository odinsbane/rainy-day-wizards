package wizards.maps;

import java.awt.*;
import java.awt.geom.Point2D;

public class Light{
    final Point2D p;
    double radius;
    int x, y, r;
    public Light(Point2D p, double radius){
        this.p = p;
        this.radius = radius;

    }

    public void paintLight(Graphics g){
        r = (int)radius;
        x = (int)p.getX() - r;
        y = (int)p.getY() - r;
        g.fillOval(x, y, 2 * r, 2 * r);

    }




}
