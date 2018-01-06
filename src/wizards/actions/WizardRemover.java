package wizards.actions;

import wizards.actors.Wizard;

import java.util.ArrayList;

public class WizardRemover implements Runnable{
    ArrayList<Wizard> l;
    Wizard a;
    public WizardRemover(Wizard actor, ArrayList<Wizard> list){
        a = actor;
        l=list;
    }
    public void run(){
        l.remove(a);
    }
}
