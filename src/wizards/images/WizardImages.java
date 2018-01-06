package wizards.images;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/22/11
 * Time: 2:51 PM
 */
public class WizardImages {
    public static Image[] wizard_walking;
    public static Image[] wizard_standing;
    public static Image[] wizard_casting;

    public static Image[] spell_flying;
    public static Image[] spell_exploding;

    public static Image[] creature_walking;
    public static Image[] creature_searching;

    public static Image cavern;
    public static Image[] rocks;

    public static Image tree_top;
    public static Image tree_trunk;
    public static Image tree_burned;
    public static Image[] tree_burning;

    public static Image beacon;

    public static boolean loaded = false;
    public static void loadImages(){
        if(loaded) return;

        wizard_walking = new Image[7];
        Image def = new BufferedImage(20,20,BufferedImage.TYPE_INT_ARGB);

        Graphics g = def.getGraphics();
        g.setColor(Color.RED);
        g.drawOval(0,0,15,15);
        g.dispose();

        for(int i = 0; i<7; i++){

            String name = "w_walk" + i + ".png";
            try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/"+name);
                BufferedImage im = ImageIO.read(is);
                wizard_walking[i] = im;
            } catch(Exception e){
                //missed.
                wizard_walking[i] = def;
            }
        }

        wizard_standing = new Image[1];
        try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/w_stand.png");
                BufferedImage im = ImageIO.read(is);
                wizard_standing[0] = im;
        } catch(Exception e){
            //missed.
            wizard_standing[0] = def;

        }

        spell_flying = new Image[5];

        for(int i = 0; i<5; i++){

            String name = "s_flying_" + i + ".png";
            try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/"+name);
                BufferedImage im = ImageIO.read(is);
                spell_flying[i] = im;
            } catch(Exception e){
                //missed.
                spell_flying[i] = def;
            }
        }

        spell_exploding = new Image[8];

        for(int i = 0; i<8; i++){

            String name = "spell_exploding_" + i + ".png";
            try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/"+name);
                BufferedImage im = ImageIO.read(is);
                spell_exploding[i] = im;
            } catch(Exception e){
                //missed.
                spell_exploding[i] = def;
            }
        }

        creature_walking = new Image[9];

        for(int i = 0; i<9; i++){

            String name = String.format("creature%03d.png",i+1);
            try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/"+name);
                BufferedImage im = ImageIO.read(is);
                creature_walking[i] = im;
            } catch(Exception e){
                //missed.
                creature_walking[i] = def;
            }
        }

        try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/cave.png");
                BufferedImage im = ImageIO.read(is);
                cavern = im;
            } catch(Exception e){
                //missed.
                cavern = def;
            }
        try{
            InputStream is = WizardImages.class.getResourceAsStream("/images/beacon.png");
            BufferedImage im = ImageIO.read(is);
            beacon = im;
        } catch(Exception e){
                //missed.
                beacon = def;
        }

        rocks = new Image[1];

        for(int i = 0; i<rocks.length; i++){

            String name = "rock_" + i + ".png";
            try{
                InputStream is = WizardImages.class.getResourceAsStream("/images/"+name);
                BufferedImage im = ImageIO.read(is);
                rocks[i] = im;
            } catch(Exception e){
                //missed.
                e.printStackTrace();
                rocks[i] = def;
            }
        }

        try{
            InputStream is = WizardImages.class.getResourceAsStream("/images/tree_top.png");
            BufferedImage im = ImageIO.read(is);
            tree_top = im;

        } catch(Exception e){
            //missed.
            e.printStackTrace();
            tree_top = def;
        }

        try{
            InputStream is = WizardImages.class.getResourceAsStream("/images/tree_trunk.png");
            BufferedImage im = ImageIO.read(is);
            tree_trunk = im;

        } catch(Exception e){
            //missed.
            e.printStackTrace();
            tree_trunk = def;
        }

        try{
            InputStream is = WizardImages.class.getResourceAsStream("/images/tree_burned.png");
            BufferedImage im = ImageIO.read(is);
            tree_burned = im;

        } catch(Exception e){
            //missed.
            e.printStackTrace();
            tree_burned = def;
        }

        tree_burning = new Image[6];

        for(int i = 0; i<tree_burning.length; i++){
            try{
                InputStream is = WizardImages.class.getResourceAsStream(String.format("/images/tree_burning%03d.png",i+1));
                BufferedImage im = ImageIO.read(is);
                tree_burning[i] = im;

            } catch(Exception e){
                //missed.
                e.printStackTrace();
                tree_burning[i] = def;
            }
        }

        loaded = true;
    }

}
