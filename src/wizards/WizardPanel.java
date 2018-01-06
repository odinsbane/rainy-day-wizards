package wizards;

import wizards.actions.ActorAdder;
import wizards.actions.ActorRemover;
import wizards.actors.Actor;
import wizards.actors.Creature;
import wizards.actors.Spell;
import wizards.actors.Wizard;
import wizards.controllers.NullController;
import wizards.controllers.WizardController;
import wizards.maps.WizardMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

/**
 * Handles the painting.
 * 
 * User: mbs207
 * Date: 4/7/11
 * Time: 8:33 AM
 */
public class WizardPanel extends JPanel {
    ArrayList<Actor> wizards = new ArrayList<Actor>();
    ArrayList<Actor> spells = new ArrayList<Actor>();
    ArrayList<Actor> creatures = new ArrayList<Actor>();
    WizardMap map;
    WizardController player_control =new NullController();
    List<MouseListener> mouseListeners = new ArrayList<>();
    List<MouseMotionListener> motionListeners= new ArrayList<>();
    double scale = 1.0;
    AffineTransform scaleTransform = AffineTransform.getScaleInstance(1,1);
    Dimension scaledSize;
    public WizardPanel(WizardMap m){
        map = m;
        ScaledListener l = new ScaledListener();
        super.addMouseListener(l);
        super.addMouseMotionListener(l);
    }
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;

        g2d.transform(AffineTransform.getScaleInstance(scale, scale));
        map.paint(g);

        for(Actor a: wizards)
            a.paint(g);

        for(Actor a: spells)
            a.paint(g);


        for(Actor a: creatures)
            a.paint(g);

        map.paintHigh(g);

        player_control.drawStatus(g);

        //g.drawImage(shadow,0,0,this);


    }



    public void addWizard(Wizard w){
        addActor(w,wizards);
    }

    public void removeWizard(Wizard w){

        removeActor(w,wizards);
    }

    public void addSpell(Spell s){
        addActor(s, spells);
    }

    public void removeSpell(Spell s){
        removeActor(s,spells);
    }

    public void addActor(Actor a, ArrayList<Actor> list){
        EventQueue.invokeLater(new ActorAdder(a,list,map));
    }

    public void removeActor(Actor a, ArrayList<Actor> list){
        EventQueue.invokeLater(new ActorRemover(a,list,map));
    }

    public void addCreature(Creature c) {
        addActor(c,creatures);
    }

    public void removeCreature(Creature c) {
        removeActor(c,creatures);
    }

    public void setMap(WizardMap m){
        final WizardMap fm = m;
        EventQueue.invokeLater(new Runnable(){
           public void run(){
               map = fm;
           }
        });
    }
    public void setPlayerController(WizardController wc){
        player_control = wc;
        addMouseListener(player_control);
        addMouseMotionListener(player_control);
    }

    public void removePlayerController(){
        removeMouseListener(player_control);
        removeMouseMotionListener(player_control);
        player_control = new NullController();
    }
    public void reset(){
        EventQueue.invokeLater(new Runnable(){

            public void run() {
                wizards.clear();
                spells.clear();
                creatures.clear();
            }
        });

    }

    public void setScale(double v) {
        scale = v;
        int w = (int)(map.getVisibleWidth()*v);
        int h = (int)(map.getVisibleHeight()*v);
        scaledSize = new Dimension(w,h);
        scaleTransform = AffineTransform.getScaleInstance(1/scale, 1/scale);
    }

    @Override
    public Dimension getPreferredSize(){
        return scaledSize;
    }

    @Override
    public Dimension getMinimumSize(){
        return scaledSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return scaledSize;
    }

    @Override
    public void addMouseListener(MouseListener listener){
        mouseListeners.add(listener);

    }

    @Override
    public void removeMouseListener(MouseListener listener){
        mouseListeners.remove(listener);
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener listener){
        motionListeners.add(listener);
    }

    MouseEvent getScaledEvent(MouseEvent e){
        Point2D p = e.getPoint();
        Point2D n = new Point2D.Double();
        scaleTransform.transform(p, n);
        e.translatePoint((int)(n.getX() - p.getX()), (int)(n.getY() - p.getY()));
        return e;
    }
    private class ScaledListener implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            MouseEvent se = getScaledEvent(e);
            for(MouseListener l: mouseListeners){
                l.mouseClicked(se);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MouseEvent se = getScaledEvent(e);
            for(MouseListener l: mouseListeners){
                l.mousePressed(se);
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MouseEvent se = getScaledEvent(e);
            for(MouseListener l: mouseListeners){
                l.mouseReleased(se);
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            MouseEvent se = getScaledEvent(e);
            for(MouseListener l: mouseListeners){
                l.mouseEntered(se);
            }

        }

        @Override
        public void mouseExited(MouseEvent e) {
            MouseEvent se = getScaledEvent(e);
            for(MouseListener l: mouseListeners){
                l.mouseExited(se);
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            MouseEvent se = getScaledEvent(e);
            for(MouseMotionListener l: motionListeners){
                l.mouseDragged(se);
            }

        }

        @Override
        public void mouseMoved(MouseEvent e) {

            MouseEvent se = getScaledEvent(e);
            for(MouseMotionListener l: motionListeners){
                l.mouseMoved(se);
            }

        }
    }


}

