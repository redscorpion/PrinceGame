package com.tieto.princegame.persia;

import static com.tieto.princegame.persia.domain.EDirection.*;

import java.util.List;
import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.domain.EDirection;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class Game implements Cloneable {

    private static final Logger Log = Logger.getLogger(Game.class.getName());

    public static final int MIN_HEALTH = 5;

    private EDirection direction = BKW;

    private LevelMap levelMap = new LevelMap();

    private GameHistory history = new GameHistory();

    private int princePos = 0;

    private int stepNumber = 0;

    private boolean retreat;

    private boolean turnBack;

    private boolean allowJumping;

    private Action action;

    public Game() {
        initialize();
    }

    private void initialize() {
        retreat = false;
        turnBack = false;
        allowJumping = true;
        action = null;
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

    public int getPrincePos() {
        return princePos;
    }

    public void setPrincePos(int princePos) {
        this.princePos = princePos;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
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

    public TurnStrategy newTurnStrategy(Prince prince, int stepNumber, List<ActionStrategy> strategies) {
        TurnStrategy turnStrategy = new TurnStrategy(prince, this, strategies);
        this.stepNumber = stepNumber;
        updateLevelMap(prince);
        return turnStrategy;
    }

    private void updateLevelMap(Prince prince) {
        levelMap.updateLevelMap(getPrincePos(), prince);
        updateFieldDamage(prince);
    }

    private void updateFieldDamage(Prince prince) {
        TurnStrategy lastStrategy = getHistory().lastElement();
        if (lastStrategy != null) {
            int expectedHealth = lastStrategy.getPrince().getHealth();
            if (Utils.isHeal(lastStrategy.getGame().getAction()) && lastStrategy.getPrince().getHealth() < lastStrategy.getPrince().getMaxHealth()) {
                expectedHealth++;
            }
            int damage = expectedHealth - prince.getHealth();
            levelMap.updateFieldDamage(getPrincePos(), damage);
            Log.fine("-- damage : " + damage);
        }
    }

    @Override
    protected Object clone() {
        return clone(false);
    }

    public Game clone(boolean initialize) {
        try {
            Game clone = (Game) super.clone();
            clone.history = new GameHistory(history);
            clone.levelMap = levelMap.clone();
            if (initialize) {
                clone.initialize();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}