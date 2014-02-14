/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.simulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

/**
 * TestPrince
 * 
 * @author <a href="mailto:kamil.siroky@teliasonera.com">Kamil Siroky</a>
 * 
 */
public final class TestPrince implements Prince, Cloneable {

    private int health;
    private int maxHealth;
    private List<Equipment> inventory = new ArrayList<Equipment>();
    private TestGame game;

    public TestPrince(int health, int maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public int getId() {
        return System.identityHashCode(this);
    }

    public String getName() {
        return "prince";
    }

    public String getProperty(String arg0) {
        throw new UnsupportedOperationException();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }

    public Collection<Equipment> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public void addToInventory(Equipment equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException();
        }
        inventory.add(equipment);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Field look(int arg0) {
        if (Math.abs(arg0) <= 1) {
            return game.getLookAt(arg0);
        }
        throw new IllegalArgumentException();
    }

    void setGame(TestGame game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return clone(game.clone());
    }

    protected TestPrince clone(TestGame game) {
        try {
            TestPrince clone = (TestPrince) super.clone();
            clone.inventory = new ArrayList<Equipment>(inventory);
            clone.game = game;
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}