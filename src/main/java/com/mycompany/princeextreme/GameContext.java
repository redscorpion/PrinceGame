package com.mycompany.princeextreme;

import static com.mycompany.princeextreme.EDirection.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.gameobject.Prince;

public class GameContext implements Cloneable {

    private static final Logger Log = Logger.getLogger(GameContext.class.getName());

    public static final int MIN_HEALTH = 5;

    private EDirection direction = BKW;

    private LevelMap levelMap = new LevelMap();

    private List<TurnStrategy> history = new ArrayList<TurnStrategy>();

    private int playerPos = 0;

    private int stepNumber = 0;

    private boolean retreat = false;

    private boolean turnBack = false;

    private boolean allowJumping = true;

    private void clearFlags() {
        retreat = false;
        turnBack = false;
        allowJumping = true;
    }

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

    public boolean isRetreat() {
        return retreat;
    }

    public void setRetreat(boolean retreat) {
        this.retreat = retreat;
    }

    public boolean isTurnBack() {
        return turnBack;
    }

    public void setTurnBack(boolean turnBack) {
        this.turnBack = turnBack;
    }

    public void setAllowJumping(boolean allowJumping) {
        this.allowJumping = allowJumping;
    }

    public boolean isJumpingAllowed() {
        return allowJumping;
    }

    public TurnStrategy newStep(Prince prince, int stepNumber, List<ActionStrategy> strategies) {
        TurnStrategy turnStrategy = new TurnStrategy(prince, this, strategies);
        this.stepNumber = stepNumber;
        updateLevelMap(prince);
        return turnStrategy;
    }

    private void updateLevelMap(Prince prince) {
        levelMap.updateLevelMap(getPlayerPos(), prince);
        updateFieldDamage(prince);
    }

    private void updateFieldDamage(Prince prince) {
        if (!getHistory().isEmpty()) {
            TurnStrategy lastStrategy = getHistory().get(getHistory().size() - 1);
            int expectedHealth = lastStrategy.getPrince().getHealth();
            if (lastStrategy.getAction() instanceof Heal && lastStrategy.getPrince().getHealth() < lastStrategy.getPrince().getMaxHealth()) {
                expectedHealth++;
            }
            int damage = expectedHealth - prince.getHealth();
            levelMap.updateFieldDamage(getPlayerPos(), damage);
            Log.fine("-- damage : " + damage);
        }
    }

    @Override
    protected Object clone() {
        return clone(false);
    }

    public GameContext clone(boolean clearFlags) {
        try {
            GameContext clone = (GameContext) super.clone();
            clone.history = new ArrayList<TurnStrategy>(history);
            clone.levelMap = levelMap.clone();
            if (clearFlags) {
                clone.clearFlags();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}