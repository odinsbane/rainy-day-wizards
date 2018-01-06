package wizards;

import wizards.actions.*;
import wizards.actors.Actor;
import wizards.actors.Creature;
import wizards.actors.Spell;
import wizards.actors.Wizard;
import wizards.maps.WizardMap;
import wizards.scenarios.WizardScenario;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *  This is the game engine.
 */
class WizardEngine extends Thread{
    static final int BIDING = 0;
    static final int PLAYING = 1;
    ArrayList<Wizard> wizards = new ArrayList<Wizard>();
    ArrayList<Spell> spells = new ArrayList<Spell>();
    ArrayList<Creature> creatures = new ArrayList<Creature>();

    ConcurrentLinkedQueue<Runnable> actions = new ConcurrentLinkedQueue<Runnable>();
    WizardScenario scenario;
    boolean running = false;
    boolean CLEAR=false;
    int state = 0;

    public void run(){

        engine_loop: for(;;){


            switch(state){
                case BIDING:
                    try{
                        waitFor();
                    }catch(Exception e){
                        break engine_loop;
                    }
                    break;
                case PLAYING:
                    runScenario();
                    break;


            }


        }



    }

    synchronized public void waitFor() throws InterruptedException{
        wait();
    }

    synchronized public void startScenario(WizardScenario ws){
        state = PLAYING;
        scenario = ws;
        notifyAll();
    }
    public void runScenario(){
        long old,diff,now;

        scenario.startScenario();

        wizards.clear();
        spells.clear();
        creatures.clear();
        running=true;
        while(running){
            old = System.currentTimeMillis();
            step();
            interact();
            purge();
            interact();
            scenarioUpdate();
            now = System.currentTimeMillis();
            running = scenario.keepRunning();
            try{
                diff = now - old;
                if(diff<35)
                    Thread.sleep(35 - diff);
            } catch(Exception e){
                running = false;
            }


        }

        scenario.endScenario();
        actions.clear();
        WizardApplication.app.finishedScenario();
        state = BIDING;
    }
    

    void scenarioUpdate(){
        scenario.update(wizards,spells,creatures);
    }

    public void purge(){
        if(CLEAR){
             wizards.clear();
            spells.clear();
            creatures.clear();
            actions.clear();
            CLEAR=false;
        }
        while(actions.size()>0)
            actions.poll().run();
    }

    /**
     * Removes all actors, this action is performed on this thread.
     * If another thread calls this method then
     */
    public void clear(){
        CLEAR=true;
        if(Thread.currentThread()==this){
            purge();
        }
    }

    /**
     * This will go through each actor and give it a chance to move
     * and think.
     */
    public void step(){
        WizardMap map = scenario.getMap();
        Point2D n;
        for(Wizard w: wizards){
            Point2D p = w.getPosition();
            double[] velocity = w.getVelocity();
            if(velocity!=null){
                n = new Point2D.Double(p.getX() + velocity[0], p.getY() + velocity[1]);
                if(! map.obstructed(n,w.getRadius(), w.getMoveType(), w.getImpact())){
                    w.setPosition(n);
                } else{
                    w.obstructed();
                }
            }

            w.update();

        }

        for(Spell s: spells){
            Point2D p = s.getPosition();
            double[] velocity = s.getVelocity();
            if(velocity!=null){
                n = new Point2D.Double(p.getX() + velocity[0], p.getY() + velocity[1]);
                if(! map.obstructed( n,s.getRadius(), s.getMoveType(), s.getImpact())){
                    s.setPosition(n);
                } else{
                    s.obstructed();
                }

            }

            s.update();

        }
        for(Creature c: creatures){
            Point2D p = c.getPosition();
            double[] velocity = c.getVelocity();
            if(velocity!=null){
                n = new Point2D.Double(p.getX() + velocity[0], p.getY() + velocity[1]);
                if(! map.obstructed(n,c.getRadius(),c.getMoveType(), c.getImpact())){
                    c.setPosition(n);
                } else{
                    c.obstructed();
                }
            }

            c.update();
            
        }
    


    }

    /**
     * Collision detection.
     */
    public void interact(){

        Iterator<Wizard> witer = wizards.iterator();
        ArrayList<Actor> checked = new ArrayList<Actor>();
        while(witer.hasNext()){

            Wizard w = witer.next();
            checked.add(w);
            for(Wizard o: wizards){
                if(checked.contains(o))
                    continue;

                if(colliding(w,o)){
                    w.interact(o);
                    o.interact(w);
                }
            }



            for(Spell s: spells){
                if(colliding(w,s)){
                    w.interact(s);
                    s.interact(w);
                }
            }

            for(Creature c: creatures){
                if(colliding(w,c)){
                    w.interact(c);
                    c.interact(w);
                }
            }

        }

        checked.clear();
        Iterator<Spell> siter = spells.iterator();
        while(siter.hasNext()){

            Spell s = siter.next();
            checked.add(s);

            for(Spell o: spells){
                if(checked.contains(o)) continue;

                if(colliding(s,o)){
                    s.interact(o);
                    o.interact(s);
                }
            }

            for(Creature c: creatures){
                if(colliding(s,c)){
                    s.interact(c);
                    c.interact(s);
                }
            }



        }

        Iterator<Creature> citer = creatures.iterator();
        checked.clear();

        while(citer.hasNext()){
            Creature c = citer.next();
            checked.add(c);
            for(Creature o: creatures){
                if(checked.contains(o)) return;

                if(colliding(o,c)){
                   c.interact(o);
                    o.interact(c);
                }
            }

        }



    }

    /**
     * Colliding actors
     * @param s b
     * @param o a
     */
    public boolean colliding(Actor s, Actor o){
        Point2D a = o.getPosition();
        Point2D b = s.getPosition();
        double ra = o.getRadius();
        double rb = s.getRadius();

        double mag = Math.pow(ra+rb,2);

        return a.distanceSq(b)<mag;

    }

    public void add(Wizard w){
        actions.add(new WizardAdder(w,wizards));
    }
    public void remove(Wizard w){
        actions.add(new WizardRemover(w,wizards));
    }

    public void add(Creature c){

        actions.add(new CreatureAdder(c,creatures));

    }

    public void remove(Creature c){
        actions.add(new CreatureRemover(c,creatures));
    }

    public void add(Spell s){
        actions.add(new SpellAdder(s,spells));
    }

    public void remove(Spell s){
       actions.add(new SpellRemover(s,spells));
    }

    /**
     * Finds wizards that are within a cone of view.  Note the cone has to be forward.
     *
     * @param position origin
     * @param direction facing direction, magnitude should be 1.
     * @param angle sin of the angle that represents the cone.
     * @param range maximum distance
     * @return collections of wizards that fall in the above code.
     */
    Set<Wizard> lookForWizards(Point2D position, Point2D direction, double angle, double range){
        Set<Wizard> seen = new HashSet<Wizard>();
        for(Wizard w: wizards){
            Point2D w_pos = w.getPosition();
            double mag = position.distance(w_pos);

            //too far
            if(mag>range) continue;
    
            double vx = w_pos.getX() - position.getX();
            double vy = w_pos.getY() - position.getY();

            double dot = direction.getX()*vx + direction.getY()*vy;

            //behind
            if(dot<0) continue;

            double cross = Math.abs(direction.getX()*vy - direction.getY()*vx)/mag;

            //too large of angle
            if(cross>angle) continue;

            //check if something is blocking the view.
            if(scenario.getMap().obstructed(position, w_pos)) continue;

            //made it this far, you've been seen.
            seen.add(w);
        }

        return seen;
    }

}
