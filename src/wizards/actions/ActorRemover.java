package wizards.actions;

import wizards.actors.Actor;
import wizards.maps.Light;
import wizards.maps.WizardMap;

import java.util.ArrayList;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/8/11
 * Time: 9:46 AM
 */
public class ActorRemover implements Runnable{
    ArrayList<Actor> l;
    Actor a;
    WizardMap m;
    public ActorRemover(Actor actor, ArrayList<Actor> list, WizardMap map){
        a = actor;
        l=list;
        m = map;
    }
    public void run(){
        l.remove(a);
        Light l = a.getLight();
        if(l!=null){
            m.removeLight(l);
        }
    }
}