package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.domain.EDirection;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Prince;

public class RetreatStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(RetreatStrategy.class.getName());

    private static final double RETREAT_HEAL_PCT = 0.333;

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {

        if (Utils.isSafeToHealHere(stepStrategy)) {
            Log.fine("-- it should be safe to heal here");

            if (stepStrategy.getGame().isRetreat()) {
                stopRetreat(stepStrategy);
            }

            return stepStrategy.evaluateNext();
        } else {
            Log.fine("-- it is not safe to heal here");
            final EDirection enemyDirection = Utils.getAttackingEnemyDirection(stepStrategy);
            if (enemyDirection == null) {
                Log.fine("-- and i don't see the enemy");
            }
            final RetreatAnalysisResult retreatResult = shouldRetreat(prince, stepStrategy, enemyDirection != null ? enemyDirection.opposite() : null);
            if (retreatResult.shouldRetreat) {
                Log.fine("-- low health");
                final Integer damage = stepStrategy.getGame().getGameMap().getDamageAt(stepStrategy.getGame().getPrincePos());
                if (damage != null && damage > prince.getHealth()) {
                    Log.fine("-- it's hard to survive here, damage is too high!");
                }

                if (retreatResult.retreatDirection == null) {
                    final StepStrategy lastStrategy = stepStrategy.getGame().getHistory().lastElement();
                    if (lastStrategy != null) {
                        if (lastStrategy.getGame().isRetreat()) {
                            Log.fine("-- continue in retreat");
                            retreatResult.retreatDirection = lastStrategy.getStepDirection();
                        } else {
                            Log.fine("-- turn back");
                            retreatResult.retreatDirection = lastStrategy.getStepDirection().opposite();
                        }
                    } else {
                        Log.fine("-- i'm totally lost, turn back");
                        retreatResult.retreatDirection = stepStrategy.getStepDirection().opposite();
                    }
                }

                beginRetreat(stepStrategy, retreatResult.retreatDirection);
                if (retreatResult.bestRetreatResult == null) {
                    retreatResult.bestRetreatResult = Utils.getBestRetreatResult(stepStrategy, retreatResult.retreatDirection);
                }

                final Action bestRetreatAction = retreatResult.bestRetreatResult.getGame().getStepAction();

                if (Utils.isHeal(bestRetreatAction)) {
                    Log.fine("-- not good, we can't move... only heal!");
                    return stepStrategy.evaluateNext();
                }
                return bestRetreatAction;
            }

            return stepStrategy.evaluateNext();
        }
    }

    private void beginRetreat(final StepStrategy stepStrategy, final EDirection retreatDirection) {
        Log.fine("-- retreat, direction: " + retreatDirection);
        stepStrategy.getGame().setRetreat(true);
        stepStrategy.setStepDirection(retreatDirection);
    }

    private void stopRetreat(final StepStrategy stepStrategy) {
        Log.fine("-- ending retreat");
        stepStrategy.getGame().setRetreat(false);
    }

    public RetreatAnalysisResult shouldRetreat(final Prince prince, final StepStrategy stepStrategy, final EDirection retreatDirection) {
        final RetreatAnalysisResult result = new RetreatAnalysisResult();
        result.retreatDirection = retreatDirection;
        result.damage = stepStrategy.getGame().getGameMap().getDamageAt(stepStrategy.getGame().getPrincePos());
        if (result.damage != null && retreatDirection != null) {
            result.bestRetreatResult = Utils.getBestRetreatResult(stepStrategy, retreatDirection);
            final int retreatPos = Utils.getRetreatPossition(result.bestRetreatResult, result.bestRetreatResult.getGame().getPrincePos());
            if (stepStrategy.getGame().getPrincePos() != retreatPos) {
                result.retreatDamage = result.bestRetreatResult.getGame().getGameMap().getDamageAt(retreatPos);
                if (result.retreatDamage != null) {
                    result.shouldRetreat = prince.getHealth() <= result.damage + result.retreatDamage;
                    return result;
                }
            }
        }

        result.shouldRetreat = (result.damage != null && prince.getHealth() <= result.damage)
                || prince.getHealth() < Math.ceil(prince.getMaxHealth() * RETREAT_HEAL_PCT);
        return result;
    }

    private static class RetreatAnalysisResult {
        private EDirection retreatDirection;
        private Integer damage;
        private Integer retreatDamage;
        private StepStrategy bestRetreatResult;
        private boolean shouldRetreat;
    }
}