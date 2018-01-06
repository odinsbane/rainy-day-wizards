package wizards.maps;

import wizards.actors.Obstacle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * User: mbs207
 * Date: 5/29/11
 * Time: 10:58 AM
 */
public class DefaultMapTwo extends WizardMap{
    ArrayList<MapRegion> regions = new ArrayList<MapRegion>();

    public DefaultMapTwo(){
        try{
            InputStream is = WizardMap.class.getResourceAsStream("/maps/default_map_two.txt");
            if(is==null) System.exit(1);
            MapEditor.loadMap(is,this);
        }catch(Exception e){

            e.printStackTrace();

        }


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

    @Override
    public boolean checkOutOfBounds(Point2D p, double radius, int move_type) {
        synchronized(regions){
            for(MapRegion bounds: regions){
                if(bounds.containsCircle(p,radius)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void addRegion(MapRegion r){
        regions.add(r);
        if(r.ACTIVE){
            for(Obstacle o: r.getObstacles()){
                addObstacle(o);
            }
        }
    }
}
