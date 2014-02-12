package com.mycompany.princeextreme.actionstrategies;

import java.util.List;

import com.mycompany.princeextreme.EDirection;
import com.mycompany.princeextreme.GameStrategy;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.gameobject.Prince;

public class HealStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {

        if (Utils.isSafeToHealHere(turnStrategy)) {
            System.out.println("-- it should be safe to heal here");
            if (prince.getHealth() < prince.getMaxHealth()) {
                return doHeal(prince, turnStrategy);
            }
        } else {
            System.out.println("-- it is not safe to heal here");
            if (shouldRetreat(prince)) {
                System.out.println("-- low health");
                EDirection enemyDirection = Utils.geEnemyDirection(turnStrategy);
                beginRetreat(turnStrategy, enemyDirection.opposite());
            }
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }

    private Action doHeal(Prince prince, TurnStrategy turnStrategy) {
        System.out.println("-- heal");
        List<TurnStrategy> history = turnStrategy.getGameStrategy().getHistory();
        if (history.size() > 0) {
            TurnStrategy lastStrategy = history.get(history.size() - 1);
            if (lastStrategy.getAction() instanceof Heal) {
                if (prince.getHealth() <= lastStrategy.getPrince().getHealth()) {
                    System.out.println(" -- but something is damaging me");
                    if (lastStrategy.getGameStrategy().isRetreat()) {
                        System.out.println("-- continue retreat");
                        beginRetreat(turnStrategy, lastStrategy.getStepDirection().opposite());
                    } else if (shouldRetreat(prince)) {
                        System.out.println("-- low health");
                        beginRetreat(turnStrategy, lastStrategy.getStepDirection().opposite());
                    }

                    return turnStrategy.invokeNext(prince, turnStrategy);
                }
            }
        }

        if (turnStrategy.getGameStrategy().isRetreat()) {
            stopRetreat(turnStrategy);
        }

        return turnStrategy.heal();
    }

    private void beginRetreat(TurnStrategy turnStrategy, EDirection retreatDirection) {
        System.out.println(" -- retreat, direction: " + retreatDirection);
        turnStrategy.getGameStrategy().setRetreat(true);
        turnStrategy.setStepDirection(retreatDirection);
    }

    private void stopRetreat(TurnStrategy turnStrategy) {
        System.out.println(" -- ending retreat");
        turnStrategy.getGameStrategy().setRetreat(false);
    }

    public boolean shouldRetreat(Prince prince) {
        return prince.getHealth() < Math.min(prince.getMaxHealth() / 2, GameStrategy.MIN_HEALTH);
    }
}