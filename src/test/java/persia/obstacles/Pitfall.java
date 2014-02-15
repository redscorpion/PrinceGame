package persia.obstacles;

import persia.simulator.TestEquipment;
import persia.simulator.TestGame;
import persia.simulator.TestObstacle;

public final class Pitfall extends TestObstacle {

    @Override
    public String getName() {
        return "pitfall";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean walkTo(final TestGame game) {
        doDamageToPrince(game, getName(), "walk to", Integer.MAX_VALUE);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean jumpOver(final TestGame game) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void nextTurn(final TestGame game) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void useEquipment(final TestGame game, final TestEquipment equipment) {
    }

}