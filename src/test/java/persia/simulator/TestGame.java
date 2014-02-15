package persia.simulator;

import java.util.HashMap;
import java.util.Map;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;
import cz.tieto.princegame.common.action.Grab;
import cz.tieto.princegame.common.action.Heal;
import cz.tieto.princegame.common.action.JumpBackward;
import cz.tieto.princegame.common.action.JumpForward;
import cz.tieto.princegame.common.action.MoveBackward;
import cz.tieto.princegame.common.action.MoveForward;
import cz.tieto.princegame.common.action.Use;
import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Field;
import cz.tieto.princegame.common.gameobject.Obstacle;

public final class TestGame extends TestScenario implements Cloneable {

    protected TestPrince prince;

    private boolean gameOver;

    public TestGame(final TestPrince prince, final int levelLength, final int princePos, final int gatePos) {
        this(prince, levelLength, princePos, gatePos, new HashMap<Integer, TestObstacle>(), new HashMap<Integer, TestEquipment>());
    }

    public TestGame(final TestPrince prince, final int levelLength, final Integer princePos, final Integer gatePos, final Map<Integer, TestObstacle> obstacles,
            final Map<Integer, TestEquipment> equipments) {
        super(levelLength, princePos, gatePos, obstacles, equipments);
        this.prince = prince;
        this.prince.setGame(this);
    }

    public TestPrince getPrince() {
        return prince.clone();
    }

    private void doGrabEquipment() {
        if (equipments.get(princePos) != null) {
            final Equipment removed = equipments.remove(princePos);
            prince.addToInventory(removed);
        }
    }

    private void doUseEquimpent(final Equipment equipment, final Obstacle target) {
        if (equipment == null || target == null) {
            throw new IllegalArgumentException("null");
        }

        TestEquipment testEquipment = null;

        for (final Equipment eq : prince.getInventory()) {
            if (eq.equals(equipment)) {
                testEquipment = (TestEquipment) eq;
                break;
            }
        }

        if (testEquipment == null) {
            throw new IllegalArgumentException(equipment.toString());
        }

        TestObstacle testTarget = null;

        for (final Obstacle obstacle : obstacles.values()) {
            if (obstacle.equals(target)) {
                testTarget = (TestObstacle) obstacle;
                break;
            }
        }

        if (testTarget == null) {
            throw new IllegalArgumentException(target.toString());
        }

        final int distance = testTarget.getDistanceFromPrince(this);
        if (Math.abs(distance) <= 1) {
            testTarget.useEquipment(this, testEquipment);
        }
    }

    private void doEnterGate() {
        if (isGate(princePos)) {
            gameOver = true;
            System.out.println("VICTORY");
        }
    }

    Field getLookAt(final int num) {
        final int lookPos = princePos + num;

        if (lookPos < 0 || lookPos >= getLength()) {
            return null;
        }

        return new TestField(lookPos, this);
    }

    public TestGame doStep(final Action step) {
        if (gameOver) {
            throw new IllegalStateException("game ower");
        }

        final TestGame newScenario = clone();

        newScenario.princeTurn(step);
        newScenario.ememyTurn();

        if (getPrince().getHealth() <= 0) {
            newScenario.gameOver = true;
            System.out.println("PRINCE DEAD");
        }

        return newScenario;
    }

    private void princeTurn(final Action step) {
        if (MoveForward.class == step.getClass()) {
            doMoveForward();
        } else if (MoveBackward.class == step.getClass()) {
            doMoveBackward();
        } else if (JumpForward.class == step.getClass()) {
            doJumpForward();
        } else if (JumpBackward.class == step.getClass()) {
            doJumpBackward();
        } else if (Grab.class == step.getClass()) {
            doGrabEquipment();
        } else if (Use.class == step.getClass()) {
            doUseEquimpent(((Use) step).getEquipment(), ((Use) step).getObstacle());
        } else if (Heal.class == step.getClass()) {
            doHeal();
        } else if (EnterGate.class == step.getClass()) {
            doEnterGate();
        } else {
            throw new UnsupportedOperationException(step.getClass().getSimpleName());
        }
    }

    private void doHeal() {
        prince.setHealth(prince.getHealth() + 1);
    }

    private void doJumpBackward() {
        final TestObstacle skippedObstacle = obstacles.get(princePos - 1);
        if (princePos - 1 < 0 || (skippedObstacle != null && !skippedObstacle.jumpOver(this))) {
            return;
        }

        final TestObstacle targetObstacle = obstacles.get(princePos - 2);
        if (princePos - 2 >= 0 && (targetObstacle == null || targetObstacle.walkTo(this))) {
            princePos -= 2;
            return;
        }

        if ((princePos - 2 < 0 && (skippedObstacle == null || (skippedObstacle != null && skippedObstacle.walkTo(this))))
                || (skippedObstacle != null && skippedObstacle.walkTo(this))) {
            princePos -= 1;
        }
    }

    private void doMoveBackward() {
        final TestObstacle targetObstacle = obstacles.get(princePos - 1);
        if (princePos - 1 >= 0 && (targetObstacle == null || targetObstacle.walkTo(this))) {
            princePos -= 1;
        }
    }

    private void doJumpForward() {
        final TestObstacle skippedObstacle = obstacles.get(princePos + 1);
        if (princePos + 1 > levelLength - 1 || (skippedObstacle != null && !skippedObstacle.jumpOver(this))) {
            return;
        }

        final TestObstacle targetObstacle = obstacles.get(princePos + 2);
        if (princePos + 2 <= levelLength - 1 && (targetObstacle == null || targetObstacle.walkTo(this))) {
            princePos += 2;
            return;
        }

        if ((princePos + 2 > levelLength - 1 && (skippedObstacle == null || (skippedObstacle != null && skippedObstacle.walkTo(this))))
                || (skippedObstacle != null && skippedObstacle.walkTo(this))) {
            princePos += 1;
        }
    }

    private void doMoveForward() {
        final TestObstacle targetObstacle = obstacles.get(princePos + 1);
        if (princePos + 1 <= levelLength - 1 && (targetObstacle == null || targetObstacle.walkTo(this))) {
            princePos += 1;
        }
    }

    private void ememyTurn() {
        for (final TestObstacle enemy : obstacles.values()) {
            enemy.nextTurn(this);
        }
    }

    void doDamageToPrince(final String who, final String reason, final int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("" + amount);
        }
        prince.setHealth(prince.getHealth() - amount);
        System.out.println(-amount + " damage to prince from '" + who + "' because '" + reason + "'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestGame clone() {
        final TestGame clone = (TestGame) super.clone();
        clone.prince = prince.clone(clone);
        return clone;
    }

    public boolean isOwer() {
        return gameOver;
    }

    Map<Integer, TestObstacle> getObstacles() {
        return obstacles;
    }

    Map<Integer, TestEquipment> getEquipments() {
        return equipments;
    }
}