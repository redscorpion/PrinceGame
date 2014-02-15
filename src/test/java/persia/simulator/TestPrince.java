package persia.simulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public final class TestPrince implements Prince, Cloneable {

    private final int id;
    private int health;
    private final int maxHealth;
    private List<Equipment> inventory = new ArrayList<Equipment>();
    private TestGame game;

    public TestPrince(final int health, final int maxHealth) {
        id = System.identityHashCode(this);
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "prince";
    }

    public String getProperty(final String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        if ("health".equals(arg0)) {
            return "" + health;
        } else if ("maxHealth".equals(arg0)) {
            return "" + maxHealth;
        } else {
            return null;
        }
    }

    public int getHealth() {
        return health;
    }

    void setHealth(final int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }

    public Collection<Equipment> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    void addToInventory(final Equipment equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException();
        }
        inventory.add(equipment);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Field look(final int arg0) {
        if (Math.abs(arg0) <= 1) {
            return game.getLookAt(arg0);
        }
        throw new IllegalArgumentException();
    }

    void setGame(final TestGame game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestPrince clone() {
        return clone(game.clone());
    }

    protected TestPrince clone(final TestGame game) {
        try {
            final TestPrince clone = (TestPrince) super.clone();
            clone.inventory = new ArrayList<Equipment>(inventory);
            clone.game = game;
            return clone;
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}