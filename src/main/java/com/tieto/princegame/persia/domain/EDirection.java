package com.tieto.princegame.persia.domain;

public enum EDirection {
    FWD, BKW;

    public EDirection opposite() {
        return this == FWD ? BKW : FWD;
    }
}