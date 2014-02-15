package com.tieto.princegame.persia;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.domain.EDirection;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Grab;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.action.Use;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class StepStrategy implements Cloneable {

    private static final Logger Log = Logger.getLogger(StepStrategy.class.getName());

    private List<ActionStrategy> strategies;
    private Game game;
    private EDirection stepDirection;
    private ListIterator<ActionStrategy> strategiesChain;
    private boolean turnBack;

    public StepStrategy(final Game game, final List<ActionStrategy> strategies) {
        this.game = game;
        stepDirection = game.getStepDirection();
        this.strategies = strategies;
        strategiesChain = strategies.listIterator();
    }

    public Field getField() {
        return game.getPrince().look(0);
    }

    public Prince getPrince() {
        return game.getPrince();
    }

    public EDirection getStepDirection() {
        return stepDirection;
    }

    public void setStepDirection(final EDirection direction) {
        stepDirection = direction;
    }

    public Game getGame() {
        return game;
    }

    public Field getNextStepField(final Prince prince) {
        Field next;
        switch (stepDirection) {
        case FWD:
            next = prince.look(1);
            break;
        case BKW:
            next = prince.look(-1);
            break;
        default:
            throw new IllegalStateException();
        }
        return next;
    }

    public boolean isTurnBack() {
        return turnBack;
    }

    public void setTurnBack(final boolean turnBack) {
        this.turnBack = turnBack;
    }

    public Action evaluate() {
        strategiesChain = strategies.listIterator();
        return evaluateNext();
    }

    public Action evaluateNext() {
        if (strategiesChain.hasNext()) {
            final ActionStrategy next = strategiesChain.next();
            return next.getAction(game.getPrince(), this);
        }
        return null;
    }

    /* Actions */

    public Action move() {
        switch (stepDirection) {
        case FWD:
            return new MoveForward();
        case BKW:
            return new MoveBackward();
        default:
            throw new IllegalStateException();
        }
    }

    public Action jump(final boolean shouldBeSafe) {
        switch (stepDirection) {
        case FWD:
            if (!shouldBeSafe && getGame().getGameMap().getGameField(getGame().getPrincePos()) == null) {
                getGame().resetLevelMap();
            }
            return new JumpForward();
        case BKW:
            if (!shouldBeSafe && getGame().getGameMap().getGameField(getGame().getPrincePos()) == null) {
                getGame().resetLevelMap();
            }
            return new JumpBackward();
        default:
            throw new IllegalStateException();
        }
    }

    public Action grab() {
        return new Grab();
    }

    public Action heal() {
        return new Heal();
    }

    public Action use(final Equipment eq, final Obstacle obstacle) {
        Log.fine("-- use " + eq.getName() + " on " + obstacle.getName());
        return new Use(eq, obstacle);
    }

    public List<ActionStrategy> getStrategies() {
        return strategies;
    }

    public void switchStepDirection() {
        setStepDirection(getStepDirection().opposite());
    }

    @Override
    public StepStrategy clone() {
        return clone(strategies, strategiesChain);
    }

    public StepStrategy clone(final List<ActionStrategy> strategies) {
        return clone(strategies, strategies.listIterator());
    }

    private StepStrategy clone(final List<ActionStrategy> strategies, final ListIterator<ActionStrategy> strategiesChain) {
        try {
            final StepStrategy clone = (StepStrategy) super.clone();
            clone.game = game.clone(clone);
            clone.strategies = new ArrayList<PersiaStrategy.ActionStrategy>(strategies);
            clone.strategiesChain = clone.strategies.listIterator();
            while (clone.strategiesChain.nextIndex() < strategiesChain.nextIndex()) {
                clone.strategiesChain.next();
            }
            return clone;
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}