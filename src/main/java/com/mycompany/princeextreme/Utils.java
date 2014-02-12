package com.mycompany.princeextreme;

import com.mycompany.princeextreme.LevelMap.MapField;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class Utils {

    public static boolean isAlive(Obstacle obstacle) {
        return obstacle != null && !Boolean.parseBoolean(obstacle.getProperty("dead"));
    }

    public static int getHealth(Obstacle obstacle) {
        return obstacle != null ? Integer.parseInt(obstacle.getProperty("health")) : 0;
    }

    public static Equipment getEquipment(Prince prince, EEquipment equipment) {
        return getEquipment(prince, equipment.getName());
    }

    public static Equipment getEquipment(Prince prince, String name) {
        for (Equipment eq : prince.getInventory()) {
            if (eq.getName().equals(name)) {
                return eq;
            }
        }
        return null;
    }

    public static int getAttack(Obstacle obstacle, int distance) {
        return getAttack(EObstacle.valueOf(obstacle), distance);
    }

    public static int getAttack(EObstacle obstacle, int distance) {
        return obstacle != null ? obstacle.getAttack(distance) : 0;
    }

    public static int getWeaponAttack(Equipment weapon) {
        return getWeaponAttack(EEquipment.valueOf(weapon));
    }

    public static int getWeaponAttack(EEquipment weapon) {
        return weapon != null ? weapon.getAttack() : 0;
    }

    public static Equipment getWeapon(Prince prince, Obstacle obstacle) {
        return getEquipment(prince, EEquipment.SWORD);
    }

    public static boolean isSafeToMoveFast(TurnStrategy turnStrategy, int jumpLength) {
        int playerPos = turnStrategy.getGameStrategy().getPlayerPos();
        LevelMap levelMap = turnStrategy.getGameStrategy().getLevelMap();

        int direction = (EDirection.FWD == turnStrategy.getStepDirection()) ? 1 : -1;

        for (int i = 1; i <= jumpLength; i++) {
            MapField mapField = levelMap.getMapField(playerPos + i * direction);
            if (mapField == null || mapField.getGameField().getEquipment() != null) {
                return false;
            }
            if (mapField.getGameField().getObstacle() != null) {
                Obstacle obstacle = mapField.getGameField().getObstacle();
                if (!isEnemy(obstacle) || isAlive(obstacle)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isEnemy(Obstacle obstacle) {
        return isEnemy(EObstacle.valueOf(obstacle));
    }

    public static boolean isEnemy(EObstacle obstacle) {
        return EObstacle.KNIGHT == obstacle || EObstacle.DRAGON == obstacle;
    }

    public static boolean isSafeToHealHere(TurnStrategy turnStrategy) {
        GameStrategy gameStrategy = turnStrategy.getGameStrategy();

        EObstacle[] values = EObstacle.values();
        for (EObstacle eObstacle : values) {
            if (isEnemy(eObstacle)) {
                if (gameStrategy.getLevelMap().isEnemyNear(eObstacle, gameStrategy.getPlayerPos(), eObstacle.getAttackRange())) {
                    return false;
                }
            }
        }

        return true;
    }

    public static EDirection geEnemyDirection(TurnStrategy turnStrategy) {
        GameStrategy gameStrategy = turnStrategy.getGameStrategy();

        EObstacle[] values = EObstacle.values();
        for (EObstacle eObstacle : values) {
            if (isEnemy(eObstacle)) {
                EDirection enemyDirection = gameStrategy.getLevelMap().getEnemyDirection(eObstacle, gameStrategy.getPlayerPos(), eObstacle.getAttackRange());
                if (enemyDirection != null) {
                    return enemyDirection;
                }
            }
        }

        return null;
    }

}
