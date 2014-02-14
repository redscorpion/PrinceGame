/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.obstacles;

import persia.simulator.TestGame;
import persia.simulator.TestObstacle;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;

public class Pitfall implements Obstacle, TestObstacle {

    public String getProperty(String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        return null;
    }

    public String getName() {
        return "pitfall";
    }

    public int getId() {
        return System.identityHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    public boolean walkTo(TestGame game) {
        game.doDamageToPrince(getName(), "walk to", Integer.MAX_VALUE);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean jumpOver(TestGame game) {
        return true;
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
    }

}