package wizards.controllers;

import wizards.maps.WizardMap;
import wizards.actors.Wizard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class WizardController implements MouseListener, MouseMotionListener {
    Wizard wizard;
    WizardMap map;
    boolean ENABLED=false;
    Point2D target;
    GestureMachine gestures;
    public WizardController(){
        gestures = new GestureMachine();
    }

    public void setWizard(Wizard wiz){
        wizard = wiz;
    }

    public void setMap(WizardMap m){
        map = m;
    }
    public void mouseClicked(MouseEvent e) {

    }

    public void setEnabled(boolean t){
        ENABLED=t;
    }

    public void mousePressed(MouseEvent e) {
        if(!ENABLED) return;
        switch(e.getButton()){
                case MouseEvent.BUTTON1:
                    wizard.moveTo(map.getRealX(e.getX()),map.getRealY(e.getY()));
                    break;
                case MouseEvent.BUTTON3:
                    beginSpellGesture(e);
                    break;
            }

    }

    public void mouseReleased(MouseEvent e) {
        if(!ENABLED) return;
        if(e.getButton()==MouseEvent.BUTTON3){
            finishSpellGesture(e);
        }

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void drawStatus(Graphics g){

        g.setColor(Color.RED);
        if(gestures.gesturing){
            gestures.paint(g);
        }


        ((Graphics2D)g).setTransform(new AffineTransform());
        g.drawRect(575,25,10,50);
        int height = (int)((wizard.health*1.0/Wizard.MAX_HEALTH)*50);
        g.fillRect(575,75 - height,10,height);


    }

    public void mouseDragged(MouseEvent e) {
        if(!ENABLED) return;

        updateSpellGesture(e);

    }

    public void mouseMoved(MouseEvent e) {
    }

    void beginSpellGesture(MouseEvent e){
        target = new Point2D.Double(map.getRealX(e.getX()),map.getRealY(e.getY()));
        gestures.begin(target);
    }

    void finishSpellGesture(MouseEvent e){
        Point2D pt = new Point2D.Double(map.getRealX(e.getX()),map.getRealY(e.getY()));
        wizard.castTo(target.getX(), target.getY(),gestures.finish(pt));

    }

    void updateSpellGesture(MouseEvent e){
        if(!gestures.gesturing) return;
        
        Point2D pt = new Point2D.Double(map.getRealX(e.getX()),map.getRealY(e.getY()));
        gestures.update(pt);

    }
}

