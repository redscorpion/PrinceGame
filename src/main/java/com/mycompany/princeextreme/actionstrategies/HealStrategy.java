package com.mycompany.princeextreme.actionstrategies;

import com.mycompany.princeextreme.GameStrategy;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.gameobject.Prince;

public class HealStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        int minHealth = Math.min(prince.getMaxHealth(), Math.max(turnStrategy.getGameStrategy().getNeededHealth(), GameStrategy.MIN_ATTACK_HEALTH));

        if (prince.getHealth() < minHealth) {
            return new Heal();
        }

        if (Utils.isSafeToHeal(turnStrategy)) {
            // we can re-heal
            int healTo = Math.min(GameStrategy.MIN_WALKING_HEALTH, prince.getMaxHealth());
            if (prince.getHealth() < healTo) {
                return new Heal();
            }
        }

        turnStrategy.getGameStrategy().setNeededHealth(GameStrategy.MIN_ATTACK_HEALTH);

        return turnStrategy.invokeNext(prince, turnStrategy);
    }
}