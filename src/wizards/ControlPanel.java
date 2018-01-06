package wizards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/17/11
 * Time: 10:59 AM
 */
public class ControlPanel extends JPanel {

    ControlPanel(){
        setBounds(0,0,600,200);
        setOpaque(false);

        Dimension d = new Dimension(200,20);
        JButton start = new JButton("Start");
        start.setPreferredSize(d);
        start.setMaximumSize(d);
        start.setMinimumSize(d);

        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                setExposed(false);
                invalidate();
                WizardApplication.getApplication().startScenario();
            }
        });



        JButton quit = new JButton("Quit");
        quit.setPreferredSize(d);
        quit.setMaximumSize(d);
        quit.setMinimumSize(d);

        quit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                WizardApplication.getApplication().endApplication();
            }
        });

        
        add(start);
        add(quit);

    }

    public void setExposed(boolean expose){
        if(expose)
            setLocation(0, 0);
        else
            setLocation(0,-200);
    }
    
}

class SlidingAnimator extends Thread{
    volatile boolean animated = false;
    public void run(){


    }

}
