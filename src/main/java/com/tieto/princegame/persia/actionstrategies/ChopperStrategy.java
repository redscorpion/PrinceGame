package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class ChopperStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(ChopperStrategy.class.getName());

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next != null && EObstacle.CHOPPER.equalsTo(next.getObstacle())) {
            boolean closing = Boolean.parseBoolean(next.getObstacle().getProperty("closing"));
            boolean opening = Boolean.parseBoolean(next.getObstacle().getProperty("opening"));
            if (!closing && opening) {
                Log.fine("-- chopper is opening, jump");
                return turnStrategy.jump(true);
            } else {
                Log.fine("-- chopper is closing, wait");
                return turnStrategy.heal();
            }
        }

        return turnStrategy.evaluateNext();
    }
}