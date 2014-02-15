package com.tieto.princegame.persia.domain;

import cz.tieto.princegame.common.gameobject.Equipment;

public enum EEquipment {

    SWORD("sword", false, 1, 1), MATCHES("matches", false, 0); // ?

    private String name;
    private boolean stackable;
    private int attackRange;
    private int[] attack;

    private EEquipment(final String name, final boolean stackable, final int attackRange, final int... attack) {
        this.name = name;
        this.stackable = stackable;
        this.attackRange = attackRange;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public boolean isStackable() {
        return stackable;
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

    public boolean equalsTo(final Equipment eq) {
        return eq != null && getName().equals(eq.getName());
    }

    public static EEquipment valueOf(final Equipment eq) {
        if (eq == null) {
            return null;
        }

        for (final EEquipment eEquipment : values()) {
            if (eEquipment.getName().equals(eq.getName())) {
                return eEquipment;
            }
        }
        return null;
    }

    public static int getAttack(final Equipment weapon, final int distance) {
        return getAttack(EEquipment.valueOf(weapon), distance);
    }

    public static int getAttack(final EEquipment weapon, final int distance) {
        return weapon != null ? weapon.getAttack(distance) : 0;
    }

    public static int getAttackRange(final Equipment weapon) {
        return getAttackRange(EEquipment.valueOf(weapon));
    }

    public static int getAttackRange(final EEquipment weapon) {
        return weapon != null ? weapon.getAttackRange() : 0;
    }

    public static boolean isStackable(final Equipment equipment) {
        return isStackable(EEquipment.valueOf(equipment));
    }

    public static boolean isStackable(final EEquipment equipment) {
        return equipment != null && equipment.isStackable();
    }

}