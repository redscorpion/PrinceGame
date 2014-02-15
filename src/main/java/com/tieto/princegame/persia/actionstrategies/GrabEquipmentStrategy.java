package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.domain.EEquipment;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Prince;

public class GrabEquipmentStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(GrabEquipmentStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        final Equipment equipment = prince.look(0).getEquipment();
        if (equipment != null && (EEquipment.isStackable(equipment) || Utils.getEquipment(prince, equipment.getName()) == null)
                && EEquipment.valueOf(equipment) != null) {
            Log.info("-- grab " + equipment.getName());
            return stepStrategy.grab();
        }

        return stepStrategy.evaluateNext();
    }
}