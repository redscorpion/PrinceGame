package persia.simulator;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;

public final class TestField implements Field {

    private final int pos;
    private final TestGame game;

    public TestField(final int pos, final TestGame game) {
        this.pos = pos;
        this.game = game;
    }

    public Equipment getEquipment() {
        final TestEquipment equipment = game.getEquipment(pos);
        return equipment != null ? equipment.clone() : null;
    }

    public Obstacle getObstacle() {
        final TestObstacle obstacle = game.getObstacle(pos);
        return obstacle != null ? obstacle.clone() : null;
    }

    public boolean isGate() {
        return game.isGate(pos);
    }

    public Field getLookAt(final int num) {
        return game.getLookAt(num);
    }

}