package com.mycompany.princeextreme;

import static com.mycompany.princeextreme.EDirection.*;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.gameobject.Prince;

public class GameStrategy implements Cloneable {

    public static final int MIN_HEALTH = 5;

    private EDirection direction = FWD;

    private LevelMap levelMap = new LevelMap();

    private List<TurnStrategy> history = new ArrayList<TurnStrategy>();

    private int playerPos;

    private int stepNumber;

    private boolean retreat;

    public EDirection getDirection() {
        return this.direction;
    }

    public void setDirection(EDirection direction) {
        this.direction = direction;
    }

    public List<TurnStrategy> getHistory() {
        return history;
    }

    public void resetLevelMap() {
        levelMap.reset();
    }

    public LevelMap getLevelMap() {
        return levelMap;
    }

    public int getPlayerPos() {
        return playerPos;
    }

    public void setPlayerPos(int playerPos) {
        this.playerPos = playerPos;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public TurnStrategy newStep(Prince prince, int stepNumber, List<ActionStrategy> strategies) {
        TurnStrategy turnStrategy = new TurnStrategy(prince, this, strategies);
        this.stepNumber = stepNumber;
        updateLevelMap(prince);
        return turnStrategy;
    }

    private void updateLevelMap(Prince prince) {
        levelMap.updateLevelMap(getPlayerPos(), prince);
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

    /**
     * @return the retreat
     */
    public boolean isRetreat() {
        return retreat;
    }

    /**
     * @param retreat the retreat to set
     */
    public void setRetreat(boolean retreat) {
        this.retreat = retreat;
    }

}