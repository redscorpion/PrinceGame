package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class ChopperStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(ChopperStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        final Field next = stepStrategy.getNextStepField(prince);

        if (next != null && EObstacle.CHOPPER.equalsTo(next.getObstacle())) {
            Log.fine("-- chopper");
            final boolean closing = Boolean.parseBoolean(next.getObstacle().getProperty("closing"));
            final boolean opening = Boolean.parseBoolean(next.getObstacle().getProperty("opening"));
            if (!closing && opening) {
                Log.fine("-- is opening, jump");
                return stepStrategy.jump(true);
            } else {
                Log.fine("-- is closed or closing, wait");
                return stepStrategy.heal();
            }
        }

        return stepStrategy.evaluateNext();
    }
}