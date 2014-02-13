package com.mycompany.princeextreme.actionstrategies;

import java.util.List;

import com.mycompany.princeextreme.EDirection;
import com.mycompany.princeextreme.GameContext;
import com.mycompany.princeextreme.PersiaStrategy;
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
            EDirection enemyDirection = Utils.geEnemyDirection(turnStrategy);
            if (enemyDirection == null) {
                System.out.println("-- and i don't see the enemy");
            }
            if (shouldRetreat(prince, turnStrategy, enemyDirection != null ? enemyDirection.opposite() : null)) {
                System.out.println("-- low health");
                if (enemyDirection == null) {
                    List<TurnStrategy> history = turnStrategy.getGame().getHistory();
                    if (history.size() > 0) {
                        TurnStrategy lastStrategy = history.get(history.size() - 1);
                        if (lastStrategy.getGame().isRetreat()) {
                            System.out.println("-- continue retreat");
                            enemyDirection = lastStrategy.getStepDirection().opposite();
                        } else {
                            System.out.println("-- turn back");
                            enemyDirection = lastStrategy.getStepDirection();
                        }
                    } else {
                        System.out.println("-- i'm totally lost, turn back");
                        enemyDirection = turnStrategy.getStepDirection().opposite();
                    }
                }
                beginRetreat(turnStrategy, enemyDirection.opposite());
                return getRetreatAction(turnStrategy, enemyDirection.opposite());
            }
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }

    private Action doHeal(Prince prince, TurnStrategy turnStrategy) {
        System.out.println("-- heal");
        List<TurnStrategy> history = turnStrategy.getGame().getHistory();
        if (history.size() > 0) {
            TurnStrategy lastStrategy = history.get(history.size() - 1);
            if (lastStrategy.getAction() instanceof Heal) {
                if (prince.getHealth() <= lastStrategy.getPrince().getHealth()) {
                    System.out.println(" -- but something is damaging me");
                    EDirection lastDirection = lastStrategy.getStepDirection();
                    if (lastStrategy.getGame().isRetreat()) {
                        System.out.println("-- continue retreat");
                        beginRetreat(turnStrategy, lastDirection);
                        return getRetreatAction(turnStrategy, lastDirection);
                    } else if (shouldRetreat(prince, turnStrategy, lastDirection.opposite())) {
                        System.out.println("-- turn back");
                        beginRetreat(turnStrategy, lastDirection.opposite());
                        return getRetreatAction(turnStrategy, lastDirection.opposite());
                    }

                    return turnStrategy.invokeNext(prince, turnStrategy);
                }
            }
        }

        if (turnStrategy.getGame().isRetreat()) {
            stopRetreat(turnStrategy);
        }

        return turnStrategy.heal();
    }

    private void beginRetreat(TurnStrategy turnStrategy, EDirection retreatDirection) {
        System.out.println(" -- retreat, direction: " + retreatDirection);
        turnStrategy.getGame().setRetreat(true);
        turnStrategy.setStepDirection(retreatDirection);
    }

    private void stopRetreat(TurnStrategy turnStrategy) {
        System.out.println(" -- ending retreat");
        turnStrategy.getGame().setRetreat(false);
    }

    public boolean shouldRetreat(Prince prince, TurnStrategy turnStrategy, EDirection retreatDirection) {
        if (retreatDirection != null) {
            Integer damage = turnStrategy.getGame().getLevelMap().getDamageAt(turnStrategy.getGame().getPlayerPos());
            if (damage != null) {
                Integer retreatDamage = getRetreatDamage(turnStrategy, retreatDirection);
                if (retreatDamage != null) {
                    return prince.getHealth() <= damage + retreatDamage;
                }
            }
        }

        return prince.getHealth() < Math.min(prince.getMaxHealth() / 2, GameContext.MIN_HEALTH);
    }

    private Integer getRetreatDamage(TurnStrategy turnStrategy, EDirection retreatDirection) {
        TurnStrategy retreatResult = getRetreatResult(turnStrategy, retreatDirection);
        int retreatPos = getRetreatPossition(retreatResult, turnStrategy.getGame().getPlayerPos());
        return turnStrategy.getGame().getLevelMap().getDamageAt(retreatPos);
    }

    private Action getRetreatAction(TurnStrategy turnStrategy, EDirection retreatDirection) {
        return getRetreatResult(turnStrategy, retreatDirection).getAction();
    }

    private TurnStrategy getRetreatResult(TurnStrategy turnStrategy, EDirection retreatDirection) {
        GameContext retreatContext = turnStrategy.getGame().clone();
        retreatContext.setRetreat(true);
        TurnStrategy retreatStrategy = new TurnStrategy(turnStrategy.getPrince(), retreatContext, PersiaStrategy.retreatStrategies);
        retreatStrategy.setStepDirection(retreatDirection);
        Action retreatAction = retreatStrategy.invokeNext(retreatStrategy.getPrince(), retreatStrategy);
        retreatStrategy.setAction(retreatAction);
        return retreatStrategy;
    }

    private int getRetreatPossition(TurnStrategy retreatResult, int currPlayerPos) {
        Utils.updatePrincePossition(retreatResult.getGame(), retreatResult.getAction());
        return retreatResult.getGame().getPlayerPos();
    }
}