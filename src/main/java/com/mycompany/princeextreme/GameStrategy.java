/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.mycompany.princeextreme;

import static com.mycompany.princeextreme.EDirection.*;

import java.util.ArrayList;
import java.util.List;

public class GameStrategy implements Cloneable {

    public static final int MIN_ATTACK_HEALTH = 3;

    public static final int MIN_WALKING_HEALTH = 4;

    private EDirection direction = FWD;

    private int neededHealth = MIN_WALKING_HEALTH;

    private LevelMap levelMap = new LevelMap();

    private List<TurnStrategy> history = new ArrayList<TurnStrategy>();

    public EDirection getDirection() {
        return this.direction;
    }

    public void setDirection(EDirection direction) {
        this.direction = direction;
    }

    public int getNeededHealth() {
        return neededHealth;
    }

    public void setNeededHealth(int neededHealth) {
        this.neededHealth = neededHealth;
    }

    public List<TurnStrategy> getHistory() {
        return history;
    }

    public void resetLevelMap() {
        levelMap.reset();
    }

    public void updateLevelMap(TurnStrategy turnStrategy) {
        levelMap.updateLevelMap(turnStrategy.getPlayerPos(), turnStrategy.getPrince());
    }

    public LevelMap getLevelMap() {
        return levelMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GameStrategy clone() {
        try {
            GameStrategy clone = (GameStrategy) super.clone();
            clone.history = new ArrayList<TurnStrategy>(history);
            clone.levelMap = levelMap.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}