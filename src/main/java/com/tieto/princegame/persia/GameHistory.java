package com.tieto.princegame.persia;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Vector;

public class GameHistory extends Vector<StepStrategy> {

    private static final long serialVersionUID = -3266950713047993275L;

    public GameHistory() {
    }

    public GameHistory(final Collection<StepStrategy> history) {
        super(history);
    }

    @Override
    public synchronized StepStrategy lastElement() {
        try {
            return super.lastElement();
        } catch (final NoSuchElementException nse) {
            return null;
        }
    }

    @Override
    public synchronized StepStrategy firstElement() {
        try {
            return super.firstElement();
        } catch (final NoSuchElementException nse) {
            return null;
        }
    }
}