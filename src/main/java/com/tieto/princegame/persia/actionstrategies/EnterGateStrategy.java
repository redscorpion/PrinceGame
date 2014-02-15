package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.gameobject.Prince;

public class EnterGateStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(PitfallStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        if (prince.look(0).isGate()) {
            Log.fine("-- gate");
            return new EnterGate();
        }
        return stepStrategy.evaluateNext();
    }
}