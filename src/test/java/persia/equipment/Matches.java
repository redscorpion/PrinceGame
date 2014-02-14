/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.equipment;

import cz.tieto.princegame.common.gameobject.Equipment;

public class Matches implements Equipment {

    public String getProperty(String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        return null;
    }

    public String getName() {
        return "matches";
    }

    public int getId() {
        return System.identityHashCode(this);
    }
}