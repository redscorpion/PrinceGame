package com.tieto.princegame.persia.actionstrategies;

import java.util.logging.Logger;

import com.tieto.princegame.persia.TurnStrategy;
import com.tieto.princegame.persia.Utils;
import com.tieto.princegame.persia.PersiaStrategy.ActionStrategy;
import com.tieto.princegame.persia.domain.EDirection;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class AttackEnemyStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(AttackEnemyStrategy.class.getName());

    private static final int ATTACK_RANGE = 1;

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Obstacle enemy = Utils.getNearestEnemy(turnStrategy.getGame(), turnStrategy.getGame().getPrincePos(), ATTACK_RANGE);
        if (enemy != null) {
            EDirection enemyDirection = Utils.getEnemyDirection(turnStrategy.getGame(), enemy);
            turnStrategy.setStepDirection(enemyDirection);
            turnStrategy.getGame().setStepDirection(turnStrategy.getStepDirection());

            Log.fine("-- enemy: " + enemy.getName() + " health:" + Utils.getHealth(enemy));
            Equipment weapon = Utils.getBestWeapon(prince, enemy);
            Log.fine("-- weapon: " + (weapon != null ? weapon.getName() : "null"));
            if (weapon == null) {
                Log.fine("-- switch direction and find weapon");
                turnStrategy.switchStepDirection();
                turnStrategy.getGame().setStepDirection(turnStrategy.getStepDirection());
                return turnStrategy.evaluateNext();
            }

            return turnStrategy.use(weapon, enemy);
        }

        return turnStrategy.evaluateNext();
    }
}