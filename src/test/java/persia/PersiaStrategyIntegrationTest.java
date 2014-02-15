package persia;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import persia.equipment.Matches;
import persia.equipment.Sword;
import persia.obstacles.Chopper;
import persia.obstacles.Dragon;
import persia.obstacles.Knight;
import persia.obstacles.Pitfall;
import persia.obstacles.Thornbush;
import persia.simulator.TestGame;
import persia.simulator.TestGameBuilder;

import com.tieto.princegame.persia.PersiaStrategy;

@RunWith(JUnit4.class)
public class PersiaStrategyIntegrationTest {

    private void runGame(TestGame game) {
        final PersiaStrategy strategy = new PersiaStrategy();

        do {
            game = game.doStep(strategy.step(game.getPrince()));
        } while (!game.isOwer());

        if (game.getPrince().getHealth() <= 0) {
            fail("PRINCE DEAD");
        }
    }

    @Test
    public void test1() {
        final TestGameBuilder builder = TestGameBuilder.getBuilder();

        builder.setPrinceHealth(10);
        builder.setPrincePossition(3);
        builder.setGateAt(6);

        builder.addObstacle(new Knight(10), 1);
        builder.addEquipment(new Sword(), 4);
        builder.addObstacle(new Knight(10), 5);

        final TestGame game = builder.build();

        runGame(game);
    }

    @Test
    public void test2() {
        final TestGameBuilder builder = TestGameBuilder.getBuilder();

        builder.setPrinceHealth(10);
        builder.setPrincePossition(6);
        builder.setGateAt(20);

        builder.addObstacle(new Knight(4), 1);
        builder.addEquipment(new Sword(), 2);
        builder.addObstacle(new Pitfall(), 3);
        builder.addObstacle(new Chopper(), 5);
        builder.addObstacle(new Chopper(), 10);
        builder.addObstacle(new Knight(4), 12);
        builder.addObstacle(new Knight(4), 13);
        builder.addObstacle(new Dragon(4), 14);
        builder.addObstacle(new Dragon(4), 15);
        builder.addObstacle(new Pitfall(), 16);

        final TestGame game = builder.build();

        runGame(game);
    }

    @Test
    public void test3() {
        final TestGameBuilder builder = TestGameBuilder.getBuilder();

        builder.setPrinceHealth(10);
        builder.setPrincePossition(3);
        builder.setGateAt(7);

        builder.addObstacle(new Knight(4), 0);
        builder.addEquipment(new Matches(), 1);
        builder.addEquipment(new Sword(), 4);
        builder.addObstacle(new Thornbush(), 5);
        builder.addObstacle(new Dragon(4), 6);

        final TestGame game = builder.build();

        runGame(game);
    }

    @Test
    public void test4() {
        final TestGameBuilder builder = TestGameBuilder.getBuilder();

        builder.setPrinceHealth(10);
        builder.setPrincePossition(3);
        builder.setGateAt(7);

        builder.addObstacle(new Knight(4), 1);
        builder.addEquipment(new Matches(), 0);
        builder.addEquipment(new Sword(), 3);
        builder.addObstacle(new Thornbush(), 4);
        builder.addObstacle(new Dragon(4), 5);

        final TestGame game = builder.build();

        runGame(game);
    }

    @Test
    @Ignore
    public void test5() {
        final TestGameBuilder builder = TestGameBuilder.getBuilder();

        builder.setPrinceHealth(20);
        builder.setPrincePossition(3);
        builder.setGateAt(7);

        builder.addObstacle(new Knight(4), 2);
        builder.addEquipment(new Sword(), 3);
        builder.addObstacle(new Dragon(4), 4);

        final TestGame game = builder.build();

        runGame(game);
    }

}
