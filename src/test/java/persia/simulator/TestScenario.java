package persia.simulator;

import java.util.HashMap;
import java.util.Map;

public class TestScenario implements Cloneable {

    protected int princePos;
    protected int gatePos;
    protected int levelLength;
    protected Map<Integer, TestObstacle> obstacles;
    protected Map<Integer, TestEquipment> equipments;

    public TestScenario(final int levelLength, final Integer princePos, final Integer gatePos, final Map<Integer, TestObstacle> obstacles, final Map<Integer, TestEquipment> equipments) {
        super();
        this.levelLength = levelLength;
        this.princePos = princePos;
        this.gatePos = gatePos;
        this.equipments = equipments;
        this.obstacles = obstacles;
    }

    public int getLength() {
        return levelLength;
    }

    public int getGatePos() {
        return gatePos;
    }

    public int getPrincePos() {
        return princePos;
    }

    public TestEquipment getEquipment(final int pos) {
        return equipments.get(pos);
    }

    public TestObstacle getObstacle(final int pos) {
        return obstacles.get(pos);
    }

    public boolean isGate(final int pos) {
        return gatePos == pos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestScenario clone() {
        try {
            final TestScenario clone = (TestGame) super.clone();
            clone.equipments = new HashMap<Integer, TestEquipment>();
            clone.obstacles = new HashMap<Integer, TestObstacle>();
            for (final Map.Entry<Integer, TestEquipment> entry : equipments.entrySet()) {
                clone.equipments.put(entry.getKey(), entry.getValue().clone());
            }
            for (final Map.Entry<Integer, TestObstacle> entry : obstacles.entrySet()) {
                clone.obstacles.put(entry.getKey(), entry.getValue().clone());
            }
            return clone;
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}