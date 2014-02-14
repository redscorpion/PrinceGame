package com.tieto.princegame.persia.actionstrategies;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.gameobject.Prince;

public class EnterGateStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        if (prince.look(0).isGate()) {
            return new EnterGate();
        }
        return turnStrategy.evaluateNext();
    }
}