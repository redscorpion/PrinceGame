package com.mycompany.princeextreme;

import java.util.HashMap;
import java.util.Map;

import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Prince;

public class LevelMap implements Cloneable {

    private Map<Integer, MapField> mapFields = new HashMap<Integer, MapField>();

    public void updateLevelMap(int pos, Prince player) {
        Field field = player.look(0);
        Field nextField = player.look(1);
        Field prevField = player.look(-1);

        MapField mapField = mapFields.get(pos);
        MapField nextMapField = mapFields.get(pos + 1);
        MapField prevMapField = mapFields.get(pos - 1);

        if (mapField == null) {
            mapField = new MapField();
            mapField.gameField = field;
        }

        if (nextField != null && nextMapField == null) {
            nextMapField = new MapField();
            nextMapField.gameField = nextField;
        }

        if (prevField != null && prevMapField == null) {
            prevMapField = new MapField();
            prevMapField.gameField = prevField;
        }

        mapField.next = nextMapField;
        mapField.prev = prevMapField;
        mapField.gameField = field;
        mapFields.put(pos, mapField);

        if (nextMapField != null) {
            nextMapField.prev = mapField;
            nextMapField.gameField = nextField;
            mapFields.put(pos + 1, nextMapField);
        }

        if (prevMapField != null) {
            prevMapField.next = mapField;
            prevMapField.gameField = prevField;
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
        return mapField != null && enemy.equalsTo(mapField.gameField.getObstacle()) && Utils.isAlive(mapField.gameField.getObstacle());
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
        public MapField prev;
        public MapField next;
        public Field gameField;

    }

}
