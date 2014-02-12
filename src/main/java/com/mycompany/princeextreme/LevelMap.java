package com.mycompany.princeextreme;

import java.util.HashMap;
import java.util.Map;

import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class LevelMap implements Cloneable {

    private Map<Integer, MapField> mapFields = new HashMap<Integer, MapField>();

    public void updateLevelMap(int pos, Prince player) {
        final Field field = player.look(0);
        final Field nextField = player.look(1);
        final Field prevField = player.look(-1);

        MapField mapField = mapFields.get(pos);
        MapField nextMapField = mapFields.get(pos + 1);
        MapField prevMapField = mapFields.get(pos - 1);

        if (field != null && mapField == null) {
            mapField = new MapField();
        }

        if (nextField != null && nextMapField == null) {
            nextMapField = new MapField();
        }

        if (prevField != null && prevMapField == null) {
            prevMapField = new MapField();
        }

        if (mapField != null) {
            mapField.setNext(nextMapField);
            mapField.setPrevious(prevMapField);
            mapField.setGameField(field);
            mapFields.put(pos, mapField);
        }

        if (nextMapField != null) {
            nextMapField.setPrevious(mapField);
            nextMapField.setGameField(nextField);
            mapFields.put(pos + 1, nextMapField);
        }

        if (prevMapField != null) {
            prevMapField.setNext(mapField);
            prevMapField.setGameField(prevField);
            mapFields.put(pos - 1, prevMapField);
        }
    }

    public MapField getMapField(int pos) {
        return mapFields.get(pos);
    }

    public void reset() {
        mapFields.clear();
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

    private boolean checkEnemy(EObstacle enemy, MapField mapField) {
        return mapField != null && enemy.equalsTo(mapField.getGameField().getObstacle()) && Utils.isAlive(mapField.getGameField().getObstacle());
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
        private MapField prev;
        private MapField next;
        private Field gameField;

        public MapField getPrevious() {
            return prev;
        }

        private void setPrevious(MapField prev) {
            this.prev = prev;
        }

        public MapField getNext() {
            return next;
        }

        private void setNext(MapField next) {
            this.next = next;
        }

        public Field getGameField() {
            return gameField;
        }

        private void setGameField(Field gameField) {
            this.gameField = gameField;
        }

    }

}
