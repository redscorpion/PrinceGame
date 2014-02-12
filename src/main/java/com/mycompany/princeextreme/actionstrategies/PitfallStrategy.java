/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.mycompany.princeextreme.actionstrategies;

import com.mycompany.princeextreme.EObstacle;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class PitfallStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next != null && EObstacle.PITFALL.equalsTo(next.getObstacle())) {
            return turnStrategy.jump(true);
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }
}