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
        testField.setPrinceHealth(5);
        testField.setPrinceMaxHealth(5);
        testField.setPos(2);
        testField.setGatePos(7);
        testField.setLength(8);
        testField.addObstacle(4, new Pitfall());
        testField.addObstacle(6, new Knight(5));
        testField.addEquipment(1, new Sword());

        PersiaStrategy strategy = new PersiaStrategy();

        Action step;

        do {
            step = strategy.step(prince);
            updateGameState(step);
        } while (!(step instanceof EnterGate) && testField.getPrinceHealth() > 0);
    }

    /**
     * @param step
     */
    private void updateGameState(Action step) {
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
        testField = testField.clone();
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

    private final class Knight implements Obstacle {

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
            if (prev != null && prev.getObstacle() instanceof Knight) {
                Knight knight = ((Knight) prev.getObstacle());
                if (knight.getHealth() > 0) {
                    princeHealth -= EObstacle.KNIGHT.getAttack();
                }
            }
            TestField next = getLookAt(1);
            if (next != null && next.getObstacle() instanceof Knight) {
                Knight knight = ((Knight) next.getObstacle());
                if (knight.getHealth() > 0) {
                    princeHealth -= EObstacle.KNIGHT.getAttack();
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
                if (prev != null && prev.getObstacle() instanceof Knight) {
                    Knight knight = ((Knight) prev.getObstacle());
                    knight.setHealth(knight.getHealth() - EObstacle.KNIGHT.getAttack());
                }
                TestField next = getLookAt(1);
                if (next != null && next.getObstacle() instanceof Knight) {
                    Knight knight = ((Knight) next.getObstacle());
                    knight.setHealth(knight.getHealth() - EObstacle.KNIGHT.getAttack());
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