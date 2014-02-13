package com.mycompany.princeextreme;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Vector;

public class GameHistory extends Vector<TurnStrategy> {

    private static final long serialVersionUID = -3266950713047993275L;

    public GameHistory() {
    }

    public GameHistory(Collection<TurnStrategy> history) {
        super(history);
    }

    @Override
    public synchronized TurnStrategy lastElement() {
        try {
            return super.lastElement();
        } catch (NoSuchElementException nse) {
            return null;
        }
    }

    @Override
    public synchronized TurnStrategy firstElement() {
        try {
            return super.firstElement();
        } catch (NoSuchElementException nse) {
            return null;
        }
    }
}