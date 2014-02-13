package com.mycompany.princeextreme.actionstrategies;

import java.util.logging.Logger;

import com.mycompany.princeextreme.Game;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.EDirection;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class HealStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(HealStrategy.class.getName());

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {

        if (Utils.isSafeToHealHere(turnStrategy)) {
            Log.fine("-- it should be safe to heal here");
            if (prince.getHealth() < prince.getMaxHealth()) {
                return doHeal(prince, turnStrategy);
            }
        } else {
            Log.fine("-- it is not safe to heal here");
            EDirection enemyDirection = Utils.geEnemyDirection(turnStrategy);
            if (enemyDirection == null) {
                Log.fine("-- and i don't see the enemy");
            }
            if (shouldRetreat(prince, turnStrategy, enemyDirection != null ? enemyDirection.opposite() : null)) {
                Log.fine("-- low health");
                if (enemyDirection == null) {
                    TurnStrategy lastStrategy = turnStrategy.getGame().getHistory().lastElement();
                    if (lastStrategy != null) {
                        if (lastStrategy.getGame().isRetreat()) {
                            Log.fine("-- continue in retreat");
                            enemyDirection = lastStrategy.getStepDirection().opposite();
                        } else {
                            Log.fine("-- turn back");
                            enemyDirection = lastStrategy.getStepDirection();
                        }
                    } else {
                        Log.fine("-- i'm totally lost, turn back");
                        enemyDirection = turnStrategy.getStepDirection().opposite();
                    }
                }
                beginRetreat(turnStrategy, enemyDirection.opposite());
                return Utils.getBestRetreatAction(turnStrategy, enemyDirection.opposite());
            }
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }

    private Action doHeal(Prince prince, TurnStrategy turnStrategy) {
        Log.fine("-- heal");

        if (turnStrategy.getGame().isRetreat()) {
            stopRetreat(turnStrategy);
        }

        return turnStrategy.heal();
    }

    private void beginRetreat(TurnStrategy turnStrategy, EDirection retreatDirection) {
        Log.fine(" -- retreat, direction: " + retreatDirection);
        turnStrategy.getGame().setRetreat(true);
        turnStrategy.setStepDirection(retreatDirection);
    }

    private void stopRetreat(TurnStrategy turnStrategy) {
        Log.fine(" -- ending retreat");
        turnStrategy.getGame().setRetreat(false);
    }

    public boolean shouldRetreat(Prince prince, TurnStrategy turnStrategy, EDirection retreatDirection) {
        if (retreatDirection != null) {
            Integer damage = turnStrategy.getGame().getLevelMap().getDamageAt(turnStrategy.getGame().getPricePos());
            if (damage != null) {
                Integer retreatDamage = Utils.getSmallestRetreatDamage(turnStrategy, retreatDirection);
                if (retreatDamage != null) {
                    return prince.getHealth() <= damage + retreatDamage;
                }
            }
        }

        return prince.getHealth() < Math.min(prince.getMaxHealth() / 2, Game.MIN_HEALTH);
    }


}