package com.tieto.princegame.persia.actionstrategies;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class PitfallStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next != null && EObstacle.PITFALL.equalsTo(next.getObstacle())) {
            return turnStrategy.jump(true);
        }

        return turnStrategy.evaluateNext();
    }
}