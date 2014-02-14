package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class SimpleMoveStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(SimpleMoveStrategy.class.getName());

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next == null || Utils.isAliveEnemy(next.getObstacle())) {
            // avoid endless cycle
            if (turnStrategy.getGame().isTurnBack()) {
                Log.fine("-- can't move");
                return turnStrategy.heal();
            }
            Log.fine("-- wall or enemy, turn back");
            turnStrategy.getGame().setTurnBack(true);
            turnStrategy.setStepDirection(turnStrategy.getStepDirection().opposite());
            turnStrategy.getGame().setStepDirection(turnStrategy.getStepDirection());
            return turnStrategy.evaluate();
        }

        if (Utils.isSafeToMoveFast(turnStrategy, 2) && turnStrategy.getGame().isJumpingAllowed()) {
            return turnStrategy.jump(false);
        } else {
            return turnStrategy.move();
        }
    }
}