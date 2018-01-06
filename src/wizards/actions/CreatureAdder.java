package wizards.actions;

import wizards.actors.Creature;

import java.util.ArrayList;

public class CreatureAdder implements Runnable{
    ArrayList<Creature> l;
    Creature a;
    public CreatureAdder(Creature actor, ArrayList<Creature> list){
        a = actor;
        l=list;
    }
    public void run(){
        l.add(a);
    }
}
