package persia.obstacles;

import persia.simulator.TestEquipment;
import persia.simulator.TestGame;
import persia.simulator.TestObstacle;

public final class Chopper extends TestObstacle {

    private enum State {
        OPENING, CLOSING, CLOSED;

        public State next() {
            return State.values()[(ordinal() + 1) % State.values().length];
        }
    };

    private State state = State.CLOSED;

    @Override
    public String getProperty(final String arg0) {
        if ("closing".equals(arg0)) {
            return "" + isClosing();
        }
        if ("opening".equals(arg0)) {
            return "" + isOpening();
        }
        return super.getProperty(arg0);
    }

    private boolean isOpening() {
        return State.OPENING == state;
    }

    private boolean isClosing() {
        return State.CLOSING == state;
    }

    @Override
    public String getName() {
        return "chopper";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean walkTo(final TestGame game) {
        doDamageToPrince(game, getName(), "walk to", 2);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean jumpOver(final TestGame game) {
        if (!isOpening() || isClosing()) {
            doDamageToPrince(game, getName(), "jump over", 2);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void nextTurn(final TestGame game) {
        state = state.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void useEquipment(final TestGame game, final TestEquipment equipment) {
    }

}