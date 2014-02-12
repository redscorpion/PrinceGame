package landscape;

import cz.tieto.princegame.common.gameobject.Obstacle;

public class Dragon implements Obstacle {

    private int health;

    public Dragon(int health) {
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
    public void setHealth(int i) {
        this.health = i;
    }
}
