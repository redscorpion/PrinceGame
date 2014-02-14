/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.simulator;

import cz.tieto.princegame.common.gameobject.Equipment;

public interface TestObstacle {

    boolean walkTo(TestGame game);

    boolean jumpOver(TestGame game);

    void nextTurn(TestGame game);

    void useEquipment(TestGame game, Equipment equipment);

}