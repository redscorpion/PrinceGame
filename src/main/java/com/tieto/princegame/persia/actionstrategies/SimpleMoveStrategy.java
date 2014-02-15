package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class SimpleMoveStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(SimpleMoveStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        final Field next = stepStrategy.getNextStepField(prince);

        boolean canMove = true;

        if (next == null) {
            Log.fine("-- wall, can't move " + stepStrategy.getStepDirection());
            canMove = false;
        } else if (EObstacle.isAliveEnemy(next.getObstacle())) {
            Log.fine("-- enemy " + next.getObstacle().getName() + ", can't move" + stepStrategy.getStepDirection());
            canMove = false;
        } else if (!EObstacle.isDisabled(next.getObstacle())) {
            Log.fine("-- " + next.getObstacle().getName() + ", can't move");
            canMove = false;
        }

        if (!canMove) {
            if (stepStrategy.isTurnBack()) {
                Log.fine("-- can't move in any direction");
                return stepStrategy.heal();
            }
            Log.fine("-- turn back");
            // set turnBack flag to avoid endless cycle
            stepStrategy.setTurnBack(true);
            stepStrategy.switchStepDirection();
            stepStrategy.getGame().setStepDirection(stepStrategy.getStepDirection());
            return stepStrategy.evaluate();
        }

        if (Utils.isSafeToJump(stepStrategy) && stepStrategy.getGame().isJumpingAllowed()) {
            return stepStrategy.jump(false);
        } else {
            return stepStrategy.move();
        }
    }
}