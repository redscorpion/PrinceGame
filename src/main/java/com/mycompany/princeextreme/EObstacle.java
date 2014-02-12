/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.mycompany.princeextreme;

import cz.tieto.princegame.common.gameobject.Obstacle;

public enum EObstacle {

    PITFALL("pitfall", 0), KNIGHT("knight", 1);

    private String name;
    int attack;

    private EObstacle(String name, int attack) {
        this.name = name;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public boolean equalsTo(Obstacle ob) {
        return ob != null && getName().equals(ob.getName());
    }

    public static EObstacle valueOf(Obstacle ob) {
        if (ob == null) {
            return null;
        }

        for (EObstacle eObstacle : values()) {
            if (eObstacle.getName().equals(ob.getName())) {
                return eObstacle;
            }
        }
        return null;
    }

}