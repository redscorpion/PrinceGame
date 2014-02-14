/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.obstacles;

import persia.simulator.TestGame;
import persia.simulator.TestObstacle;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;

public class Chopper implements Obstacle, TestObstacle {

    private boolean closing = true;
    private boolean opening = false;

    public String getProperty(String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        if ("closing".equals(arg0)) {
            return "" + closing;
        }
        if ("opening".equals(arg0)) {
            return "" + opening;
        }
        return null;
    }

    public String getName() {
        return "chopper";
    }

    public int getId() {
        return System.identityHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    public boolean walkTo(TestGame game) {
        game.doDamageToPrince(getName(), "walk to", 2);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean jumpOver(TestGame game) {
        if (!opening || closing) {
            game.doDamageToPrince(getName(), "jump over", 2);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void nextTurn(TestGame game) {
        closing = !closing;
        opening = !opening;
    }

    /**
     * {@inheritDoc}
     */
    public void useEquipment(TestGame game, Equipment equipment) {
    }
}