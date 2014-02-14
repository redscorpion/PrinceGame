package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.domain.EEquipment;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class ThornbushStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(ChopperStrategy.class.getName());

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);

        if (next != null && EObstacle.THORNBUSH.equalsTo(next.getObstacle())) {
            boolean burnt = Boolean.parseBoolean(next.getObstacle().getProperty("burnt"));
            if (!burnt) {
                Equipment matches = Utils.getEquipment(prince, EEquipment.MATCHES);
                if (matches != null) {
                    return turnStrategy.use(matches, next.getObstacle());
                } else {
                    Log.fine("-- switch direction and find matches");
                    turnStrategy.switchStepDirection();
                    turnStrategy.getGame().setStepDirection(turnStrategy.getStepDirection());
                    return turnStrategy.evaluateNext();
                }
            }
        }

        return turnStrategy.evaluateNext();
    }
}