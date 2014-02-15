package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class HealStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(HealStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        if (Utils.isSafeToHealHere(stepStrategy)) {
            if (prince.getHealth() < prince.getMaxHealth()) {
                return doHeal(prince, stepStrategy);
            }
        } else if (stepStrategy.getGame().isRetreat()) {
            return doBattleHeal(prince, stepStrategy);
        }

        return stepStrategy.evaluateNext();
    }

    private Action doHeal(final Prince prince, final StepStrategy stepStrategy) {
        Log.fine("-- heal");

        return stepStrategy.heal();
    }

    private Action doBattleHeal(final Prince prince, final StepStrategy stepStrategy) {
        Log.fine("-- emergency battle heal");

        return stepStrategy.heal();
    }

}