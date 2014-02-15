package persia.obstacles;

import persia.equipment.Sword;
import persia.simulator.TestEquipment;
import persia.simulator.TestGame;
import persia.simulator.TestObstacle;

public final class Knight extends TestObstacle {

    private int health;

    public Knight(final int health) {
        this.health = health;
    }

    @Override
    public String getProperty(final String arg0) {
        if ("dead".equals(arg0)) {
            return "" + (health <= 0);
        }
        if ("health".equals(arg0)) {
            return "" + health;
        }
        return super.getProperty(arg0);
    }

    @Override
    public String getName() {
        return "knight";
    }

    public int getHealth() {
        return health;
    }

    private int getDamage(final int distance) {
        if (Math.abs(distance) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean walkTo(final TestGame game) {
        if (getHealth() <= 0) {
            return true;
        }
        doDamageToPrince(game, getName(), "walk to", 2);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean jumpOver(final TestGame game) {
        if (getHealth() <= 0) {
            return true;
        }
        doDamageToPrince(game, getName(), "walk to", 2);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void nextTurn(final TestGame game) {
        if (getHealth() > 0) {
            final int damage = getDamage(getDistanceFromPrince(game));
            if (damage > 0) {
                doDamageToPrince(game, getName(), "prince in attack range", damage);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void useEquipment(final TestGame game, final TestEquipment equipment) {
        if (getHealth() > 0 && Sword.NAME.equals(equipment.getName())) {
            health -= 1;
        }
    }

}