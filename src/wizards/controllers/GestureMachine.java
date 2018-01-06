package wizards.controllers;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 *
 * Date: 5/24/11
 * Time: 7:56 AM
 */
public class GestureMachine{

    final static int BOX_SIZE = 25;



    final static int RING_OF_FIRE = (((1<<2) + 1)<<16) +
                                    (((2<<2) + 1)<<12) +
                                    (((2<<2) + 0)<<8) +
                                    (((1<<2) + 0)<<4) +
                                    0;

    final static int FORCE_FIELD = (((1<<2) + 1)<<12) +
                                    (((2<<2) + 1)<<8) +
                                    (((1<<2) + 1)<<4) +
                                    (((0<<2) + 1)<<0);

    /* considering as an alternative.
       private final static int[][] ROF_path =
                    {
                        {1,1},
                        {2,1},
                        {2,0},
                        {1,0},
                        {0,0}
                     };
      final static int RING_OF_FIRE = pathFromCoordinates(ROF_path);
    */

    Point2D origin;
    int last = 0;
    int path;
    int count;
    boolean gesturing = false;
    public GestureMachine(){

    }

    /**
     * Starts  a new path for a gesture.
     * @param p starting point.
     */
    public void begin(Point2D p){
        gesturing = true;
        origin = p;

        //packs xy coordinates, first point is always 1,1
        last = (1<<2) + 1;
        count = 0;

        //path will accumulate all of the points.
        path=last;
        count++;

    }

    /**
     * Finds the relative position of the current point, and places it
     * into a gesture box.  Each box is (xdex<<2) + ydex
     *
     * @param p
     */
    public void update(Point2D p){
        //too many gesture points return.
        if(count>=8) return;

        //x index of box
        int x_dex = (int)(p.getX() - origin.getX() + 1.5* BOX_SIZE);
        x_dex = x_dex/ BOX_SIZE;

        //only the closest boxes are valid
        if(x_dex<0||x_dex>2) return;

        //same for y.
        int y_dex = (int)(p.getY() - origin.getY() + 1.5* BOX_SIZE);
        y_dex = y_dex/ BOX_SIZE;

        if(y_dex<0||y_dex>2) return;

        //The current position is within the same box ignore
        int now = (x_dex<<2) + y_dex;
        if(now==last) return;

        last = now;

        path = (path<<4) + now;
        count++;




    }

    /**
     *
     * @param p final point not used
     * @return return the spell number, if this spell is not known returns zero
     */
    public int finish(Point2D p){
        gesturing = false;

        switch(path){
            case RING_OF_FIRE:
                return 1;
            case FORCE_FIELD:
                return 2;
            default:
                return 0;
        }
    }

    public void paint(Graphics g){

        for(int i = -1; i< 2; i++){
            for(int j = -1; j<2; j++){
                int x = (int)(origin.getX() + i*BOX_SIZE - 0.5*BOX_SIZE);
                int y = (int)(origin.getY() + j*BOX_SIZE - 0.5*BOX_SIZE);
                g.drawRect(x,y,BOX_SIZE,BOX_SIZE);
            }
        }

        for(int i = 0; i<count; i++){
            int xdex = (path&(12<<4*i))>>(2+4*i);
            int x = (int)(origin.getX() + xdex*BOX_SIZE - 1.5*BOX_SIZE);
            int ydex = (path&(3<<4*i))>>(4*i);
            int y = (int)(origin.getY() + ydex*BOX_SIZE - 1.5*BOX_SIZE);
            g.drawOval(x,y,BOX_SIZE,BOX_SIZE);

        }


    }

    public static int pathFromCoordinates(int[][] coordinates){
        int path = 0;
        for(int[] c: coordinates){

            path = (path<<4) + (c[0]<<2) + c[1];


        }

        return path;
    }

}