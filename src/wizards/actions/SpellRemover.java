package wizards.actions;

import wizards.actors.Spell;

import java.util.ArrayList;

public class SpellRemover implements Runnable{
    ArrayList<Spell> l;
    Spell a;
    public SpellRemover(Spell actor, ArrayList<Spell> list){
        a = actor;
        l=list;
    }
    public void run(){
        l.remove(a);
    }
}
