/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package com.mycompany.princeextreme;

public enum EDirection {
    FWD, BKW;

    public EDirection opposite() {
        return this == FWD ? BKW : FWD;
    }
}