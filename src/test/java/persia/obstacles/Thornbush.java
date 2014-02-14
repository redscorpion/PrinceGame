/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.obstacles;

import persia.simulator.TestGame;
import persia.simulator.TestObstacle;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;

public class Thornbush implements Obstacle, TestObstacle {

    boolean burnt = false;

    public String getProperty(String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        if ("burnt".equals(arg0)) {
            return "" + burnt;
        }
        return null;
    }

    public String getName() {
        return "thornbush";
    }

    public int getId() {
        return System.identityHashCode(this);
    }

    /**
     * @param b
     */
    public void setBurnt(boolean b) {
        this.burnt = b;
    }

    /**
     * {@inheritDoc}
     */
    public boolean walkTo(TestGame game) {
        if (!burnt) {
            game.doDamageToPrince(getName(), "walk to", 2);
        }
        return burnt;
    }

    /**
     * {@inheritDoc}
     */
    public boolean jumpOver(TestGame game) {
        if (!burnt) {
            game.doDamageToPrince(getName(), "jump over", 2);
        }
        return burnt;
    }

    /**
     * {@inheritDoc}
     */
    public void nextTurn(TestGame game) {
    }

    /**
     * {@inheritDoc}
     */
    public void useEquipment(TestGame game, Equipment equipment) {
        if ("matches".equals(equipment.getName())) {
           setBurnt(true);
       }
    }
}