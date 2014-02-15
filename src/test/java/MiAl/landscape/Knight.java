package MiAl.landscape;

import cz.tieto.princegame.common.gameobject.Obstacle;

public class Knight implements Obstacle {

    private int health;

    public Knight(final int health) {
        this.health = health;
    }

    public String getProperty(final String arg0) {
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

    /**
     * @param i
     */
    public void setHealth(final int i) {
        health = i;
    }

    public void fight() {
        health -= 2;
    }
}
