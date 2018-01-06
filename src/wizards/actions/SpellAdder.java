package wizards.actions;

import wizards.actors.Spell;

import java.util.ArrayList;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/8/11
 * Time: 9:13 AM
 */
public class SpellAdder implements Runnable{
    ArrayList<Spell> l;
    Spell a;
    public SpellAdder(Spell actor, ArrayList<Spell> list){
        a = actor;
        l=list;
    }
    public void run(){
        l.add(a);
    }
}

