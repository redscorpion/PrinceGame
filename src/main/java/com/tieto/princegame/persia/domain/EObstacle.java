/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.tieto.princegame.persia.domain;

import cz.tieto.princegame.common.gameobject.Obstacle;

public enum EObstacle {

    PITFALL("pitfall", 0), KNIGHT("knight", 1, 1), DRAGON("dragon", 2, 3, 1), CHOPPER("chopper", 0), THORNBUSH("thornbush", 0);

    private String name;
    private int attackRange;
    private int[] attack;

    private EObstacle(final String name, final int attackRange, final int... attack) {
        this.name = name;
        this.attackRange = attackRange;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getAttack(int distance) {
        distance = Math.abs(distance);
        if (distance > attackRange) {
            return 0;
        }
        return attack[distance - 1];
    }

    public int getAttackRange() {
        return attackRange;
    }

    public boolean equalsTo(final Obstacle ob) {
        return ob != null && getName().equals(ob.getName());
    }

    public static EObstacle valueOf(final Obstacle ob) {
        if (ob == null) {
            return null;
        }

        for (final EObstacle eObstacle : values()) {
            if (eObstacle.getName().equals(ob.getName())) {
                return eObstacle;
            }
        }
        return null;
    }

    public static boolean isAlive(final Obstacle obstacle) {
        return obstacle != null && !Boolean.parseBoolean(obstacle.getProperty("dead"));
    }

    public static boolean isAliveEnemy(final Obstacle obstacle) {
        return obstacle != null && isEnemy(obstacle) && isAlive(obstacle);
    }

    public static boolean isDeadEnemy(final Obstacle obstacle) {
        return obstacle != null && isEnemy(obstacle) && !isAlive(obstacle);
    }

    public static int getHealth(final Obstacle obstacle) {
        return obstacle != null ? Integer.parseInt(obstacle.getProperty("health")) : 0;
    }

    public static int getAttack(final Obstacle obstacle, final int distance) {
        return getAttack(EObstacle.valueOf(obstacle), distance);
    }

    public static int getAttack(final EObstacle obstacle, final int distance) {
        return obstacle != null ? obstacle.getAttack(distance) : 0;
    }

    public static int getAttackRange(final Obstacle obstacle) {
        return getAttackRange(EObstacle.valueOf(obstacle));
    }

    public static int getAttackRange(final EObstacle obstacle) {
        return obstacle != null ? obstacle.getAttackRange() : 0;
    }

    public static boolean isEnemy(final Obstacle obstacle) {
        return isEnemy(EObstacle.valueOf(obstacle));
    }

    public static boolean isEnemy(final EObstacle obstacle) {
        return obstacle != null && obstacle.getAttackRange() > 0;
    }

    public static boolean isDisabled(final Obstacle obstacle) {
        if (obstacle == null) {
            return true;
        }

        if (isEnemy(obstacle)) {
            return !isAlive(obstacle);
        }

        if (EObstacle.THORNBUSH.equalsTo(obstacle)) {
            return Boolean.parseBoolean(obstacle.getProperty("burnt"));
        }

        return false;
    }

}