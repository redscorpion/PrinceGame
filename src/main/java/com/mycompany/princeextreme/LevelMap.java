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
        mapFields.put(pos, mapField);

        if (nextMapField != null) {
            nextMapField.prev = mapField;
            mapFields.put(pos + 1, nextMapField);
        }

        if (prevMapField != null) {
            prevMapField.next = mapField;
            mapFields.put(pos - 1, prevMapField);
        }
    }

    public MapField getMapField(int pos) {
        return mapFields.get(pos);
    }

    public void reset() {
        mapFields.clear();
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

    public boolean isDragonNear(int pos) {
        MapField mapField = mapFields.get(pos);
        boolean dragon = false;
        if (mapField != null) {
            dragon |= checkDragon(mapField);
        }

        mapField = mapFields.get(pos - 1);
        if (mapField != null) {
            dragon |= checkDragon(mapField);
        }
        mapField = mapFields.get(pos - 2);
        if (mapField != null) {
            dragon |= checkDragon(mapField);
        }
        mapField = mapFields.get(pos + 1);
        if (mapField != null) {
            dragon |= checkDragon(mapField);
        }
        mapField = mapFields.get(pos + 2);
        if (mapField != null) {
            dragon |= checkDragon(mapField);
        }

        return dragon;
    }

    /**
     * @param mapField
     * @return
     */
    private boolean checkDragon(MapField mapField) {
        return mapField != null && EObstacle.DRAGON.equalsTo(mapField.gameField.getObstacle()) && Utils.isAlive(mapField.gameField.getObstacle());
    }

    public static class MapField {
        public MapField prev;
        public MapField next;
        public Field gameField;

    }
}
