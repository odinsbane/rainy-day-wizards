package wizards.maps;

import wizards.actors.Obstacle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The basic map that is loaded from a file.  This will eventually become the only map class.
 *
 *
 * User: melkor
 * Date: 6/12/11
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoadedMap extends WizardMap{
    ArrayList<MapRegion> regions = new ArrayList<MapRegion>();

    public LoadedMap(String filename){
        try{
            InputStream is = WizardMap.class.getResourceAsStream("/maps/" + filename);

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