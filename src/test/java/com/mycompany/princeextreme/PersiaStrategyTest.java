/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.princeextreme;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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

@RunWith(MockitoJUnitRunner.class)
public class PersiaStrategyTest {

    private TestField testField;
    private Prince prince;

    public PersiaStrategyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testField = new TestField();

        createPrince(testField);

    }

    private void createPrince(final TestField testField) {
        prince = Mockito.mock(Prince.class);

        when(prince.look(0)).thenAnswer(new Answer<Field>() {
            public Field answer(InvocationOnMock invocation) throws Throwable {
                return testField;
            }
        });

        when(prince.look(-1)).thenAnswer(new Answer<Field>() {
            public Field answer(InvocationOnMock invocation) throws Throwable {
                return testField.getLookAt(-1);
            }
        });

        when(prince.look(1)).thenAnswer(new Answer<Field>() {
            public Field answer(InvocationOnMock invocation) throws Throwable {
                return testField.getLookAt(1);
            }
        });

        when(prince.getHealth()).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return testField.getPrinceHealth();
            }
        });

        when(prince.getMaxHealth()).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return testField.getPriceMaxHealth();
            }
        });

        when(prince.getInventory()).thenAnswer(new Answer<Collection<Equipment>>() {
            public Collection<Equipment> answer(InvocationOnMock invocation) throws Throwable {
                return testField.getInventory();
            }
        });
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() {
        testField.setPrinceHealth(20);
        testField.setPrinceMaxHealth(10);
        testField.setPos(3);
        testField.setGatePos(8);
        testField.setLength(9);
        testField.addObstacle(7, new Chopper());
        testField.addObstacle(4, new Knight(10));
        testField.addEquipment(3, new Sword());
        testField.addObstacle(2, new Knight(10));

        PersiaStrategy strategy = new PersiaStrategy();

        Action step;

        do {
            createPrince(testField);
            step = strategy.step(prince);
            updateGameState(step);
        } while (!(step instanceof EnterGate) && testField.getPrinceHealth() > 0);
    }

    /**
     * @param step
     */
    private void updateGameState(Action step) {
        testField = testField.clone();
        if (step instanceof MoveForward) {
            testField.setPos(testField.getPos() + 1);
        } else if (step instanceof MoveBackward) {
            testField.setPos(testField.getPos() - 1);
        } else if (step instanceof JumpForward) {
            testField.setPos(testField.getPos() + 2);
        } else if (step instanceof JumpBackward) {
            testField.setPos(testField.getPos() - 2);
        } else if (step instanceof Grab) {
            testField.grabEquipment();
        } else if (step instanceof Use) {
            testField.useEquimpent(((Use) step).getEquipment());
        } else if (step instanceof Heal) {
            testField.setPrinceHealth(testField.getPrinceHealth() + 1);
        }
        testField.ememyTurn();
    }

    private final class Pitfall implements Obstacle {
        public String getProperty(String arg0) {
            if ("name".equals(arg0)) {
                return getName();
            }
            return null;
        }

        public String getName() {
            return "pitfall";
        }

        public int getId() {
            return System.identityHashCode(this);
        }
    }

    private final class Chopper implements Obstacle {
        public String getProperty(String arg0) {
            if ("name".equals(arg0)) {
                return getName();
            }
            if ("closing".equals(arg0)) {
                return "" + false;
            }
            if ("opening".equals(arg0)) {
                return "" + true;
            }
            return null;
        }

        public String getName() {
            return "chopper";
        }

        public int getId() {
            return System.identityHashCode(this);
        }
    }

    private final class Sword implements Equipment {
        public String getProperty(String arg0) {
            if ("name".equals(arg0)) {
                return getName();
            }
            return null;
        }

        public String getName() {
            return "sword";
        }

        public int getId() {
            return System.identityHashCode(this);
        }
    }

    private interface KillableObstacle {
        void setHealth(int h);

        int getHealth();
    }

    private final class Knight implements Obstacle, KillableObstacle {

        private int health;

        public Knight(int health) {
            this.health = health;
        }

        public String getProperty(String arg0) {
            if ("name".equals(arg0)) {
                return getName();
            }
            if ("dead".equals(arg0)) {
                return "" + (health <= 0);
            }
            if ("health".equals(arg0)) {
                return "" + health;
            }
            return null;
        }

        public String getName() {
            return "knight";
        }

        public int getId() {
            return System.identityHashCode(this);
        }

        public int getHealth() {
            return health;
        }

        /**
         * @param i
         */
        public void setHealth(int i) {
            this.health = i;
        }
    }

    private final class Dragon implements Obstacle, KillableObstacle {

        private int health;

        public Dragon(int health) {
            this.health = health;
        }

        public String getProperty(String arg0) {
            if ("name".equals(arg0)) {
                return getName();
            }
            if ("dead".equals(arg0)) {
                return "" + (health <= 0);
            }
            if ("health".equals(arg0)) {
                return "" + health;
            }
            return null;
        }

        public String getName() {
            return "dragon";
        }

        public int getId() {
            return System.identityHashCode(this);
        }

        public int getHealth() {
            return health;
        }

        /**
         * @param i
         */
        public void setHealth(int i) {
            this.health = i;
        }
    }

    class TestField implements Field, Cloneable {

        private int pos;
        private int princeHealth;
        private int gatePos;
        private int length;
        private Map<Integer, Obstacle> obstacles = new HashMap<Integer, Obstacle>();
        private Map<Integer, Equipment> equipments = new HashMap<Integer, Equipment>();
        private List<Equipment> inventory = new ArrayList<Equipment>();
        private int priceMaxHealth;

        public TestField() {
        }

        public void setPrinceMaxHealth(int i) {
            this.priceMaxHealth = i;
        }

        public int getPriceMaxHealth() {
            return priceMaxHealth;
        }

        public void ememyTurn() {
            TestField prev = getLookAt(-1);
            if (prev != null && prev.getObstacle() instanceof KillableObstacle) {
                KillableObstacle knight = ((KillableObstacle) prev.getObstacle());
                if (knight.getHealth() > 0) {
                    princeHealth -= Utils.getAttack(prev.getObstacle(), 1);
                }
            }
            TestField next = getLookAt(1);
            if (next != null && next.getObstacle() instanceof KillableObstacle) {
                KillableObstacle knight = ((KillableObstacle) next.getObstacle());
                if (knight.getHealth() > 0) {
                    princeHealth -= Utils.getAttack(next.getObstacle(), 1);
                }
            }
        }

        public Collection<Equipment> getInventory() {
            return inventory;
        }

        public void addEquipment(int i, Equipment equipment) {
            equipments.put(i, equipment);
        }

        public void grabEquipment() {
            if (equipments.get(pos) != null) {
                Equipment removed = equipments.remove(pos);
                inventory.add(removed);
            }
        }

        public void useEquimpent(Equipment eq) {
            if (inventory.contains(eq)) {
                TestField prev = getLookAt(-1);
                if (prev != null && prev.getObstacle() instanceof KillableObstacle) {
                    KillableObstacle knight = ((KillableObstacle) prev.getObstacle());
                    knight.setHealth(knight.getHealth() - Utils.getAttack(prev.getObstacle(), 1));
                }
                TestField next = getLookAt(1);
                if (next != null && next.getObstacle() instanceof KillableObstacle) {
                    KillableObstacle knight = ((KillableObstacle) next.getObstacle());
                    knight.setHealth(knight.getHealth() - Utils.getAttack(next.getObstacle(), 1));
                }
            }
        }

        public void addObstacle(int i, Obstacle obstacle) {
            obstacles.put(i, obstacle);
        }

        public void setLength(int i) {
            this.length = i;
        }

        public int getLength() {
            return length;
        }

        public void setGatePos(int i) {
            this.gatePos = i;
        }

        public int getGatePos() {
            return gatePos;
        }

        public int getPrinceHealth() {
            return princeHealth;
        }

        public void setPrinceHealth(int princeHealth) {
            this.princeHealth = princeHealth;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public int getPos() {
            return pos;
        }

        public Equipment getEquipment() {
            return equipments.get(pos);
        }

        public Obstacle getObstacle() {
            return obstacles.get(pos);
        }

        public boolean isGate() {
            return gatePos == pos;
        }

        public TestField getLookAt(int num) {
            int newPos = pos + num;

            if (newPos < 0 || newPos >= getLength()) {
                return null;
            }

            TestField clone = clone();
            clone.setPos(newPos);
            return clone;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected TestField clone() {
            try {
                TestField clone = (TestField) super.clone();
                return clone;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
