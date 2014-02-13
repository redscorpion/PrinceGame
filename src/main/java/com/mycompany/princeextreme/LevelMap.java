package com.mycompany.princeextreme;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class LevelMap implements Cloneable {

    private static final Logger Log = Logger.getLogger(LevelMap.class.getName());

    private Map<Integer, MapField> mapFields = new HashMap<Integer, MapField>();

    public void updateLevelMap(int pos, Prince player) {
        final Field field = player.look(0);
        final Field nextField = player.look(1);
        final Field prevField = player.look(-1);

        updateMapField(pos, field);
        updateMapField(pos + 1, nextField);
        updateMapField(pos - 1, prevField);
    }

    public void updateFieldDamage(int playerPos, int damage) {
        MapField mapField = mapFields.get(playerPos);
        if (mapField != null) {
            mapField.setDamage(damage);
        }
    }

    public Integer getDamageAt(int playerPos) {
        MapField mapField = mapFields.get(playerPos);
        if (mapField != null) {
            return mapField.getDamage();
        }
        return null;
    }

    public MapField getMapField(int pos) {
        return mapFields.get(pos);
    }

    public boolean isEnemyNear(EObstacle enemy, int pos, int radius) {
        return getEnemyDirection(enemy, pos, radius) != null;
    }

    public EDirection getEnemyDirection(EObstacle enemy, int pos, int radius) {

        MapField mapField;

        for (int i = 0; i <= radius; i++) {
            mapField = mapFields.get(pos + i);
            if (mapField != null) {
                if (checkEnemy(enemy, mapField)) {
                    return EDirection.FWD;
                }
            }
        }

        for (int i = 1; i <= radius; i++) {
            mapField = mapFields.get(pos - i);
            if (mapField != null) {
                if (checkEnemy(enemy, mapField)) {
                    return EDirection.BKW;
                }
            }
        }

        return null;
    }

    public void reset() {
        mapFields.clear();
    }

    private boolean checkEnemy(EObstacle enemy, MapField mapField) {
        return mapField != null && enemy.equalsTo(mapField.getGameField().getObstacle()) && Utils.isAlive(mapField.getGameField().getObstacle());
    }

    private void updateMapField(int pos, Field gameField) {
        MapField mapField = mapFields.get(pos);

        if (gameField != null && mapField == null) {
            mapField = new MapField();
            mapFields.put(pos, mapField);
        }

        if (mapField != null) {
            Field oldGameField = mapField.getGameField();
            if (oldGameField != null && Utils.isEnemy(oldGameField.getObstacle()) && Utils.isAlive(oldGameField.getObstacle())) {
                if (gameField != null && Utils.isEnemy(gameField.getObstacle()) && !Utils.isAlive(gameField.getObstacle())) {
                    Log.fine("-- " + oldGameField.getObstacle().getName() + " is now dead");
                    int attackRange = Utils.getAttackRange(oldGameField.getObstacle());
                    for (int i = 1; i <= attackRange; i++) {
                        MapField f = mapFields.get(pos + i);
                        if (f != null && f.getDamage() != null) {
                            f.setDamage(Math.max(0, f.getDamage() - Utils.getAttack(oldGameField.getObstacle(), i)));
                        }
                    }
                    for (int i = 1; i <= attackRange; i++) {
                        MapField f = mapFields.get(pos - i);
                        if (f != null && f.getDamage() != null) {
                            f.setDamage(Math.max(0, f.getDamage() - Utils.getAttack(oldGameField.getObstacle(), i)));
                        }
                    }
                }
            }
            mapField.setGameField(gameField);
        }
    }

    @Override
    protected LevelMap clone() {
        try {
            LevelMap clone = (LevelMap) super.clone();
            clone.mapFields = new HashMap<Integer, LevelMap.MapField>(mapFields);
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class MapField {
        private Field gameField;
        private Integer damage;

        public Field getGameField() {
            return gameField;
        }

        private void setGameField(Field gameField) {
            this.gameField = gameField;
        }

        public Integer getDamage() {
            return damage;
        }

        public void setDamage(Integer damage) {
            this.damage = damage;
        }
    }
}
