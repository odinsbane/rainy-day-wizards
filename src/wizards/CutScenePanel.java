package wizards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: melkor
 * Date: 5/7/11
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class CutScenePanel extends JPanel {

    boolean VISIBLE=false;

    ArrayList<String> lines;

    CutScenePanel(){
        lines = new ArrayList<String>();
        addMouseListener(new CutScenePanelListener(this));


    }

    public void clearMessage(){
        lines.clear();
    }

    public void showMessage(String line){
        final String s = line;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                lines.add(s);
            }
        });

    }

    public void showScene(boolean t){

        if(t){
            setBounds(50,100,500,400);
            VISIBLE = true;
        }else{
            setBounds(0,0,0,0);
            VISIBLE=false;
        }

    }

    public void paint(Graphics g){
        if(!VISIBLE) return;

        int i = 0;

        g.setColor(new Color(20,20,20,200));
        g.fillRect(0,0,500,400);

        g.setColor(Color.RED);
        g.drawOval(450,350,40,40);
        g.drawString("next",457,375);

        
        for(String line: lines){
            g.drawString(line,20, 120 + 15*i);
            i++;
        }


    }

    /**
     * Block until further notice.
     */
    public synchronized void waitOnMessage(){

        try{
            wait();
        }catch(Exception e){
            //imagine something going wrong.
        }

    }

    public synchronized void nextScene(){

        notifyAll();

    }



}

class CutScenePanelListener implements MouseListener {
    CutScenePanel panel;
    Ellipse2D next= new Ellipse2D.Double(450,350,40,40);
    CutScenePanelListener(CutScenePanel p){
        panel = p;
    }
    public void mouseClicked(MouseEvent e) {
        if(next.contains(e.getPoint())){
            panel.nextScene();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}