package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class HealStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(HealStrategy.class.getName());

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        if (Utils.isSafeToHealHere(turnStrategy)) {
            if (prince.getHealth() < prince.getMaxHealth()) {
                return doHeal(prince, turnStrategy);
            }
        } else if (turnStrategy.getGame().isRetreat()) {
            return doBattleHeal(prince, turnStrategy);
        }

        return turnStrategy.evaluateNext();
    }

    private Action doHeal(Prince prince, TurnStrategy turnStrategy) {
        Log.fine("-- heal");

        return turnStrategy.heal();
    }

    private Action doBattleHeal(Prince prince, TurnStrategy turnStrategy) {
        Log.fine("-- emergency battle heal");

        return turnStrategy.heal();
    }

}