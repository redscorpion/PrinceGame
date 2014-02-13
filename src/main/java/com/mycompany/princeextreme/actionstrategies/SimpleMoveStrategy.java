package com.mycompany.princeextreme.actionstrategies;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class SimpleMoveStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next == null || (Utils.isEnemy(next.getObstacle()) && Utils.isAlive(next.getObstacle()))) {
            // avoid endless cycle
            if (turnStrategy.getGame().isTurnBack()) {
                System.out.println("-- can't move");
                return turnStrategy.heal();
            }
            System.out.println("-- wall, turn back");
            turnStrategy.getGame().setTurnBack(true);
            turnStrategy.setStepDirection(turnStrategy.getStepDirection().opposite());
            turnStrategy.getGame().setDirection(turnStrategy.getStepDirection());
            return turnStrategy.invokeFirst(prince, turnStrategy);
        }

        if (Utils.isSafeToMoveFast(turnStrategy, 2) && turnStrategy.getGame().isJumpingAllowed()) {
            return turnStrategy.jump(false);
        } else {
            return turnStrategy.move();
        }
    }
}