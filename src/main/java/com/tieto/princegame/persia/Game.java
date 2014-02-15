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

    private EDirection direction = BKW;

    private GameMap gameMap = new GameMap();

    private GameHistory history = new GameHistory();

    private int princePos = 0;

    private int stepNumber = 0;

    private boolean retreat;

    private boolean allowJumping = true;

    private Action action;

    private StepStrategy stepStrategy;

    private Prince prince;

    public Game() {
    }

    public EDirection getStepDirection() {
        return direction;
    }

    public void setStepDirection(final EDirection direction) {
        this.direction = direction;
    }

    public GameHistory getHistory() {
        return history;
    }

    public void resetLevelMap() {
        gameMap.reset();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getPrincePos() {
        return princePos;
    }

    public void setPrincePos(final int princePos) {
        this.princePos = princePos;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepAction(final Action action) {
        this.action = action;
        Log.info("- step action: " + action.getClass().getSimpleName());
    }

    public Action getStepAction() {
        return action;
    }

    public boolean isRetreat() {
        return retreat;
    }

    public void setRetreat(final boolean retreat) {
        this.retreat = retreat;
    }

    public void setAllowJumping(final boolean allowJumping) {
        this.allowJumping = allowJumping;
    }

    public boolean isJumpingAllowed() {
        return allowJumping;
    }

    public Prince getPrince() {
        return prince;
    }

    public StepStrategy newStep(final Prince prince, final List<ActionStrategy> strategies) {
        if (stepStrategy != null) {
            final StepStrategy lastStrategy = stepStrategy.clone();
            getHistory().add(lastStrategy);
            Utils.updatePrincePossition(this, lastStrategy.getGame().getStepAction());
        }

        this.prince = prince;
        stepNumber++;
        action = null;

        Log.info("---------");
        Log.info("STEP " + stepNumber);
        Log.info("---------");
        Log.fine("- possition: " + princePos);
        Log.info("- health: " + prince.getHealth());

        stepStrategy = new StepStrategy(this, strategies);

        updateGameMap(prince);

        return stepStrategy;
    }

    private void updateGameMap(final Prince prince) {
        gameMap.update(getPrincePos(), prince);
        final StepStrategy lastStrategy = getHistory().lastElement();
        if (lastStrategy != null) {
            int expectedHealth = lastStrategy.getPrince().getHealth();
            if (Utils.isHeal(lastStrategy.getGame().getStepAction()) && lastStrategy.getPrince().getHealth() < lastStrategy.getPrince().getMaxHealth()) {
                expectedHealth++;
            }
            final int damage = expectedHealth - prince.getHealth();
            gameMap.updateDamage(getPrincePos(), damage);
            Log.fine("- damage: " + damage);
        }
    }

    @Override
    protected Object clone() {
        return clone(stepStrategy);
    }

    public Game clone(final StepStrategy stepStrategy) {
        try {
            final Game clone = (Game) super.clone();
            clone.history = new GameHistory(history);
            clone.gameMap = gameMap.clone();
            clone.stepStrategy = stepStrategy;
            // clone.prince = prince.clone();
            return clone;
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}