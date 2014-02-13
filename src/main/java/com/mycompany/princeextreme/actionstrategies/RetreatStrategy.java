package com.mycompany.princeextreme.actionstrategies;

import java.util.logging.Logger;

import com.mycompany.princeextreme.EDirection;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class RetreatStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(RetreatStrategy.class.getName());

    private static final double RETREAT_HEAL_PCT = 0.333;

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {

        if (Utils.isSafeToHealHere(turnStrategy)) {
            Log.fine("-- it should be safe to heal here");

            if (turnStrategy.getGame().isRetreat()) {
                stopRetreat(turnStrategy);
            }

            return turnStrategy.evaluateNext();
        } else {
            Log.fine("-- it is not safe to heal here");
            EDirection enemyDirection = Utils.getAttackingEnemyDirection(turnStrategy);
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
                        enemyDirection = turnStrategy.getStepDirection();
                    }
                }
                beginRetreat(turnStrategy, enemyDirection.opposite());
                Action bestRetreatAction = Utils.getBestRetreatAction(turnStrategy, enemyDirection.opposite());
                if (Utils.isHeal(bestRetreatAction)) {
                    Log.fine("-- not good, we can only heal!");
                    return turnStrategy.evaluateNext();
                }
                return bestRetreatAction;
            }

            return turnStrategy.evaluateNext();
        }
    }

    private void beginRetreat(TurnStrategy turnStrategy, EDirection retreatDirection) {
        Log.fine("-- retreat, direction: " + retreatDirection);
        turnStrategy.getGame().setRetreat(true);
        turnStrategy.setStepDirection(retreatDirection);
    }

    private void stopRetreat(TurnStrategy turnStrategy) {
        Log.fine("-- ending retreat");
        turnStrategy.getGame().setRetreat(false);
    }

    public boolean shouldRetreat(Prince prince, TurnStrategy turnStrategy, EDirection retreatDirection) {
        Integer damage = turnStrategy.getGame().getLevelMap().getDamageAt(turnStrategy.getGame().getPrincePos());
        if (damage != null && retreatDirection != null) {
            TurnStrategy bestRetreatResult = Utils.getBestRetreatResult(turnStrategy, retreatDirection);
            int retreatPos = Utils.getRetreatPossition(bestRetreatResult, bestRetreatResult.getGame().getPrincePos());
            if (turnStrategy.getGame().getPrincePos() != retreatPos) {
                Integer retreatDamage = bestRetreatResult.getGame().getLevelMap().getDamageAt(retreatPos);
                if (retreatDamage != null) {
                    return prince.getHealth() <= damage + retreatDamage;
                }
            }
        }

        return (damage != null && prince.getHealth() <= damage) || prince.getHealth() < Math.ceil(prince.getMaxHealth() * RETREAT_HEAL_PCT);
    }
}