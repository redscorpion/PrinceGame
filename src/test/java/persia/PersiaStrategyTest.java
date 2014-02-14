/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persia;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import persia.equipment.Sword;
import persia.obstacles.Chopper;
import persia.obstacles.Dragon;
import persia.obstacles.Knight;
import persia.obstacles.Pitfall;
import persia.simulator.TestGame;
import persia.simulator.TestPrince;

import com.tieto.princegame.persia.PersiaStrategy;

import cz.tieto.princegame.common.action.Action;
import cz.tieto.princegame.common.action.EnterGate;

@RunWith(MockitoJUnitRunner.class)
public class PersiaStrategyTest {

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

    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() {
        TestPrince prince = new TestPrince(10, 10);
        TestGame game = new TestGame(prince, 21, 7, 19);
        game.addObstacle(2, new Knight(5));
        game.addEquipment(3, new Sword());
        game.addObstacle(4, new Pitfall());
        game.addObstacle(6, new Chopper());
        game.addObstacle(11, new Chopper());
        game.addObstacle(13, new Knight(5));
        game.addObstacle(14, new Knight(5));
        game.addObstacle(15, new Dragon(5));
        game.addObstacle(16, new Dragon(5));
        game.addObstacle(17, new Pitfall());

        PersiaStrategy strategy = new PersiaStrategy();

        Action step;

        do {
            step = strategy.step(game.getPrince());
            game = game.doStep(step);
            if (game.isGate(game.getPrincePos()) && (step instanceof EnterGate)) {
                System.out.println("VICTORY");
                break;
            }
            if (game.getPrince().getHealth() <= 0) {
                System.out.println("PRINCE DEAD");
                break;
            }
        } while (true);
    }

}
