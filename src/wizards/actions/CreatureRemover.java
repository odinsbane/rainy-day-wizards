package wizards.actions;

import wizards.actors.Creature;

import java.util.ArrayList;

public class CreatureRemover implements Runnable{
    ArrayList<Creature> l;
    Creature a;
    public CreatureRemover(Creature actor, ArrayList<Creature> list){
        a = actor;
        l=list;
    }
    public void run(){
        l.remove(a);
    }
}

