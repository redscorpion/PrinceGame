package com.mycompany.princeextreme.actionstrategies;

import java.util.logging.Logger;

import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class AttackEnemyStrategy implements ActionStrategy {

    private static final Logger Log = Logger.getLogger(AttackEnemyStrategy.class.getName());

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);
        if (next != null) {
            Obstacle obstacle = next.getObstacle();

            if (Utils.isEnemy(obstacle) && Utils.isAlive(obstacle)) {
                Log.fine("-- enemy: " + obstacle.getName() + " health:" + Utils.getHealth(obstacle));
                Equipment weapon = Utils.getWeapon(prince, obstacle);
                Log.fine("-- weapon: " + (weapon != null ? weapon.getName() : "null"));
                if (weapon == null) {
                    Log.fine("-- switch direction and find weapon");
                    turnStrategy.setStepDirection(turnStrategy.getStepDirection().opposite());
                    turnStrategy.getGame().setDirection(turnStrategy.getStepDirection());
                    return turnStrategy.invokeNext(prince, turnStrategy);
                }

                return turnStrategy.use(weapon, obstacle);
            }
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }
}