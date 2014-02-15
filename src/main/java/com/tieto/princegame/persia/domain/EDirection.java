package com.tieto.princegame.persia.domain;

public enum EDirection {
    FWD, BKW;

    public EDirection opposite() {
        return this == FWD ? BKW : FWD;
    }

    public static EDirection valueOf(int distance) {
        return distance < 0 ? BKW : distance > 0 ? FWD : null;
    }
}