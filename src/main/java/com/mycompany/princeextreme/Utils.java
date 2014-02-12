package com.mycompany.princeextreme;

import com.mycompany.princeextreme.LevelMap.MapField;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class Utils {

    public static boolean isAlive(Obstacle obstacle) {
        return obstacle != null && obstacle.getProperty("dead").equals("false");
    }

    public static int getHealth(Obstacle obstacle) {
        return obstacle != null ? new Integer(obstacle.getProperty("health")) : 0;
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

    public static int getAttack(Obstacle obstacle) {
        return getAttack(EObstacle.valueOf(obstacle));
    }

    public static int getAttack(EObstacle obstacle) {
        return obstacle != null ? obstacle.getAttack() : 0;
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
        int playerPos = turnStrategy.getPlayerPos();
        LevelMap levelMap = turnStrategy.getGameStrategy().getLevelMap();

        switch (turnStrategy.getStepDirection()) {
        case BKW:
            for (int i = 1; i < jumpLength; i++) {
                MapField mapField = levelMap.getMapField(playerPos - i);
                if (mapField == null) {
                    return false;
                }
                if (mapField.gameField.getObstacle() != null || mapField.gameField.getEquipment() != null) {
                    return false;
                }
            }
            return true;

        case FWD:
            for (int i = 1; i < jumpLength; i++) {
                MapField mapField = levelMap.getMapField(playerPos + i);
                if (mapField == null) {
                    return false;
                }
                if (mapField.gameField.getObstacle() != null || mapField.gameField.getEquipment() != null) {
                    return false;
                }

            }
            return true;

        default:
        }

        return false;
    }

    public static boolean isEnemy(Obstacle obstacle) {
        return EObstacle.KNIGHT.equalsTo(obstacle);
    }

    public static boolean isSafeToHeal(TurnStrategy turnStrategy) {
        return isSafeToHeal(turnStrategy.getPrince().look(-1)) && isSafeToHeal(turnStrategy.getPrince().look(1));
    }

    private static boolean isSafeToHeal(Field next) {
        if (next != null) {
            Obstacle obstacle = next.getObstacle();
            if (!Utils.isEnemy(obstacle) || !Utils.isAlive(obstacle)) {
                return true;
            }
            return false;
        }

        return true;
    }
}
