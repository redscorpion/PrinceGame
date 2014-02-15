package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class PitfallStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(PitfallStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        final Field next = stepStrategy.getNextStepField(prince);

        if (next != null && EObstacle.PITFALL.equalsTo(next.getObstacle())) {
            Log.fine("-- pitfall");
            return stepStrategy.jump(true);
        }

        return stepStrategy.evaluateNext();
    }
}