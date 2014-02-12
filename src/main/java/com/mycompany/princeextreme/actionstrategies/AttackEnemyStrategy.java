package com.mycompany.princeextreme.actionstrategies;

import com.mycompany.princeextreme.EDirection;
import com.mycompany.princeextreme.GameStrategy;
import com.mycompany.princeextreme.PersiaStrategy.ActionStrategy;
import com.mycompany.princeextreme.TurnStrategy;
import com.mycompany.princeextreme.Utils;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class AttackEnemyStrategy implements ActionStrategy {

    public Action getAction(Prince prince, TurnStrategy turnStrategy) {
        Field next = turnStrategy.getNextStepField(prince);
        if (next != null) {
            Obstacle obstacle = next.getObstacle();

            if (Utils.isEnemy(obstacle) && Utils.isAlive(obstacle)) {
                System.out.println("-- enemy: " + obstacle.getName());
                Equipment weapon = Utils.getWeapon(prince, obstacle);
                System.out.println("-- weapon: " + (weapon != null ? weapon.getName() : "null"));
                if (weapon == null) {
                    System.out.println("-- switch direction and find weapon");
                    turnStrategy.setStepDirection(turnStrategy.getStepDirection() == EDirection.FWD ? EDirection.BKW : EDirection.FWD);
                    turnStrategy.getGameStrategy().setDirection(turnStrategy.getStepDirection());
                    return turnStrategy.invokeNext(prince, turnStrategy);
                }

                // ?
                if (Utils.getHealth(obstacle) - Utils.getWeaponAttack(weapon) <= 0) {
                    return turnStrategy.use(weapon, obstacle);
                }

                if (prince.getHealth() - Utils.getAttack(obstacle, 1) < GameStrategy.MIN_ATTACK_HEALTH) {
                    System.out.println("-- retreat and heal");
                    turnStrategy.setStepDirection(turnStrategy.getStepDirection() == EDirection.FWD ? EDirection.BKW : EDirection.FWD);
                    int weaponAttackPerTurn = Utils.getWeaponAttack(weapon);
                    int enemyHealth = Utils.getHealth(obstacle);
                    int turnsNeeded = (int) Math.ceil(enemyHealth / weaponAttackPerTurn);
                    turnStrategy.getGameStrategy().setNeededHealth(
                            turnsNeeded * Utils.getAttack(obstacle, 1) + Utils.getAttack(obstacle, 2) + GameStrategy.MIN_ATTACK_HEALTH);
                    turnStrategy.doNotHeal = true;
                    return turnStrategy.invokeNext(prince, turnStrategy);
                }

                return turnStrategy.use(weapon, obstacle);
            }
        }

        return turnStrategy.invokeNext(prince, turnStrategy);
    }
}