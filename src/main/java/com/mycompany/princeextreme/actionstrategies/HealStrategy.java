package com.mycompany.princeextreme.actionstrategies;

import java.util.List;
import java.util.logging.Logger;

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
                    List<TurnStrategy> history = turnStrategy.getGame().getHistory();
                    if (history.size() > 0) {
                        TurnStrategy lastStrategy = history.get(history.size() - 1);
                        if (lastStrategy.getGame().isRetreat()) {
                            Log.fine("-- continue retreat");
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
                return getBestRetreatAction(turnStrategy, enemyDirection.opposite());
            }
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }

    private Action doHeal(Prince prince, TurnStrategy turnStrategy) {
        Log.fine("-- heal");
        List<TurnStrategy> history = turnStrategy.getGame().getHistory();
        if (history.size() > 0) {
            TurnStrategy lastStrategy = history.get(history.size() - 1);
            if (lastStrategy.getAction() instanceof Heal) {
                if (prince.getHealth() <= lastStrategy.getPrince().getHealth()) {
                    Log.fine(" -- but something is damaging me");
                    EDirection lastDirection = lastStrategy.getStepDirection();
                    if (lastStrategy.getGame().isRetreat()) {
                        Log.fine("-- continue retreat");
                        beginRetreat(turnStrategy, lastDirection);
                        return getBestRetreatAction(turnStrategy, lastDirection);
                    } else if (shouldRetreat(prince, turnStrategy, lastDirection.opposite())) {
                        Log.fine("-- turn back");
                        beginRetreat(turnStrategy, lastDirection.opposite());
                        return getBestRetreatAction(turnStrategy, lastDirection.opposite());
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
            Integer damage = turnStrategy.getGame().getLevelMap().getDamageAt(turnStrategy.getGame().getPlayerPos());
            if (damage != null) {
                Integer retreatDamage = getSmallestRetreatDamage(turnStrategy, retreatDirection);
                if (retreatDamage != null) {
                    return prince.getHealth() <= damage + retreatDamage;
                }
            }
        }

        return prince.getHealth() < Math.min(prince.getMaxHealth() / 2, GameContext.MIN_HEALTH);
    }

    private Integer getSmallestRetreatDamage(TurnStrategy turnStrategy, EDirection retreatDirection) {
        TurnStrategy retreatResult = getBestRetreatResult(turnStrategy, retreatDirection);
        int retreatPos = getRetreatPossition(retreatResult, retreatResult.getGame().getPlayerPos());
        return retreatResult.getGame().getLevelMap().getDamageAt(retreatPos);
    }

    private Action getBestRetreatAction(TurnStrategy turnStrategy, EDirection retreatDirection) {
        return getBestRetreatResult(turnStrategy, retreatDirection).getAction();
    }

    private TurnStrategy getBestRetreatResult(TurnStrategy turnStrategy, EDirection retreatDirection) {

        TurnStrategy retreatResultWithoutJumping = getRetreatResult(turnStrategy, retreatDirection, false);
        int retreatPosWithoutJumping = getRetreatPossition(retreatResultWithoutJumping, retreatResultWithoutJumping.getGame().getPlayerPos());
        Integer damageWithoutJumping = retreatResultWithoutJumping.getGame().getLevelMap().getDamageAt(retreatPosWithoutJumping);

        if (damageWithoutJumping == 0) {
            return retreatResultWithoutJumping;
        }

        TurnStrategy retreatResultWithJumping = getRetreatResult(turnStrategy, retreatDirection, true);
        int retreatPosWithJumping = getRetreatPossition(retreatResultWithJumping, retreatResultWithJumping.getGame().getPlayerPos());
        Integer damageWithJumping = retreatResultWithJumping.getGame().getLevelMap().getDamageAt(retreatPosWithJumping);

        if (damageWithJumping == null) {
            return retreatResultWithoutJumping;
        }

        if (damageWithoutJumping == null) {
            return retreatResultWithJumping;
        }

        return damageWithoutJumping < damageWithJumping ? retreatResultWithoutJumping : retreatResultWithJumping;
    }

    private TurnStrategy getRetreatResult(TurnStrategy turnStrategy, EDirection retreatDirection, boolean allowJumping) {
        GameContext retreatContext = turnStrategy.getGame().clone(true);
        retreatContext.setRetreat(true);
        retreatContext.setAllowJumping(allowJumping);
        TurnStrategy retreatStrategy = new TurnStrategy(turnStrategy.getPrince(), retreatContext, PersiaStrategy.retreatStrategies);
        retreatStrategy.setStepDirection(retreatDirection);
        Action retreatAction = retreatStrategy.invokeNext(retreatStrategy.getPrince(), retreatStrategy);
        retreatStrategy.setAction(retreatAction);
        return retreatStrategy;
    }

    private int getRetreatPossition(TurnStrategy retreatResult, int currPlayerPos) {
        return Utils.getNewPrincePossition(retreatResult.getGame().getPlayerPos(), retreatResult.getAction());
    }
}