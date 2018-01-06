package wizards;

import wizards.actors.Creature;
import wizards.actors.Spell;
import wizards.actors.Wizard;
import wizards.controllers.WizardController;
import wizards.images.WizardImages;
import wizards.maps.DefaultMapOne;
import wizards.maps.WizardMap;
import wizards.scenarios.WizardScenario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.Timer;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/7/11
 * Time: 8:33 AM
 */
public class WizardApplication {
    WizardEngine engine;
    WizardPanel panel;
    ControlPanel controls;
    CutScenePanel cut_panel;
    int baseWidth = 600;
    int baseHeight = 600;

    WizardController player_controls;
    static WizardApplication app;

    public void init(){
        JFrame frame = new JFrame("Rainy Day Wizards");
        JPanel content_pain = new JPanel();
        content_pain.setLayout(new BorderLayout());

        JLayeredPane main_content = new JLayeredPane();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(baseWidth,baseHeight+100);

        WizardMap map = new DefaultMapOne();
        panel = new WizardPanel(map);
        main_content.add(panel,new Integer(-1));
        cut_panel = new CutScenePanel();

        cut_panel.setOpaque(false);
        main_content.add(cut_panel,new Integer(1));

        controls = new ControlPanel();

        main_content.add(controls, new Integer(0));

        PlayerControlPanel player_control_panel = new PlayerControlPanel();
        player_control_panel.setSize(600,100);

        WizardEngine we = new WizardEngine();
        we.start();

        content_pain.add(main_content, BorderLayout.CENTER);

        frame.setContentPane(content_pain);
        frame.setVisible(true);

        Timer timer = new java.util.Timer();
        timer.schedule(
                new TimerTask(){
                    public void run(){
                        panel.repaint();
                    }
                },100,35);

        engine = we;
        app = this;

        player_controls = new WizardController();
        frame.addComponentListener(sizeController());


    }

    public void startScenario(){
        panel.reset();
        cut_panel.clearMessage();
        panel.setPlayerController(player_controls);

        WizardImages.loadImages();

        WizardScenario def = WizardScenario.getDefaultScenario();
        def.init(this,player_controls);
        panel.setMap(def.getMap());




        engine.startScenario(def);

    }

    public void setMap(WizardMap map){

        panel.setMap(map);

    }

    public void finishedScenario(){

        panel.removePlayerController();
        controls.setExposed(true);

    }


    public void addSpell(Spell s){

        engine.add(s);
        panel.addSpell(s);


    }

    public void removeSpell(Spell s){

        engine.remove(s);
        panel.removeSpell(s);

    }

    public void addCreature(Creature c){
        engine.add(c);
        panel.addCreature(c);
    }

    public void removeCreature(Creature c){
        engine.remove(c);
        panel.removeCreature(c);
    }

    public void removeWizard(Wizard w){
        engine.remove(w);
        panel.removeWizard(w);
    }

    public void addWizard(Wizard w){
        engine.add(w);
        panel.addWizard(w);
    }

    /**
     * Removes everything.
     */
    public void clearActors(){

        panel.reset();
        engine.clear();

    }

    public void showControls(){
        panel.removeMouseListener(player_controls);
        controls.setExposed(true);
    }

    public void addCutSceneText(String line){
        cut_panel.showMessage(line);
    }

    public void showCutScene(){
        cut_panel.showScene(true);
        cut_panel.repaint();
        cut_panel.waitOnMessage();
        cut_panel.showScene(false);
        panel.repaint();
    }

    public void clearCutSceneMessages(){
        cut_panel.clearMessage();
    }


    public static WizardApplication getApplication(){
        return app;
    }

   /**
     * Finds wizards that are within a cone of view.  Note the cone has to be forward.
     *
     * @param position origin
     * @param direction facing direction, magnitude should be 1.
     * @param angle sin of the angle that represents the cone.
     * @param range maximum distance
     * @return collections of wizards that fall in the above code.
     */
    public Set<Wizard> lookForWizards(Point2D position, Point2D direction, double angle, double range){
       return engine.lookForWizards(position, direction, angle, range);
    }

    public static void main(String[] args){

        WizardApplication app = new WizardApplication();
        app.init();

    }

    private ComponentListener sizeController(){
        ComponentListener listen = new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = e.getComponent();
                int h = c.getHeight();
                int w = c.getWidth();

                double scaleX = w*1.0/baseWidth;
                double scaleY = h*1.0/baseHeight;

                panel.setScale(scaleX<scaleY?scaleX:scaleY);

                Dimension d = panel.getPreferredSize();

                int dw =  (w-d.width)/2;
                int dh = (h-d.height)/2;
                panel.setBounds(dw,dh,d.width, d.height);

                c.invalidate();
                c.repaint();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        };
        return listen;
    }

    public void endApplication(){
        
        System.exit(0);

    }


}

