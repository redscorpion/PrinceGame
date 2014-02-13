package com.mycompany.princeextreme;

public enum EDirection {
    FWD, BKW;

    public EDirection opposite() {
        return this == FWD ? BKW : FWD;
    }
}