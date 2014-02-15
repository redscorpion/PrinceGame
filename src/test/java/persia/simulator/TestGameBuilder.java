package persia.simulator;

import java.util.HashMap;
import java.util.Map;

public class TestGameBuilder {
    private Integer princePos;
    private Integer princeHealth;
    private Integer gatePos;
    private final Map<Integer, TestObstacle> obstacles = new HashMap<Integer, TestObstacle>();
    private final Map<Integer, TestEquipment> equipments = new HashMap<Integer, TestEquipment>();

    private int levelLength;

    private TestGameBuilder() {
    }

    public static TestGameBuilder getBuilder() {
        return new TestGameBuilder();
    }

    public TestGameBuilder setPrincePossition(final int pos) {
        princePos = pos;
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGameBuilder setPrinceHealth(final int health) {
        princeHealth = health;
        return this;
    }

    public TestGameBuilder setGateAt(final int pos) {
        gatePos = pos;
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGameBuilder addObstacle(final TestObstacle obstacle, final int pos) {
        obstacles.put(pos, obstacle);
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGameBuilder addEquipment(final TestEquipment obstacle, final int pos) {
        equipments.put(pos, obstacle);
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGame build() {
        assert princeHealth != null;
        assert princePos != null;
        assert gatePos != null;

        final TestPrince prince = new TestPrince(princeHealth, princeHealth);
        final TestGame game = new TestGame(prince, levelLength, princePos, gatePos, obstacles, equipments);

        return game;
    }

}
