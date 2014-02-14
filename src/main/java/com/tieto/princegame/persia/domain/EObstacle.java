/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.tieto.princegame.persia.domain;

import cz.tieto.princegame.common.gameobject.Obstacle;

public enum EObstacle {

    PITFALL("pitfall", 0), KNIGHT("knight", 1, 1), DRAGON("dragon", 2, 3, 1), CHOPPER("chopper", 0);

    private String name;
    private int attackRange;
    private int[] attack;

    private EObstacle(String name, int attackRange, int... attack) {
        this.name = name;
        this.attackRange = attackRange;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getAttack(int distance) {
        if (distance > attackRange) {
            return 0;
        }
        return attack[distance - 1];
    }

    public int getAttackRange() {
        return attackRange;
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