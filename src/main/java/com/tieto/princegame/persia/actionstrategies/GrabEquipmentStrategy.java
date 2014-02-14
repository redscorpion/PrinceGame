package com.tieto.princegame.persia.actionstrategies;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Prince;

public class GrabEquipmentStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Equipment equipment = prince.look(0).getEquipment();
        if (equipment != null && Utils.getEquipment(prince, equipment.getName()) == null) {
            return turnStrategy.grab();
        }

        return turnStrategy.evaluateNext();
    }
}