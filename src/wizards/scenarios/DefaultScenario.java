package wizards.scenarios;

import wizards.WizardApplication;
import wizards.actors.*;
import wizards.controllers.WizardController;
import wizards.maps.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

class DefaultScenario extends WizardScenario {
    long time;
    WizardApplication app;
    volatile boolean running = false;
    Wizard player;
    Creature[] caves;
    WizardMap map;
    boolean WIN = false;
    WizardController controls;

    int state;

    Beacon[] beacons;


    public void init(WizardApplication app, WizardController player_control){
        this.app = app;
        map = new LoadedMap("default_map_one.txt");
        running = true;
        player = new Wizard();
        player.setPosition(new Point2D.Double(100,100));
        player_control.setWizard(player);
        player_control.setMap(map);
        controls = player_control;
        state=1;
    }

    public void initTwo(){

        state=2;
        map = new LoadedMap("default_map_two.txt");
        controls.setMap(map);
        app.setMap(map);
        app.clearActors();
        app.addWizard(player);
        player.setPosition(new Point2D.Double(150,1950));

    }

    public void initThree(){
        state=3;
        map = new DefaultMapThree();
        controls.setMap(map);
        app.setMap(map);
        app.clearActors();
        app.addWizard(player);
        player.setPosition(new Point2D.Double(280,330));
        beacons = new Beacon[6];
        
        beacons[0] = new Beacon(225,-375);
        beacons[1] = new Beacon(425,-375);
        beacons[2] = new Beacon(125,-250);
        beacons[3] = new Beacon(525,-250);
        beacons[4] = new Beacon(225,-50);
        beacons[5] = new Beacon(425,-50);

        for(Beacon b: beacons){
            map.addObstacle(b);
        }

    }



    public void startScenario(){
        
        app.addWizard(player);
        app.addCutSceneText("You are a wizard with mighty fireballs");
        app.showCutScene();

        caves = new Creature[3];
        Creature cave = new Cavern();
        app.addCreature(cave);
        cave.setPosition(new Point2D.Double(-445,-311));
        caves[0] = cave;
        cave = new Cavern();
        app.addCreature(cave);
        cave.setPosition(new Point2D.Double(316,-510));
        caves[1] = cave;

        cave = new Cavern();
        app.addCreature(cave);
        cave.setPosition(new Point2D.Double(685,-276));
        caves[2] = cave;

        //app.addCutSceneText("A normally passive Lizard has been troubling a town.");
        //app.addCutSceneText("While the lizard is dangerous, it is not evil.");
        app.addCutSceneText("There are three caves where the creatures come out of.");
        app.addCutSceneText("Destroy these three caves, but try not to burn down trees and kill creatures");
        app.showCutScene();



        //top center.
        time = System.currentTimeMillis();
        controls.setEnabled(true);


    }

     public void startTwo(){
         
         app.clearCutSceneMessages();
         app.addCutSceneText("Well Done");
         app.addCutSceneText("It is odd that the creatures were arriving in such large numbers");
         app.addCutSceneText("Check their place of origin to see why.");

         

         app.showCutScene();
         Creature c = new Creature();
         c.setPosition(new Point2D.Double(300,50));
         Creature cave = new Cavern();
         app.addCreature(cave);
         cave.setPosition(new Point2D.Double(300,20));

        app.addCreature(c);



    }

    public void startThree(){
        app.clearCutSceneMessages();
        app.addCutSceneText("The cave entrance here is larger than the previous cave entrances");
        app.addCutSceneText("Light the beacons for light.");
        app.showCutScene();



    }

    @Override
    public void endScenario() {
        controls.setEnabled(false);
        app.clearCutSceneMessages();
        if(WIN){
            app.addCutSceneText("Well Done");
            app.showCutScene();
        } else{
            app.addCutSceneText("You died a miserable death.");
            app.showCutScene();
        }
    }

    public void update(ArrayList<Wizard> wizards, ArrayList<Spell> spells, ArrayList<Creature> creatures){
        map.updatePlayer(player);
        if(state==1){
            updateOne(wizards,spells,creatures);
        } else if(state==2){
            updateTwo(wizards,spells,creatures);
        } else{
            updateThree(wizards,spells,creatures);
        }
    }

    public void updateThree(ArrayList<Wizard> wizards, ArrayList<Spell> spells, ArrayList<Creature> creatures){
        for(Beacon b: beacons){
            if(!b.LIT){
                return;
            }
        }

        WIN = true;
        running = false;

    }

    public void updateOne(ArrayList<Wizard> wizards, ArrayList<Spell> spells, ArrayList<Creature> creatures){
        long now = System.currentTimeMillis();
        if(now-time<1000){
            return;
        }


        time = now;

        //lose condition
        running = player.health>0;



        if(!running){
            //the player died.
            WIN=false;
            return;
        }

        boolean finished = true;
        //win condition
        for(Creature c: caves){
            if(c.health>0){
                finished=false;
                break;
            }
        }

        if(finished){

            initTwo();
            startTwo();

        }


    }

    public void updateTwo(ArrayList<Wizard> wizards, ArrayList<Spell> spells, ArrayList<Creature> creatures){
        
        if(creatures.size()==0){
            //WIN=true;
            //running=false;

            initThree();
            startThree();
            
        }
        if(player.health<0){
            WIN=false;
            running=false;
        }
    }



    public boolean keepRunning(){

        return running;

    }

    public WizardMap getMap(){
        return map;
    }


}