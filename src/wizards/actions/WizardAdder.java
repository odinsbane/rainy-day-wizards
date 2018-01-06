package wizards.actions;

import wizards.actors.Wizard;

import java.util.ArrayList;

public class WizardAdder implements Runnable{
    ArrayList<Wizard> l;
    Wizard a;
    public WizardAdder(Wizard actor, ArrayList<Wizard> list){
        a = actor;
        l=list;
    }
    public void run(){
        l.add(a);
    }
}

