package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.domain.EEquipment;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class ThornbushStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(ThornbushStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        final Field next = stepStrategy.getNextStepField(prince);

        if (next != null && EObstacle.THORNBUSH.equalsTo(next.getObstacle())) {
            Log.fine("-- thornbush");
            final boolean burnt = Boolean.parseBoolean(next.getObstacle().getProperty("burnt"));
            if (!burnt) {
                Log.fine("-- not burnt");
                final Equipment matches = Utils.getEquipment(prince, EEquipment.MATCHES);
                if (matches != null) {
                    return stepStrategy.use(matches, next.getObstacle());
                } else {
                    Log.fine("-- switch direction and find matches");
                    stepStrategy.switchStepDirection();
                    stepStrategy.getGame().setStepDirection(stepStrategy.getStepDirection());
                    return stepStrategy.evaluateNext();
                }
            } else {
                Log.fine("-- burnt");
            }
        }

        return stepStrategy.evaluateNext();
    }
}