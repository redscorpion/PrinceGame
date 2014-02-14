/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.simulator;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;

public final class TestField implements Field {

    private final int pos;
    private final TestGame game;

    public TestField(int pos, TestGame game) {
        this.pos = pos;
        this.game = game;
    }

    public Equipment getEquipment() {
        return game.getEquipment(pos);
    }

    public Obstacle getObstacle() {
        return game.getObstacle(pos);
    }

    public boolean isGate() {
        return game.isGate(pos);
    }

    public Field getLookAt(int num) {
        return game.getLookAt(num);
    }

}