package persia.simulator;

import java.util.Map.Entry;

import cz.tieto.princegame.common.gameobject.Obstacle;

public abstract class TestObstacle implements Obstacle, Cloneable {

    private final int id;

    public TestObstacle() {
        id = System.identityHashCode(this);
    }

    public int getId() {
        return id;
    }

    public String getProperty(final String arg0) {
        if ("name".equals(arg0)) {
            return getName();
        }
        return null;
    }

    public abstract String getName();

    protected abstract boolean walkTo(final TestGame game);

    protected abstract boolean jumpOver(final TestGame game);

    protected abstract void nextTurn(final TestGame game);

    protected abstract void useEquipment(final TestGame game, final TestEquipment equipment);

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestObstacle clone() {
        try {
            return (TestObstacle) super.clone();
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestObstacle other = (TestObstacle) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    protected void doDamageToPrince(final TestGame game, final String who, final String reason, final int amount) {
        game.doDamageToPrince(who, reason, amount);
    }

    protected int getDistanceFromPrince(final TestGame game) {
        for (final Entry<Integer, TestObstacle> entry : game.getObstacles().entrySet()) {
            if (entry.getValue().equals(this)) {
                return entry.getKey() - game.getPrincePos();
            }
        }

        return Integer.MAX_VALUE;
    }

}