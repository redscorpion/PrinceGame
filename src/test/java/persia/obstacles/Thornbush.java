package persia.obstacles;

import persia.equipment.Matches;
import persia.simulator.TestEquipment;
import persia.simulator.TestGame;
import persia.simulator.TestObstacle;

public final class Thornbush extends TestObstacle {

    boolean burnt = false;

    @Override
    public String getProperty(final String arg0) {
        if ("burnt".equals(arg0)) {
            return "" + burnt;
        }
        return super.getProperty(arg0);
    }

    @Override
    public String getName() {
        return "thornbush";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean walkTo(final TestGame game) {
        if (!burnt) {
            doDamageToPrince(game, getName(), "walk to", 2);
        }
        return burnt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean jumpOver(final TestGame game) {
        if (!burnt) {
            doDamageToPrince(game, getName(), "jump over", 2);
        }
        return burnt;
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
        if (Matches.NAME.equals(equipment.getName())) {
            burnt = true;
        }
    }

}