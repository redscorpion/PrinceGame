package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.StepStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.domain.EDirection;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class AttackEnemyStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(AttackEnemyStrategy.class.getName());

    public Action getAction(final Prince prince, final StepStrategy stepStrategy) {
        final Obstacle enemy = Utils.getEnemyToAttack(stepStrategy.getGame());
        if (enemy != null) {
            final int enemyDistance = Utils.getEnemyDistance(stepStrategy.getGame(), enemy);
            final EDirection enemyDirection = Utils.getEnemyDirection(enemyDistance);
            stepStrategy.setStepDirection(enemyDirection);
            stepStrategy.getGame().setStepDirection(stepStrategy.getStepDirection());

            Log.fine("-- enemy: " + enemy.getName() + " health: " + EObstacle.getHealth(enemy));
            final Equipment weapon = Utils.getBestWeapon(prince, enemy, enemyDistance);
            Log.fine("-- weapon: " + (weapon != null ? weapon.getName() : "null"));
            if (weapon == null) {
                Log.fine("-- switch direction and find weapon");
                stepStrategy.switchStepDirection();
                stepStrategy.getGame().setStepDirection(stepStrategy.getStepDirection());
                return stepStrategy.evaluateNext();
            }

            return stepStrategy.use(weapon, enemy);
        }

        return stepStrategy.evaluateNext();
    }
}