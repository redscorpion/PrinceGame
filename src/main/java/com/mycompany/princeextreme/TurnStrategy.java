package com.mycompany.princeextreme;

import java.util.Iterator;
import java.util.List;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;

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

public class TurnStrategy {
    private final Prince prince;
    private final Field field;
    private final List<ActionStrategy> strategies;
    private final GameStrategy gameStrategy;

    private EDirection stepDirection;
    private Iterator<ActionStrategy> currentStrategy;
    private Action action;

    public TurnStrategy(Prince prince, GameStrategy gameStrategy, List<ActionStrategy> strategies) {
        this.prince = prince;
        this.gameStrategy = gameStrategy;
        this.stepDirection = gameStrategy.getDirection();
        this.strategies = strategies;
        this.currentStrategy = strategies.iterator();
        this.field = prince.look(0);
    }

    public Field getField() {
        return field;
    }

    public Prince getPrince() {
        return prince;
    }

    public EDirection getStepDirection() {
        return this.stepDirection;
    }

    public void setStepDirection(EDirection direction) {
        this.stepDirection = direction;
    }

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }

    public Field getNextStepField(Prince prince) {
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

    public Action invokeFirst(Prince prince, TurnStrategy context) {
        currentStrategy = strategies.iterator();
        return invokeNext(prince, context);
    }

    public Action invokeNext(Prince prince, TurnStrategy context) {
        if (currentStrategy.hasNext()) {
            ActionStrategy next = currentStrategy.next();
            return next.getAction(prince, this);
        }
        return null;
    }

    /* Actions */

    public Action move() {
        switch (stepDirection) {
        case FWD:
            getGameStrategy().setPlayerPos(getGameStrategy().getPlayerPos() + 1);
            return new MoveForward();
        case BKW:
            getGameStrategy().setPlayerPos(getGameStrategy().getPlayerPos() - 1);
            return new MoveBackward();
        default:
            throw new IllegalStateException();
        }
    }

    public Action jump(boolean shouldBeSafe) {
        switch (stepDirection) {
        case FWD:
            getGameStrategy().setPlayerPos(getGameStrategy().getPlayerPos() + 2);
            if (!shouldBeSafe && getGameStrategy().getLevelMap().getMapField(getGameStrategy().getPlayerPos()) == null) {
                getGameStrategy().resetLevelMap();
            }
            return new JumpForward();
        case BKW:
            getGameStrategy().setPlayerPos(getGameStrategy().getPlayerPos() - 2);
            if (!shouldBeSafe && getGameStrategy().getLevelMap().getMapField(getGameStrategy().getPlayerPos()) == null) {
                getGameStrategy().resetLevelMap();
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

    public Action use(Equipment eq, Obstacle obstacle) {
        return new Use(eq, obstacle);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

}