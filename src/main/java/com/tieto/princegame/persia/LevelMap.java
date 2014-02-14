package com.tieto.princegame.persia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.tieto.princegame.persia.domain.EDirection;

import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
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

    public List<Obstacle> getAllEnemies() {
        List<Obstacle> enemies = new ArrayList<Obstacle>();

        for (MapField mapField : mapFields.values()) {
            if (isDeadly(mapField)) {
                enemies.add(mapField.getGameField().getObstacle());
            }
        }

        return enemies;
    }

    public List<Obstacle> findEnemyNear(int pos, int radius) {

        List<Obstacle> enemies = new ArrayList<Obstacle>();

        MapField mapField;

        for (int i = 0; i <= radius; i++) {
            mapField = mapFields.get(pos + i);
            if (isDeadly(mapField)) {
                enemies.add(mapField.getGameField().getObstacle());
            }
        }

        for (int i = 1; i <= radius; i++) {
            mapField = mapFields.get(pos - i);
            if (isDeadly(mapField)) {
                enemies.add(mapField.getGameField().getObstacle());
            }
        }

        return enemies;
    }

    public EDirection getEnemyDirection(Obstacle enemy, int princePos) {
        int distance = getEnemyDistance(enemy, princePos);
        if (Integer.MAX_VALUE == distance) {
            return null;
        }

        return distance > 0 ? EDirection.FWD : EDirection.BKW;
    }

    public int getEnemyDistance(Obstacle enemy, int princePos) {
        for (Entry<Integer, MapField> entry : mapFields.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getGameField() != null && entry.getValue().getGameField().getObstacle() != null
                    && entry.getValue().getGameField().getObstacle().getId() == enemy.getId()) {
                return entry.getKey() - princePos;
            }
        }
        return Integer.MAX_VALUE;
    }

    public void reset() {
        mapFields.clear();
    }

    private boolean isDeadly(MapField mapField) {
        return mapField != null && mapField.getGameField() != null && Utils.isAliveEnemy(mapField.getGameField().getObstacle());
    }

    private void updateMapField(int pos, Field gameField) {
        MapField mapField = mapFields.get(pos);

        if (gameField != null && mapField == null) {
            mapField = new MapField();
            mapFields.put(pos, mapField);
        }

        if (mapField != null) {
            Field oldGameField = mapField.getGameField();
            if (oldGameField != null && Utils.isAliveEnemy(oldGameField.getObstacle()) && gameField != null && Utils.isDeadEnemy(gameField.getObstacle())
                    && gameField.getObstacle().getId() == oldGameField.getObstacle().getId()) {
                enemyKilled(pos, gameField.getObstacle());

            }
            mapField.setGameField(gameField);
        }
    }

    private void enemyKilled(int enemyPos, Obstacle enemy) {
        Log.fine("-- enemy " + enemy.getName() + " is now dead");

        int attackRange = Utils.getAttackRange(enemy);

        for (int i = 1; i <= attackRange; i++) {
            MapField f = mapFields.get(enemyPos + i);
            if (f != null && f.getDamage() != null) {
                f.setDamage(Math.max(0, f.getDamage() - Utils.getAttack(enemy, i)));
            }
        }

        for (int i = 1; i <= attackRange; i++) {
            MapField f = mapFields.get(enemyPos - i);
            if (f != null && f.getDamage() != null) {
                f.setDamage(Math.max(0, f.getDamage() - Utils.getAttack(enemy, i)));
            }
        }
    }

    @Override
    public LevelMap clone() {
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
