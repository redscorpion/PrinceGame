package com.mycompany.princeextreme.actionstrategies;

import java.util.List;

import com.mycompany.princeextreme.GameStrategy;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.gameobject.Prince;

public class HealStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {

        if (turnStrategy.doNotHeal) {
            return turnStrategy.invokeNext(prince, turnStrategy);
        }

        if (Utils.isSafeToHeal(turnStrategy)) {
            // we can re-heal
            int minAttackHeal = Math.min(prince.getMaxHealth(), Math.max(turnStrategy.getGameStrategy().getNeededHealth(), GameStrategy.MIN_ATTACK_HEALTH));
            int minHealth = Math.min(GameStrategy.MIN_WALKING_HEALTH, prince.getMaxHealth());

            int healTo = Math.min(Math.max(minAttackHeal, minHealth), prince.getMaxHealth());

            if (prince.getHealth() < healTo) {
                return new Heal();
            }
        } else {
            List<TurnStrategy> history = turnStrategy.getGameStrategy().getHistory();
            if (history.size() > 0) {
                TurnStrategy lastStrategy = history.get(history.size() - 1);
                turnStrategy.setStepDirection(lastStrategy.getStepDirection());
            }
        }

        turnStrategy.getGameStrategy().setNeededHealth(GameStrategy.MIN_ATTACK_HEALTH);

        return turnStrategy.invokeNext(prince, turnStrategy);
    }
}