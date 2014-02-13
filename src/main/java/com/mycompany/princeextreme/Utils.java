package com.mycompany.princeextreme;

import com.mycompany.princeextreme.LevelMap.MapField;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
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

    public static int getAttackRange(Obstacle obstacle) {
        return getAttackRange(EObstacle.valueOf(obstacle));
    }

    public static int getAttackRange(EObstacle obstacle) {
        return obstacle != null ? obstacle.getAttackRange() : 0;
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
        return isSafeToMoveFast(turnStrategy.getGame(), turnStrategy.getStepDirection(), jumpLength);
    }

    public static boolean isSafeToMoveFast(GameContext game, EDirection direction, int jumpLength) {
        int playerPos = game.getPlayerPos();
        LevelMap levelMap = game.getLevelMap();

        int dir = (EDirection.FWD == direction) ? 1 : -1;

        for (int i = 1; i <= jumpLength; i++) {
            MapField mapField = levelMap.getMapField(playerPos + i * dir);
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
        GameContext gameStrategy = turnStrategy.getGame();

        EObstacle[] values = EObstacle.values();
        for (EObstacle eObstacle : values) {
            if (isEnemy(eObstacle)) {
                if (gameStrategy.getLevelMap().isEnemyNear(eObstacle, gameStrategy.getPlayerPos(), eObstacle.getAttackRange())) {
                    return false;
                }
            }
        }

        Integer damage = gameStrategy.getLevelMap().getDamageAt(gameStrategy.getPlayerPos());
        return damage == null || damage <= 0;
    }

    public static EDirection geEnemyDirection(TurnStrategy turnStrategy) {
        GameContext gameStrategy = turnStrategy.getGame();

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

    public static void updatePrincePossition(GameContext game, Action action) {
        game.setPlayerPos(getNewPrincePossition(game.getPlayerPos(), action));
    }

    public static int getNewPrincePossition(int currentPossition, Action action) {
        int newPossition = currentPossition;
        if (action instanceof JumpForward) {
            newPossition += 2;
        } else if (action instanceof JumpBackward) {
            newPossition -= 2;
        } else if (action instanceof MoveForward) {
            newPossition += 1;
        } else if (action instanceof MoveBackward) {
            newPossition -= 1;
        }
        return newPossition;
    }
}
