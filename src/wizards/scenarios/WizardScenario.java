package wizards.scenarios;

import wizards.WizardApplication;
import wizards.maps.DefaultMapOne;
import wizards.maps.WizardMap;
import wizards.actors.*;
import wizards.controllers.WizardController;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class if for running a scenerio, it will get get 'updates' from the Engine
 * it should use time and existing characters to decide to continue.
 *
 * User: mbs207
 * Date: 4/10/11
 * Time: 3:33 PM
 */
public abstract class WizardScenario {
    abstract public void init(WizardApplication app, WizardController player_controls);
    abstract public void startScenario();
    abstract public void endScenario();
    abstract public void update(ArrayList<Wizard> wizards, ArrayList<Spell> spells, ArrayList<Creature> creatures);
    abstract public boolean keepRunning();
    abstract public WizardMap getMap();
    static public WizardScenario getDefaultScenario(){
        DefaultScenario ds = new DefaultScenario();
        return ds;
    }
}

