package persia.simulator;

import cz.tieto.princegame.common.gameobject.Equipment;

public abstract class TestEquipment implements Equipment, Cloneable {

    private final int id;

    public TestEquipment() {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestEquipment clone() {
        try {
            return (TestEquipment) super.clone();
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
        final TestEquipment other = (TestEquipment) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
