package com.mycompany.princeextreme;

import static com.mycompany.princeextreme.EDirection.*;

import java.util.List;
import java.util.logging.Logger;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.gameobject.Prince;

public class Game implements Cloneable {

    private static final Logger Log = Logger.getLogger(Game.class.getName());

    public static final int MIN_HEALTH = 5;

    private EDirection direction = BKW;

    private LevelMap levelMap = new LevelMap();

    private GameHistory history = new GameHistory();

    private int playerPos = 0;

    private int stepNumber = 0;

    private boolean retreat;

    private boolean turnBack;

    private boolean allowJumping;

    public Game() {
        initFlags();
    }

    private void initFlags() {
        retreat = false;
        turnBack = false;
        allowJumping = true;
    }

    public EDirection getDirection() {
        return this.direction;
    }

    public void setStepDirection(EDirection direction) {
        this.direction = direction;
    }

    public GameHistory getHistory() {
        return history;
    }

    public void resetLevelMap() {
        levelMap.reset();
    }

    public LevelMap getLevelMap() {
        return levelMap;
    }

    public int getPricePos() {
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
        levelMap.updateLevelMap(getPricePos(), prince);
        updateFieldDamage(prince);
    }

    private void updateFieldDamage(Prince prince) {
        TurnStrategy lastStrategy = getHistory().lastElement();
        if (lastStrategy != null) {
            int expectedHealth = lastStrategy.getPrince().getHealth();
            if (lastStrategy.getAction() instanceof Heal && lastStrategy.getPrince().getHealth() < lastStrategy.getPrince().getMaxHealth()) {
                expectedHealth++;
            }
            int damage = expectedHealth - prince.getHealth();
            levelMap.updateFieldDamage(getPricePos(), damage);
            Log.fine("-- damage : " + damage);
        }
    }

    @Override
    protected Object clone() {
        return clone(false);
    }

    public Game clone(boolean clearFlags) {
        try {
            Game clone = (Game) super.clone();
            clone.history = new GameHistory(history);
            clone.levelMap = levelMap.clone();
            if (clearFlags) {
                clone.initFlags();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}