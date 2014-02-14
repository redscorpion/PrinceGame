package com.tieto.princegame.persia.actionstrategies;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class ChopperStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next != null && EObstacle.CHOPPER.equalsTo(next.getObstacle())) {
            boolean closing = Boolean.parseBoolean(next.getObstacle().getProperty("closing"));
            boolean opening = Boolean.parseBoolean(next.getObstacle().getProperty("opening"));
            if (closing == false && opening == true) {
                return turnStrategy.jump(true);
            } else {
                return turnStrategy.heal();
            }
        }

        return turnStrategy.evaluateNext();
    }
}