package wizards.actors;

import wizards.maps.Light;

/**
 * New imagej plugin that ...
 * User: mbs207
 * Date: 4/7/11
 * Time: 9:32 AM
 */
public abstract class Spell implements Actor {
    Wizard owner;

    public abstract void interact(Wizard w);
    public abstract void interact(Creature c);
    public abstract void interact(Spell s);
    public abstract Light getLight();

}
