package com.tieto.princegame.persia.domain;

import cz.tieto.princegame.common.gameobject.Equipment;

public enum EEquipment {

    SWORD("sword", 1); // ?

    private String name;
    private int attack;

    private EEquipment(String name, int attack) {
        this.name = name;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public boolean equalsTo(Equipment eq) {
        return eq != null && getName().equals(eq.getName());
    }

    public static EEquipment valueOf(Equipment eq) {
        if (eq == null) {
            return null;
        }

        for (EEquipment eEquipment : values()) {
            if (eEquipment.getName().equals(eq.getName())) {
                return eEquipment;
            }
        }
        return null;
    }
}