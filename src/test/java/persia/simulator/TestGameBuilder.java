/***************************************************************************************************
 * Copyright 2013 TeliaSonera. All rights reserved.
 **************************************************************************************************/
package persia.simulator;

import java.util.HashMap;
import java.util.Map;

import cz.tieto.princegame.common.gameobject.Equipment;
import cz.tieto.princegame.common.gameobject.Obstacle;

/**
 * TestSceneBuilder
 * 
 * @author <a href="mailto:kamil.siroky@teliasonera.com">Kamil Siroky</a>
 * 
 */
public class TestGameBuilder {
    private Integer princePos;
    private Integer princeHealth;
    private Integer gatePos;
    private Map<Integer, Obstacle> obstacles = new HashMap<Integer, Obstacle>();
    private Map<Integer, Equipment> equipments = new HashMap<Integer, Equipment>();

    private int levelLength;

    public static TestGameBuilder buildGame() {
        return new TestGameBuilder();
    }

    public TestGameBuilder withPrinceAtPossition(int pos) {
        this.princePos = pos;
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGameBuilder withPrinceHealth(int health) {
        this.princeHealth = health;
        return this;
    }

    public TestGameBuilder withGateAt(int pos) {
        this.gatePos = pos;
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGameBuilder addObstacle(Obstacle obstacle, int pos) {
        obstacles.put(pos, obstacle);
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGameBuilder addEquipment(Equipment obstacle, int pos) {
        equipments.put(pos, obstacle);
        levelLength = Math.max(levelLength, pos + 1);
        return this;
    }

    public TestGame build() {
        assert princeHealth != null;
        assert princePos != null;
        assert gatePos != null;

        TestPrince prince = new TestPrince(princeHealth, princeHealth);
        TestGame game = new TestGame(prince, levelLength, princePos, gatePos, obstacles, equipments);

        return game;
    }

}
