/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.mycompany.princeextreme.actionstrategies;

import static com.mycompany.princeextreme.EDirection.*;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class SimpleMoveStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next == null) {
            turnStrategy.setStepDirection(turnStrategy.getStepDirection() == FWD ? BKW : FWD);
            turnStrategy.getGameStrategy().setDirection(turnStrategy.getStepDirection());
            return turnStrategy.invokeFirst(prince, turnStrategy);
        }

        if (Utils.isSafeToMoveFast(turnStrategy, 2)) {
            return turnStrategy.jump(false);
        } else {
            return turnStrategy.move();
        }
    }
}