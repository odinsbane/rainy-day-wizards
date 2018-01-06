package wizards.maps;

import wizards.actors.Actor;
import wizards.actors.Obstacle;
import wizards.actors.Spell;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A dark map, a cave.
 * 
 * User: melkor
 * Date: 6/13/11
 * Time: 8:55 PM
 */
public class DefaultMapThree extends LoadedMap {
    BufferedImage shadow;
    HashSet<Light> lights = new HashSet<Light>();
    public DefaultMapThree() {
        super("default_map_three.txt");
        shadow=new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB_PRE);
        /*
        addLight(new Point2D.Double(200,-400),50);
        addLight(new Point2D.Double(200, -200), 50);
        addLight(new Point2D.Double(200, -100), 50);

        addLight(new Point2D.Double(400, -400), 50);
        addLight(new Point2D.Double(400, -200), 50);
        addLight(new Point2D.Double(400,-100),50);
        */
    }

    @Override
    public void paintHigh(Graphics g){

        for(Obstacle o: high_obstacles){
            o.paintHigh(g);
        }


        updateShadow();
        Graphics2D g2d = (Graphics2D)g;

        AffineTransform t = g2d.getTransform();
        double sx = t.getScaleX();
        double sy = t.getScaleY();

        g2d.setTransform(AffineTransform.getScaleInstance(sx, sy));
        g2d.drawImage(shadow,0,0,null);


    }

    @Override
    public void paintBackGround(Graphics g) {
        g.setColor(Color.GRAY);

        synchronized(regions){
            for(MapRegion bounds: regions){
                if(bounds.ACTIVE)
                    ((Graphics2D)g).fill(bounds.getShape());
            }
        }


    }

    public void addLight(Light l){
        lights.add(l);

    }

    public void removeLight(Light l){
        lights.remove(l);
    }

    public void updateShadow(){
        //shadow = new BufferedImage(600,600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sg = (Graphics2D)shadow.getGraphics();
        sg.setTransform(new AffineTransform());
        sg.setColor(new Color(0, 0, 0, 200));
        sg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        sg.fillRect(0, 0, 600, 600);
        sg.setColor(new Color(0, 0, 0, 0));
        sg.setTransform(displaced);
        for(Light l: lights){

                l.paintLight(sg);

        }
        sg.dispose();
    }

}

