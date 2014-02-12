package com.mycompany.princeextreme.actionstrategies;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Prince;

public class GrabEquipmentStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        if (turnStrategy.retreat) {
            return turnStrategy.invokeNext(prince, turnStrategy);
        }

        Equipment equipment = prince.look(0).getEquipment();
        if (equipment != null && Utils.getEquipment(prince, equipment.getName()) == null) {
            return turnStrategy.grab();
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }
}