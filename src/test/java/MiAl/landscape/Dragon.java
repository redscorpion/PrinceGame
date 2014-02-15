package MiAl.landscape;

import cz.tieto.princegame.common.gameobject.Obstacle;

public class Dragon implements Obstacle {

    private int health;

    public Dragon(final int health) {
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
        return "dragon";
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
}
