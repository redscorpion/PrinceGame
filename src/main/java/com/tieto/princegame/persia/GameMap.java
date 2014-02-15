package com.tieto.princegame.persia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.tieto.princegame.persia.domain.EDirection;
import com.tieto.princegame.persia.domain.EObstacle;

import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;
import cz.tieto.princegame.common.gameobject.Prince;

public class GameMap implements Cloneable {

    private static final Logger Log = Logger.getLogger(GameMap.class.getName());

    private Map<Integer, MapField> mapFields = new HashMap<Integer, MapField>();

    public void update(final int pos, final Prince player) {
        final Field field = player.look(0);
        final Field nextField = player.look(1);
        final Field prevField = player.look(-1);

        updateMapField(pos, field);
        updateMapField(pos + 1, nextField);
        updateMapField(pos - 1, prevField);
    }

    public void updateDamage(final int pos, final int damage) {
        final MapField mapField = mapFields.get(pos);
        if (mapField != null) {
            mapField.setDamage(damage);
        }
    }

    public Integer getDamageAt(final int pos) {
        final MapField mapField = mapFields.get(pos);
        if (mapField != null) {
            return mapField.getDamage();
        }
        return null;
    }

    public Field getGameField(final int pos) {
        final MapField mapField = mapFields.get(pos);
        return mapField != null ? mapField.getGameField() : null;
    }

    public List<Obstacle> getAllEnemies() {
        final List<Obstacle> enemies = new ArrayList<Obstacle>();

        for (final MapField mapField : mapFields.values()) {
            if (isDeadly(mapField)) {
                enemies.add(mapField.getGameField().getObstacle());
            }
        }

        return enemies;
    }

    public List<Obstacle> findEnemyNear(final int pos, final int radius) {
        final List<Obstacle> enemies = new ArrayList<Obstacle>();

        for (int i = pos - radius; i <= pos + radius; i++) {
            final MapField mapField = mapFields.get(i);
            if (isDeadly(mapField)) {
                enemies.add(mapField.getGameField().getObstacle());
            }
        }

        return enemies;
    }

    public EDirection getEnemyDirection(final int princePos, final Obstacle enemy) {
        final int distance = getEnemyDistance(princePos, enemy);
        if (Integer.MAX_VALUE == distance) {
            return null;
        }

        return EDirection.valueOf(distance);
    }

    public int getEnemyDistance(final int princePos, final Obstacle enemy) {
        for (final Entry<Integer, MapField> entry : mapFields.entrySet()) {
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

    private boolean isDeadly(final MapField mapField) {
        return mapField != null && mapField.getGameField() != null && EObstacle.isAliveEnemy(mapField.getGameField().getObstacle());
    }

    private void updateMapField(final int pos, final Field gameField) {
        MapField mapField = mapFields.get(pos);

        if (gameField != null && mapField == null) {
            mapField = new MapField();
            mapFields.put(pos, mapField);
        }

        if (mapField != null) {
            final Field oldGameField = mapField.getGameField();
            if (oldGameField != null && EObstacle.isAliveEnemy(oldGameField.getObstacle()) && gameField != null
                    && EObstacle.isDeadEnemy(gameField.getObstacle()) && gameField.getObstacle().getId() == oldGameField.getObstacle().getId()) {
                enemyKilled(pos, gameField.getObstacle());

            }
            mapField.setGameField(gameField);
        }
    }

    private void enemyKilled(final int enemyPos, final Obstacle enemy) {
        Log.fine("-- enemy " + enemy.getName() + " is now dead");

        final int attackRange = EObstacle.getAttackRange(enemy);

        for (int i = enemyPos - attackRange; i <= enemyPos + attackRange; i++) {
            final MapField f = mapFields.get(i);
            if (f != null && f.getDamage() != null) {
                f.setDamage(Math.max(0, f.getDamage() - EObstacle.getAttack(enemy, i - enemyPos)));
            }
        }
    }

    @Override
    public GameMap clone() {
        try {
            final GameMap clone = (GameMap) super.clone();
            clone.mapFields = new HashMap<Integer, GameMap.MapField>(mapFields);
            return clone;
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class MapField {
        private Field gameField;
        private Integer damage;

        private Field getGameField() {
            return gameField;
        }

        private void setGameField(final Field gameField) {
            this.gameField = gameField;
        }

        private Integer getDamage() {
            return damage;
        }

        private void setDamage(final Integer damage) {
            this.damage = damage;
        }
    }
}
