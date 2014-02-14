/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.obstacles;

import persia.simulator.TestGame;
import persia.simulator.TestObstacle;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;

public class Knight implements Obstacle, TestObstacle {

    private int health;

    public Knight(int health) {
        this.health = health;
    }

    public String getProperty(String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        if ("dead".equals(arg0)) {
            return "" + (health <= 0);
        }
        if ("health".equals(arg0)) {
            return "" + health;
        }
        return null;
    }

    public String getName() {
        return "knight";
    }

    public int getId() {
        return System.identityHashCode(this);
    }

    public int getHealth() {
        return health;
    }

    private void setHealth(int health) {
        this.health = Math.max(0, health);
    }

    private int getDamage(int distance) {
        if (Math.abs(distance) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean walkTo(TestGame game) {
        if (getHealth() <= 0) {
            return true;
        }
        game.doDamageToPrince(getName(), "walk to", 2);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean jumpOver(TestGame game) {
        if (getHealth() <= 0) {
            return true;
        }
        game.doDamageToPrince(getName(), "walk to", 2);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void nextTurn(TestGame game) {
        if (getHealth() > 0) {
            int damage = getDamage(game.getDistanceFromPrince(this));
            if (damage > 0) {
                game.doDamageToPrince(getName(), "prince in attack range", damage);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void useEquipment(TestGame game, Equipment equipment) {
        if (getHealth() > 0 && "sword".equals(equipment.getName())) {
            setHealth(getHealth() - 1);
        }
    }
}