/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import cz.tieto.princegame.common.gameobject.Prince;

public final class TestGame implements Cloneable {

    private TestPrince prince;
    private int princePos;
    private int gatePos;
    private int levelLength;
    private Map<Integer, Obstacle> obstacles;
    private Map<Integer, Equipment> equipments;

    private Map<Integer, Field> fields = new HashMap<Integer, Field>();

    private boolean gameOver;

    public TestGame(TestPrince prince, int levelLength, int princePos, int gatePos) {
        this(prince, levelLength, princePos, gatePos, new HashMap<Integer, Obstacle>(), new HashMap<Integer, Equipment>());
    }

    public TestGame(TestPrince prince, int levelLength, Integer princePos, Integer gatePos, Map<Integer, Obstacle> obstacles, Map<Integer, Equipment> equipments) {
        this.prince = prince;
        this.levelLength = levelLength;
        this.princePos = princePos;
        this.gatePos = gatePos;
        this.equipments = equipments;
        this.obstacles = obstacles;
        prince.setGame(this);
    }

    public void addEquipment(int i, Equipment equipment) {
        equipments.put(i, equipment);
    }

    public void addObstacle(int pos, Obstacle obstacle) {
        obstacles.put(pos, obstacle);
    }

    private void doGrabEquipment() {
        if (equipments.get(princePos) != null) {
            Equipment removed = equipments.remove(princePos);
            prince.addToInventory(removed);
        }
    }

    private void doUseEquimpent(Equipment equipment, Obstacle target) {
        if (!prince.getInventory().contains(equipment)) {
            throw new IllegalArgumentException();
        }

        int distance = getDistanceFromPrince(target);
        if (Math.abs(distance) <= 1) {
            ((TestObstacle) target).useEquipment(this, equipment);
        }
    }

    private void doEnterGate() {
        if (isGate(princePos)) {
            gameOver = true;
            System.out.println("VICTORY");
        }
    }

    public int getLength() {
        return levelLength;
    }

    public int getGatePos() {
        return gatePos;
    }

    public Prince getPrince() {
        return prince;
    }

    public int getPrincePos() {
        return princePos;
    }

    public Equipment getEquipment(int pos) {
        return equipments.get(pos);
    }

    public Obstacle getObstacle(int pos) {
        return obstacles.get(pos);
    }

    public boolean isGate(int pos) {
        return gatePos == pos;
    }

    public Field getLookAt(int num) {
        int lookPos = princePos + num;

        if (lookPos < 0 || lookPos >= getLength()) {
            return null;
        }

        Field field = fields.get(lookPos);
        if (field == null) {
            field = new TestField(lookPos, this);
            fields.put(num, field);
        }

        return field;
    }

    public TestGame doStep(Action step) {
        if (gameOver) {
            throw new IllegalStateException();
        }

        TestGame gameClone = clone();
        if (step instanceof MoveForward) {
            gameClone.doMoveForward();
        } else if (step instanceof MoveBackward) {
            gameClone.doMoveBackward();
        } else if (step instanceof JumpForward) {
            gameClone.doJumpForward();
        } else if (step instanceof JumpBackward) {
            gameClone.doJumpBackward();
        } else if (step instanceof Grab) {
            gameClone.doGrabEquipment();
        } else if (step instanceof Use) {
            gameClone.doUseEquimpent(((Use) step).getEquipment(), ((Use) step).getObstacle());
        } else if (step instanceof Heal) {
            gameClone.doHeal();
        } else if (step instanceof EnterGate) {
            gameClone.doEnterGate();
        } else {
            throw new UnsupportedOperationException();
        }
        gameClone.ememyTurn();

        if (getPrince().getHealth() <= 0) {
            gameOver = true;
            System.out.println("PRINCE DEAD");
        }

        return gameClone;
    }

    private void doHeal() {
        prince.setHealth(prince.getHealth() + 1);
    }

    private void doJumpBackward() {
        TestObstacle skippedObstacle = (TestObstacle) obstacles.get(princePos - 1);
        if (princePos - 1 < 0 || (skippedObstacle != null && !skippedObstacle.jumpOver(this))) {
            return;
        }

        TestObstacle targetObstacle = (TestObstacle) obstacles.get(princePos - 2);
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
        TestObstacle targetObstacle = (TestObstacle) obstacles.get(princePos - 1);
        if (princePos - 1 >= 0 && (targetObstacle == null || targetObstacle.walkTo(this))) {
            princePos -= 1;
        }
    }

    private void doJumpForward() {
        TestObstacle skippedObstacle = (TestObstacle) obstacles.get(princePos + 1);
        if (princePos + 1 > levelLength - 1 || (skippedObstacle != null && !skippedObstacle.jumpOver(this))) {
            return;
        }

        TestObstacle targetObstacle = (TestObstacle) obstacles.get(princePos + 2);
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
        TestObstacle targetObstacle = (TestObstacle) obstacles.get(princePos + 1);
        if (princePos + 1 <= levelLength - 1 && (targetObstacle == null || targetObstacle.walkTo(this))) {
            princePos += 1;
        }
    }

    private void ememyTurn() {
        for (Map.Entry<Integer, Obstacle> entry : obstacles.entrySet()) {
            if (entry.getValue() instanceof TestObstacle) {
                TestObstacle enemy = (TestObstacle) entry.getValue();
                enemy.nextTurn(this);
            }
        }
    }

    public int getDistanceFromPrince(Obstacle target) {
        for (Entry<Integer, Obstacle> entry : obstacles.entrySet()) {
            if (entry.getValue() == target) {
                return entry.getKey() - princePos;
            }
        }

        return Integer.MAX_VALUE;
    }

    public void doDamageToPrince(String who, String reason, int amount) {
        prince.setHealth(prince.getHealth() - amount);
        System.out.println("prince received " + amount + " damage from " + who + " because '" + reason + "'");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestGame clone() {
        try {
            TestGame clone = (TestGame) super.clone();
            clone.equipments = new HashMap<Integer, Equipment>(equipments);
            clone.obstacles = new HashMap<Integer, Obstacle>(obstacles);
            clone.fields = new HashMap<Integer, Field>();
            clone.prince = prince.clone(clone);
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isOwer() {
        return gameOver;
    }
}